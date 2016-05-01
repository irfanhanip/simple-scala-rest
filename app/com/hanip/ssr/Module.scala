package com.hanip.ssr

import java.time.Clock

import com.google.inject.{AbstractModule, Provides}
import com.hanip.ssr.dao.CartDAO.CartTable
import com.hanip.ssr.dao.CouponDAO.CouponTable
import com.hanip.ssr.dao.ItemCartDAO.ItemCartTable
import com.hanip.ssr.dao.ItemDAO.ItemTable
import com.hanip.ssr.dao._
import com.hanip.ssr.models._

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {
  override def configure() = {
    // Use the system clock as the default implementation of Clock
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

}



