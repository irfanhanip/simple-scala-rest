package com.hanip.ssr.dao

import com.hanip.ssr.models.{ItemCart, Cart}
import com.hanip.ssr.persistence.SlickTables.BaseTable
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
 * Created by hanip on 4/30/16.
 */
object CartDAO {
  protected lazy val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import dbConfig.driver.api._

  class CartTable(tag: Tag) extends BaseTable[Cart](tag, "carts") {
    def couponId = column[Int]("coupon_id")

    //TODO: resolve issue cannot resolve nullable
    //def couponId = column[Option[Int]]("coupon_id", O.Nullable)

    def status = column[Int]("status")

    def totalAmount = column[BigDecimal]("total_amount")

    def currency = column[String]("currency")

    def * = (id, couponId, status, totalAmount, currency) <> (Cart.tupled, Cart.unapply _)
  }

  implicit val cartsTableQ: TableQuery[CartTable] = TableQuery[CartTable]
}
