package controllers

import play.api._
import play.api.mvc._
import play.api.data._

import play.api.libs.json.Json

import java.util.Date

import anorm._

import models._
import views._

object Weddings extends AuthController {

  def index(id: Long) = IsAuthenticated { user => _ =>
    val weddings = Wedding.findByCoordinator(user)
    Ok(html.weddings.weddings(
      user,
      weddings,
      Wedding.findById(id,user)))
  }

  def main = index(0)

  // REST API

  def listAll = IsAuthenticated { user => _ =>
    val weddings = Wedding.findByCoordinator(user)
    Ok(Json.toJson(weddings))
  }

  def byId(id: Long) = IsCoordinatorOf(id) { user => _ =>
    Wedding.findById(id, user).map { wedding =>
      Ok(Json.toJson(wedding))
    }.getOrElse(NotFound)
  }


  def create = IsAuthenticated { user => implicit request => 
    request.body.asJson.map { weddingJson =>
      val wedding = weddingJson.as[Wedding]

      Wedding.create(wedding).map(newWedding =>
        Ok(Json.toJson(Map(
          "status" -> "200",
          "message" -> "Wedding successfully created"
        )))
      ).getOrElse(InternalServerError)
    }.getOrElse(BadRequest)
  }

  def updateAll = TODO

  def update(id: Long) = TODO

  def deleteAll = TODO

  def delete(id: Long) = TODO

}
