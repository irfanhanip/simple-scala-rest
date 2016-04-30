package com.hanip.ssr.dao

import com.hanip.ssr.models.Coupon
import org.joda.time.DateTime

import scala.concurrent.Future

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import com.hanip.ssr.helpers.SlickMapping

class CouponDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._
  private val Coupons = TableQuery[CouponsTable]
 
  def all(): Future[Seq[Coupon]] = db.run(Coupons.result)
 
  def insert(coupon: Coupon): Future[Unit] = db.run(Coupons += coupon).map { _ => () }
 
  private class CouponsTable(tag: Tag) extends Table[Coupon](tag, "Coupon") {
    def id = column[Int]("id", O.PrimaryKey)
    def code = column[String]("code")
    def couponType = column[Int]("coupon_type")
    def fixDiscount = column[BigDecimal]("fix_discount")
    def percentDiscount = column[BigDecimal]("percent_discount")
    def validityStart = column[DateTime]("validity_start")
    def validityEnd = column[DateTime]("validity_end")
    def currency = column[String]("currency")

    def * = (id, code, couponType, fixDiscount, percentDiscount, validityStart, validityEnd, currency) <> (Coupon.tupled, Coupon.unapply _)
  }
 }