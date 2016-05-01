package com.hanip.ssr.controllers

import javax.inject._

import com.hanip.ssr.dao.CartDAO.CartTable
import com.hanip.ssr.dao.CouponDAO.CouponTable
import com.hanip.ssr.dao.ItemCartDAO.ItemCartTable
import com.hanip.ssr.dao.ItemDAO.ItemTable
import com.hanip.ssr.dao.{CouponDAO, AbstractBaseDAO, ItemCartDAO}
import com.hanip.ssr.models.{Cart, Coupon, Item, ItemCart}
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

/**
 * Created by hanip on 5/1/16.
 */
@Singleton
class CartController @Inject()(cartDAO: AbstractBaseDAO[CartTable, Cart], itemDAO: AbstractBaseDAO[ItemTable, Item],
                               couponDAO: AbstractBaseDAO[CouponTable, Coupon], itemCartDAO: AbstractBaseDAO[ItemCartTable, ItemCart])(
                                implicit exec: ExecutionContext) extends Controller {

  implicit val cartWriters = new Writes[Cart] {
    def writes(o: Cart): JsValue = Json.obj(
      "id" -> o.id,
      "coupon_id" -> o.couponId,
      "currency" -> o.totalAmount,
      "total_amount" -> o.currency
    )
  }

  def show(id: Int) = Action.async {
    cartDAO.findById(id) map { c => c.fold(NoContent)(c => Ok(Json.toJson(c))) }
  }

  def getCartAmount(cartId: Int): Future[BigDecimal] = {
    for {
      itemCart <- ItemCartDAO.findByCartId(cartId)
      item <- itemDAO.findAll()
    } yield {
      item.filter(i => itemCart.map(_.id).contains(i.id)).foldLeft(BigDecimal(0))(_ + _.price)
    }
  }

  def calculateVoucher(cart: Cart, grossAmount: BigDecimal): BigDecimal = {
    if (cart.couponId > 0) {
      val q = couponDAO.findById(cart.couponId)

      val optCoupon = Await.result(q, 60.seconds)

      if (optCoupon.isEmpty) {
        return 0
      }

      val coupon = optCoupon.get

      if (coupon.couponType == 1) {
        return grossAmount - coupon.fixDiscount
      } else if (coupon.couponType == 2) {
        return grossAmount * (100 - coupon.percentDiscount) / 100
      }
    }
    0
  }

  def updateCartAttribute(cartId: Int): Result = {
    val q1 = cartDAO.findById(cartId)
    val q2 = getCartAmount(cartId)

    val optCart = Await.result(q1, 60.seconds)
    val grossAmount: BigDecimal = Await.result(q2, 60.seconds)

    if (optCart.isEmpty) {
      return InternalServerError("There was an error at the server")
    }

    val cart: Cart = optCart.get

    val totalAmount: BigDecimal = calculateVoucher(cart, grossAmount)

    val cartUpdate = cartDAO.update(Cart(cartId, cart.couponId, cart.status, totalAmount, cart.currency)) map {
      _ => Ok(Json.obj(
        "cart_id" -> cartId,
        "total_amount" -> totalAmount,
        "currency" -> cart.currency
      ))
    }

    Await.result(cartUpdate, 60.seconds)
  }

  def addItem() = Action.async(parse.json) {
    request => {
      for {
        cartId <- (request.body \ "cart_id").asOpt[Int]
        itemId <- (request.body \ "item_id").asOpt[Int]
      } yield {
        //TODO: Should be in transaction
        (itemCartDAO.insert(ItemCart(0, itemId, cartId)) map {
          n => updateCartAttribute(cartId)
        }).recoverWith {
          case e => Future {
            e.printStackTrace()
            InternalServerError("There was an error at the server")
          }
        }
      }
    }.getOrElse(Future {
      BadRequest("Wrong json format")
    })
  }

  def removeItem() = Action.async(parse.json) {
    request => {
      for {
        cartId <- (request.body \ "cart_id").asOpt[Int]
        itemId <- (request.body \ "item_id").asOpt[Int]
      } yield {
        //TODO: Should be in transaction
        (ItemCartDAO.deleteByItemCart(ItemCart(0, itemId, cartId)) map {
          n => updateCartAttribute(cartId)
        }).recoverWith {
          case e => Future {
            e.printStackTrace()
            InternalServerError("There was an error at the server")
          }
        }
      }
    }.getOrElse(Future {
      BadRequest("Wrong json format")
    })
  }


  def validateCoupon(coupon: Coupon): Boolean = {
    //TODO: validate expiration time, used, etc
    true
  }


  def updateCoupon(cartId: Int, couponCode: String): Future[Result] = {
    val q1 = CouponDAO.findByCode(couponCode)
    val q2 = cartDAO.findById(cartId)

    val optCoupon = Await.result(q1, 60.seconds)
    val optCart = Await.result(q2, 60.seconds)

    if (optCart.isEmpty) {
      return Future(BadRequest("Invalid cart id"))
    }

    val cart: Cart = optCart.get

    if (optCoupon.isEmpty) {
      return Future(BadRequest("Invalid coupon code"))
    }

    val coupon: Coupon = optCoupon.get

    if (!validateCoupon(coupon)) {
      return Future(BadRequest("Coupon expired"))
    }

    (cartDAO.update(Cart(cartId, coupon.id, cart.status, 0, cart.currency)) map {
      n => updateCartAttribute(cartId)
    }).recoverWith {
      case e => Future {
        e.printStackTrace()
        InternalServerError("There was an error at the server")
      }
    }
  }

  def useCoupon() = Action.async(parse.json) {
    request => {
      for {
        cartId <- (request.body \ "cart_id").asOpt[Int]
        couponCode <- (request.body \ "coupon_code").asOpt[String]
      } yield
      updateCoupon(cartId, couponCode)
    }.getOrElse(Future {
      BadRequest("Wrong json format")
    })
  }

  def listItems(id: Int) = play.mvc.Results.TODO
}
