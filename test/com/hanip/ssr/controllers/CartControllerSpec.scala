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
import play.api.libs.json.{JsNumber, JsObject, JsString}
import play.api.test._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CartControllerSpec extends PlaySpecification with Results with Matchers with Mockito {
  sequential

  val daoMock = mock[BaseDAO[CartTable, Cart]]

  val application = new GuiceApplicationBuilder().overrides(new AbstractModule {
    override def configure() = {
      bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    }

    @Provides
    def provideItemDAO: AbstractBaseDAO[ItemTable, Item] = mock[BaseDAO[ItemTable, Item]]

    @Provides
    def provideCartDAO: AbstractBaseDAO[CartTable, Cart] = daoMock

    @Provides
    def provideItemCartDAO: AbstractBaseDAO[ItemCartTable, ItemCart] = mock[BaseDAO[ItemCartTable, ItemCart]]

    @Provides
    def provideCouponDAO: AbstractBaseDAO[CouponTable, Coupon] = mock[BaseDAO[CouponTable, Coupon]]
  }).build

  "Routes" should {

    "send 204 when there isn't a /cart/0" in {
      daoMock.findById(0).returns(Future {
        None
      })
      route(application, FakeRequest(GET, "/cart/0")).map(
        status(_)) shouldEqual Some(NO_CONTENT)
    }

    "send 200 when there is a /cart/1" in {
      daoMock.findById(1).returns(Future {
        Some(Cart(1, 1, -99, 0, "IDR"))
      })
      route(application, FakeRequest(GET, "/cart/1")).map(
        status(_)) shouldEqual Some(OK)
    }
  }

}

