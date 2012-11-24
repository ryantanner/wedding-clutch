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
      val weddings = Wedding.findByOwner(user.name)
      Ok(html.weddings.weddings(
        user,
        weddings,
        Wedding.findById(id)))
    }.getOrElse(Forbidden)
  }


  // REST API

  def listAll = TODO

  def byId(id: Long) = TODO

  def create = TODO

  def updateAll = TODO

  def update(id: Long) = TODO

  def deleteAll = TODO

  def delete(id: Long) = TODO

}
