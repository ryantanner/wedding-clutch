package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

import models._

object Users extends Controller with Secured  {

  def listAll = Action {
    val users = User.findAll

    Ok(Json.toJson(users))
  }

  def byId(id: Long) = Action {
    User.findById(id).map { user =>
      Ok(Json.toJson(user))
    }.getOrElse {
      NotFound(Json.toJson(Map(
        "status" -> "404",
        "message" -> "No such user exists"
      )))
    }
  }

  def create = Action(parse.json) { request =>
    val userJson = request.body
    val user = userJson.as[User]

    User.create(user)

    Ok(Json.toJson(Map(
      "status" -> "200",
      "message" -> "User successfully created"
    )))
  }

  def updateAll = TODO

  def update(id: Long) = TODO

  def deleteAll = TODO

  def delete(id: Long) = TODO

}
