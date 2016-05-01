package com.hanip.ssr.dao

import com.hanip.ssr.models.ItemCart
import com.hanip.ssr.persistence.SlickTables.BaseTable
import play.api.Play
import play.api.db.slick.{HasDatabaseConfig, DatabaseConfigProvider}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.lifted.Tag

import scala.concurrent.Future

/**
 * Created by hanip on 4/30/16.
 */
object ItemCartDAO extends HasDatabaseConfig[JdbcProfile] {
  protected lazy val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import dbConfig.driver.api._

  class ItemCartTable(tag: Tag) extends BaseTable[ItemCart](tag, "item_carts") {
    def isEqual(cart: ItemCart): Rep[Boolean] = {
      itemId === cart.itemId && cartId === cart.cartId
    }

    def itemId = column[Int]("item_id")

    def cartId = column[Int]("cart_id")

    def * = (id, itemId, cartId) <>(ItemCart.tupled, ItemCart.unapply _)
  }

  implicit val itemsTableQ: TableQuery[ItemCartTable] = TableQuery[ItemCartTable]

  def findByCartId(id : Int): Future[Seq[ItemCart]] = {
    db.run(itemsTableQ.filter(_.cartId === id).result)
  }

  def deleteByItemCart(cart: ItemCart): Future[Int] = {
    db.run(itemsTableQ.filter(_.isEqual(cart)).delete)
  }
}
