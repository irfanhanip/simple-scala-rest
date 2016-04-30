package com.hanip.ssr.dao

import com.hanip.ssr.models.Cart

import scala.concurrent.Future

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

/**
 * Created by hanip on 4/30/16.
 */
class CartDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Carts = TableQuery[CartsTable]

  def all(): Future[Seq[Cart]] = db.run(Carts.result)

  def insert(cart: Cart): Future[Unit] = db.run(Carts += cart).map { _ => () }

  private class CartsTable(tag: Tag) extends Table[Cart](tag, "Cart") {
    def id = column[Int]("id", O.PrimaryKey)
    def couponId = column[Int]("coupon_id")
    def status = column[Int]("status")
    def totalAmount = column[BigDecimal]("total_amount")
    def currency = column[String]("currency")

    def * = (id, couponId, status, totalAmount, currency) <> (Cart.tupled, Cart.unapply _)
  }
}