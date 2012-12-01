package controllers

import play.api._
import play.api.mvc._
import play.api.data._

import play.api.libs.json._
import play.api.libs.json.Json

import java.util.Date

import anorm._

import models._

object Events extends AuthController {
 
  // REST API

  def listAll(weddingId: Long) = IsCoordinatorOf(weddingId) { user => implicit request =>
    val events = Event.findByWeddingId(weddingId, user.id.get)
    Ok(Json.toJson(events))
  }

  def byId(weddingId: Long, eventId: Long) = IsCoordinatorOf(weddingId) { user => implicit request =>
    Event.findById(eventId, user.id.get).map { event =>
      Ok(Json.toJson(event))
    }.getOrElse(NotFound)
  }

  def create(weddingId: Long) = IsCoordinatorOf(weddingId) { user => implicit request =>
    request.body.asJson.map { json =>
      val event = json.as[Event]

      Event.create(event).map(newEvent =>
        Ok(Json.toJson(Map(
          "status" -> Json.toJson("200"),
          "message" -> Json.toJson("New event created"),
          "event" -> Json.toJson(newEvent)
        )))
      ).getOrElse(InternalServerError)
    }.getOrElse(BadRequest)
  }

  def updateAll(weddingId: Long) = TODO

  def update(weddingId: Long, id: Long) = TODO

  def deleteAll(weddingId: Long) = TODO

  def delete(weddingId: Long, id: Long) = TODO

  def trigger(eventId: Long) = TODO
 
}
