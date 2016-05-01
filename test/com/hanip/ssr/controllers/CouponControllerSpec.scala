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

class CouponControllerSpec extends PlaySpecification with Results with Matchers with Mockito {
  sequential

  val daoMock = mock[BaseDAO[CouponTable, Coupon]]

  val application = new GuiceApplicationBuilder().overrides(new AbstractModule {
    override def configure() = {
      bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    }

    @Provides
    def provideItemDAO: AbstractBaseDAO[ItemTable, Item] = mock[BaseDAO[ItemTable, Item]]

    @Provides
    def provideCartDAO: AbstractBaseDAO[CartTable, Cart] = mock[BaseDAO[CartTable, Cart]]

    @Provides
    def provideItemCartDAO: AbstractBaseDAO[ItemCartTable, ItemCart] = mock[BaseDAO[ItemCartTable, ItemCart]]

    @Provides
    def provideCouponDAO: AbstractBaseDAO[CouponTable, Coupon] = daoMock
  }).build

  "Routes" should {

    "send 200 when get empty /coupons" in {
      daoMock.findAll.returns(Future {
        Seq()
      })
      route(application, FakeRequest(GET, "/coupons")).map(
        status(_)) shouldEqual Some(OK)
    }

    "send 200 when get /coupons" in {
      daoMock.findAll.returns(Future {
        Seq(Coupon(0, "TL2016", 1, 2000.00, -99, 1462035032, 1493571032, "IDR"))
      })
      route(application, FakeRequest(GET, "/coupons")).map(
        status(_)) shouldEqual Some(OK)
    }

  }

}

