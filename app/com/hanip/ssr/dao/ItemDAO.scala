package com.hanip.ssr.dao

import com.hanip.ssr.models.Item
import com.hanip.ssr.persistence.SlickTables.BaseTable
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

/**
 * Created by hanip on 4/30/16.
 */
object ItemDAO {
  protected lazy val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import dbConfig.driver.api._

  class ItemTable(tag: Tag) extends BaseTable[Item](tag, "items") {
    def name = column[String]("name")

    def price = column[BigDecimal]("price")

    def desc = column[String]("desc")

    def * = (id, name, price, desc) <>(Item.tupled, Item.unapply _)
  }

  implicit val itemsTableQ: TableQuery[ItemTable] = TableQuery[ItemTable]
}
