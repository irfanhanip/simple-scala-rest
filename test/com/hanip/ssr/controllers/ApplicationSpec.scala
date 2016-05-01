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
import play.api.test._

class ApplicationSpec extends PlaySpecification with Results with Matchers with Mockito{
  sequential

  val application = new GuiceApplicationBuilder().overrides(new AbstractModule {
    override def configure() = {
      bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    }

    @Provides
    def provideItemDAO: AbstractBaseDAO[ItemTable, Item] = new BaseDAO[ItemTable, Item]

    @Provides
    def provideCartDAO: AbstractBaseDAO[CartTable, Cart] = new BaseDAO[CartTable, Cart]

    @Provides
    def provideItemCartDAO: AbstractBaseDAO[ItemCartTable, ItemCart] = new BaseDAO[ItemCartTable, ItemCart]

    @Provides
    def provideCouponDAO: AbstractBaseDAO[CouponTable, Coupon] = new BaseDAO[CouponTable, Coupon]
  }).build

  "Routes" should {

    "send 404 on a bad request" in  {
      route(application, FakeRequest(GET, "/stolen")).map(status(_)) shouldEqual Some(NOT_FOUND)
    }

  }

}

