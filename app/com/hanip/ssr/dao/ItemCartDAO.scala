package com.hanip.ssr.dao

import com.hanip.ssr.models.ItemCart
import org.joda.time.DateTime

import scala.concurrent.Future

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import com.hanip.ssr.helpers.SlickMapping

import scala.concurrent.Future

class ItemCartDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val ItemCarts = TableQuery[ItemCartsTable]

  def all(): Future[Seq[ItemCart]] = db.run(ItemCarts.result)

  def insert(itemCart: ItemCart): Future[Unit] = db.run(ItemCarts += itemCart).map { _ => () }

  private class ItemCartsTable(tag: Tag) extends Table[ItemCart](tag, "ItemCart") {
   def itemId = column[Int]("item_id", O.PrimaryKey)
   def cartId = column[Int]("cart_id", O.PrimaryKey)
   def createdOn = column[DateTime]("created_on")

   def * = (itemId, cartId, createdOn) <> (ItemCart.tupled, ItemCart.unapply _)
  }
 }