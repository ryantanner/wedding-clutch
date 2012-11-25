package controllers

import play.api._
import play.api.mvc._
import play.api.data._

import play.api.libs.json.Json

import java.util.Date

import anorm._

import models._
import views._

object Weddings extends Controller with Secured {

  def index(id: Long = 0) = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
      val weddings = Wedding.findByCoordinatorId(user.id.get)
      Ok(html.weddings.weddings(
        user,
        weddings,
        Wedding.findById(id,user.id.get)))
    }.getOrElse(Forbidden)
  }


  // REST API

  def listAll = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
      val weddings = Wedding.findByCoordinatorId(user.id.get)
      Ok(Json.toJson(weddings))
    }.getOrElse(Forbidden)
  }

  def byId(id: Long) = IsCoordinatorOf(id) { username => _ =>
    User.findByEmail(username).map { user =>
      Wedding.findById(id, user.id.get).map { wedding =>
        Ok(Json.toJson(wedding))
      }.getOrElse(NotFound)
    }.getOrElse(Forbidden)
  }


  def create = TODO

  def updateAll = TODO

  def update(id: Long) = TODO

  def deleteAll = TODO

  def delete(id: Long) = TODO

}
