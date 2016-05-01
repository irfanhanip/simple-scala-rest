package com.hanip.ssr.dao

import com.hanip.ssr.models.BaseModel
import com.hanip.ssr.persistence.SlickTables.BaseTable
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.lifted.{CanBeQueryCondition, TableQuery}

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext


trait AbstractBaseDAO[T, A] {

  def insert(row: A): Future[Int]

  def insert(rows: Seq[A]): Future[Seq[Int]]

  def update(row: A): Future[Int]

  def update(rows: Seq[A]): Future[Unit]

  def findAll(): Future[Seq[A]]

  def findById(id: Int): Future[Option[A]]

  def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]]

  def deleteById(id: Int): Future[Int]

  def deleteById(ids: Seq[Int]): Future[Int]

  def deleteByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Int]
}


class BaseDAO[T <: BaseTable[A], A <: BaseModel]()(implicit val tableQ: TableQuery[T]) extends AbstractBaseDAO[T, A] with HasDatabaseConfig[JdbcProfile] {
  protected lazy val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import dbConfig.driver.api._

  def insert(row: A): Future[Int] = {
    insert(Seq(row)).map(_.head)
  }

  def insert(rows: Seq[A]): Future[Seq[Int]] = {
    db.run(tableQ returning tableQ.map(_.id) ++= rows.filter(_.isValid))
  }

  def update(row: A): Future[Int] = {
    if (row.isValid)
      db.run(tableQ.filter(_.id === row.id).update(row))
    else
      Future {
        0
      }
  }

  def update(rows: Seq[A]): Future[Unit] = {
    db.run(DBIO.seq((rows.filter(_.isValid).map(r => tableQ.filter(_.id === r.id).update(r))): _*))
  }

  def findAll(): Future[Seq[A]] = {
    db.run(tableQ.result)
  }

  def findById(id: Int): Future[Option[A]] = {
    db.run(tableQ.filter(_.id === id).result.headOption)
  }

  def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]] = {
    db.run(tableQ.withFilter(f).result)
  }

  def deleteById(id: Int): Future[Int] = {
    deleteById(Seq(id))
  }

  def deleteById(ids: Seq[Int]): Future[Int] = {
    db.run(tableQ.filter(_.id.inSet(ids)).delete)
  }

  def deleteByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Int] = {
    db.run(tableQ.withFilter(f).delete)
  }

}