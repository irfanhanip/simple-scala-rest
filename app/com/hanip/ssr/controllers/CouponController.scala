package com.hanip.ssr.controllers

import javax.inject._

import com.hanip.ssr.dao.AbstractBaseDAO
import com.hanip.ssr.dao.CouponDAO.CouponTable
import com.hanip.ssr.models.Coupon
import play.api.libs.json.{Json, Writes}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by hanip on 4/30/16.
 */
@Singleton
class CouponController @Inject()(couponDAO: AbstractBaseDAO[CouponTable, Coupon])(implicit exec: ExecutionContext) extends Controller {

  implicit val couponsWrites = new Writes[Coupon] {
    def writes(c: Coupon) = Json.obj(
      "id" -> c.id,
      "code" -> c.code,
      "coupon_type" -> c.couponType,
      "fix_discount" -> c.fixDiscount,
      "percent_discount" -> c.percentDiscount,
      "validity_start" -> c.validityStart,
      "validity_end" -> c.validityEnd,
      "currency" -> c.currency
    )
  }

  def list = Action.async {
    couponDAO.findAll map { i => Ok(Json.toJson(i)) }
  }
}
