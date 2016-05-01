package com.hanip.ssr.persistence

import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile

/**
 * The companion object.
 */
object SlickTables extends HasDatabaseConfig[JdbcProfile] {
  protected lazy val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import dbConfig.driver.api._

  abstract class BaseTable[T](tag: Tag, name: String) extends Table[T](tag, name) {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  }

}
