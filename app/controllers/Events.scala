package controllers

import play.api._
import play.api.mvc._
import play.api.data._

import java.util.Date

import anorm._

import models._

object Events extends Controller with Secured {

  // REST API

  def listAll(weddingId: Long) = TODO

  def byId(weddingId: Long, id: Long) = TODO

  def create(weddingId: Long) = TODO

  def updateAll(weddingId: Long) = TODO

  def update(weddingId: Long, id: Long) = TODO

  def deleteAll(weddingId: Long) = TODO

  def delete(weddingId: Long, id: Long) = TODO

  def trigger(eventId: Long) = TODO

}
