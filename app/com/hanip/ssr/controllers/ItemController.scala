package com.hanip.ssr.controllers

import javax.inject._

import com.hanip.ssr.dao.AbstractBaseDAO
import com.hanip.ssr.dao.ItemDAO.ItemTable
import com.hanip.ssr.models.Item
import play.api.libs.json.{Json, Writes}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by hanip on 4/30/16.
 */
@Singleton
class ItemController @Inject()(itemDAO: AbstractBaseDAO[ItemTable, Item])(implicit exec: ExecutionContext) extends Controller {

  implicit val itemsWrites = new Writes[Item] {
    def writes(i: Item) = Json.obj(
      "id" -> i.id,
      "name" -> i.name,
      "price" -> i.price,
      "desc" -> i.desc
    )
  }

  def list = Action.async {
    itemDAO.findAll map { i => Ok(Json.toJson(i)) }
  }

  def show(id: Int) = Action.async {
    itemDAO.findById(id) map { i => i.fold(NoContent)(i => Ok(Json.toJson(i))) }
  }

  def create = Action.async(parse.json) {
    request => {
      for {
        name <- (request.body \ "name").asOpt[String]
        price <- (request.body \ "price").asOpt[BigDecimal]
        desc <- (request.body \ "desc").asOpt[String]
      } yield {
        (itemDAO.insert(Item(0, name, price, desc)) map { n => Ok(Json.obj("id" -> n)) }).recoverWith {
          case e => Future {
            e.printStackTrace()
            InternalServerError("There as an error at the server")
          }
        }
      }
    }.getOrElse(Future {
      BadRequest("Wrong json format")
    })
  }
}
