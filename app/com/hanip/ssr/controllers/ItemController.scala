package com.hanip.ssr.controllers

import javax.inject.Inject
import com.hanip.ssr.models.Item
import play.api.mvc.{Action, Controller}
import play.libs.Json
import com.hanip.ssr.dao.ItemDAO

class ItemController @Inject() (itemDao:ItemDAO) extends Controller {
  def list() = Action.async { request =>

    itemDao.all().map {
      items => Ok(Json.toJson(items))
    }
  }

  def show(itemId: Int) = play.mvc.Results.TODO
  /*
    def show(itemId: Int) = Action.async { request =>
      itemDao.findById(itemId).map { item =>
        item.fold {
          i => Ok(Json.toJson(i))
        } { i => Ok(Json.toJson(i))
        }
      }
    }
  */

  def create() = play.mvc.Results.TODO
}