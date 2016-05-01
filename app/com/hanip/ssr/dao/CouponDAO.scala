package com.hanip.ssr.dao

import com.hanip.ssr.models.Coupon
import com.hanip.ssr.persistence.SlickTables.BaseTable
import play.api.Play
import play.api.db.slick.{HasDatabaseConfig, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
 * Created by hanip on 4/30/16.
 */
object CouponDAO extends HasDatabaseConfig[JdbcProfile] {

  protected lazy val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import dbConfig.driver.api._

  class CouponTable(tag: Tag) extends BaseTable[Coupon](tag, "coupons") {
    def code = column[String]("code")

    def couponType = column[Int]("coupon_type")

    def fixDiscount = column[BigDecimal]("fix_discount")

    def percentDiscount = column[BigDecimal]("percent_discount")

    def validityStart = column[Long]("validity_start")

    def validityEnd = column[Long]("validity_end")

    def currency = column[String]("currency")

    def * = (id, code, couponType, fixDiscount, percentDiscount, validityStart, validityEnd, currency) <>(Coupon.tupled, Coupon.unapply _)
  }

  implicit val couponsTableQ: TableQuery[CouponTable] = TableQuery[CouponTable]

  def findByCode(code: String): Future[Option[Coupon]] = {
    db.run(couponsTableQ.filter(_.code === code).result.headOption)
  }
}
