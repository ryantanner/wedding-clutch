package controllers

import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.json.Json

import models._

object Vendors extends Controller with Secured  {

  def listAll = IsAuthenticated { user => _ =>
    val vendors = Vendor.findByCoordinator(user)
    Ok(Json.toJson(vendors))
  }

  def byId(vendorId: Long) = IsAuthenticated { user => _ =>
    val vendor = Vendor.findById(vendorId, user)
    Ok(Json.toJson(vendor))
  }

  def create = TODO

  def updateAll = TODO

  def update(vendorId: Long) = TODO

  def deleteAll = TODO

  def delete(vendorId: Long) = TODO


}
