package com.hanip.ssr.controllers

import java.time.Clock

import com.google.inject.{AbstractModule, Provides}
import com.hanip.ssr.dao.CartDAO.CartTable
import com.hanip.ssr.dao.CouponDAO.CouponTable
import com.hanip.ssr.dao.ItemCartDAO.ItemCartTable
import com.hanip.ssr.dao.ItemDAO.ItemTable
import com.hanip.ssr.dao.{AbstractBaseDAO, BaseDAO}
import com.hanip.ssr.models._
import org.specs2.execute.Results
import org.specs2.matcher.Matchers
import org.specs2.mock.Mockito
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsObject, JsString}
import play.api.test._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ItemControllerSpec extends PlaySpecification with Results with Matchers with Mockito {
  sequential

  val daoMock = mock[AbstractBaseDAO[ItemTable, Item]]

  val application = new GuiceApplicationBuilder().overrides(new AbstractModule {
    override def configure() = {
      bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    }

    @Provides
    def provideItemDAO: AbstractBaseDAO[ItemTable, Item] = daoMock

    @Provides
    def provideCartDAO: AbstractBaseDAO[CartTable, Cart] = mock[BaseDAO[CartTable, Cart]]

    @Provides
    def provideItemCartDAO: AbstractBaseDAO[ItemCartTable, ItemCart] = mock[BaseDAO[ItemCartTable, ItemCart]]

    @Provides
    def provideCouponDAO: AbstractBaseDAO[CouponTable, Coupon] = mock[BaseDAO[CouponTable, Coupon]]
  }).build

  "Routes" should {

    "send 200 when get /items" in {
      daoMock.findAll.returns(Future {
        Seq(Item(1, "name", BigDecimal.apply(42.00), "desc"))
      })
      route(application, FakeRequest(GET, "/items")).map(
        status(_)) shouldEqual Some(OK)
    }

    "send 204 when there isn't a /item/0" in {
      daoMock.findById(0).returns(Future {
        None
      })
      route(application, FakeRequest(GET, "/item/0")).map(
        status(_)) shouldEqual Some(NO_CONTENT)
    }

    "send 200 when there is a /item/1" in {
      daoMock.findById(1).returns(Future {
        Some(Item(1, "name", BigDecimal.apply(42.00), "desc"))
      })
      route(application, FakeRequest(GET, "/item/1")).map(
        status(_)) shouldEqual Some(OK)
    }

    "send 415 when post to create a item without json type" in {
      route(application, FakeRequest(POST, "/item/create")).map(
        status(_)) shouldEqual Some(UNSUPPORTED_MEDIA_TYPE)
    }

    "send 400 when post to create a item with empty json" in {
      route(application,
        FakeRequest(POST, "/item/create", FakeHeaders(("Content-type", "application/json") :: Nil), JsObject(Seq()))).map(
        status(_)) shouldEqual Some(BAD_REQUEST)
    }

    "send 400 when post to create a item with wrong json" in {
      route(application,
        FakeRequest(POST, "/item/create", FakeHeaders(("Content-type", "application/json") :: Nil), JsObject(Seq("wrong" -> JsString("wrong"))))).map(
        status(_)) shouldEqual Some(BAD_REQUEST)
    }

    "send 200 when post to create a item with valid json" in {
      val (name, price, desc) = ("Apple", BigDecimal.apply(5000.00), "Shut up and take my money")
      daoMock.insert(Item(0, name, price, desc)).returns(Future {
        1
      })
      route(application,
        FakeRequest(POST, "/item/create", FakeHeaders(("Content-type", "application/json") :: Nil),
          JsObject(Seq("name" -> JsString(name), "price" -> JsString(price.toString()), "desc" -> JsString(desc))))).map(
        status(_)) shouldEqual Some(OK)
    }

    "send 500 when post to create a item with valid json" in {
      val (name, price, desc) = ("Apple", BigDecimal.apply(5000.00), "Shut up and take my money")
      daoMock.insert(Item(0, name, price, desc)).returns(Future.failed {
        new Exception("Slick exception")
      })
      route(application,
        FakeRequest(POST, "/item/create", FakeHeaders(("Content-type", "application/json") :: Nil),
          JsObject(Seq("name" -> JsString(name), "price" -> JsString(price.toString()), "desc" -> JsString(desc))))).map(
        status(_)) shouldEqual Some(INTERNAL_SERVER_ERROR)
    }


  }

}

