package com.hanip.ssr.dao

import com.hanip.ssr.models.Item

import scala.concurrent.Future

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

/**
 * Created by hanip on 4/30/16.
 */
class ItemDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Items = TableQuery[ItemsTable]

  def all(): Future[Seq[Item]] = db.run(Items.result)

  def findById(itemId: Int) = db.run {
    Items.filter { f =>
      f.id == itemId
    }.result.headOption
  }

  def insert(item: Item): Future[Unit] = db.run(Items += item).map { _ => () }

  private class ItemsTable(tag: Tag) extends Table[Item](tag, "Item") {
    def id = column[Int]("id", O.PrimaryKey)
    def name = column[String]("name")
    def price = column[BigDecimal]("price")
    def desc = column[String]("desc")

    def * = (id, name, price, desc) <> (Item.tupled, Item.unapply _)
  }
}