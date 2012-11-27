package models

import play.api.db._
import play.api.Play.current

import play.api.libs.json._
import play.api.libs.json.Json

import anorm._
import anorm.SqlParser._

case class Vendor(id: Pk[Long], name: String, role: String, phone: String, coordinatorId: Long)

object Vendor   {

  val simple = {
    get[Pk[Long]]("vendor.id") ~
    get[String]("vendor.name") ~
    get[String]("vendor.role") ~
    get[String]("vendor.phone") ~
    get[Long]("vendor.coordinator_id") map {
      case id~name~role~phone~coordinatorId => Vendor(
        id, name, role, phone, coordinatorId
      )
    }
  }

  def findById(id: Long, coordinatorId: Long): Option[Vendor] = {
    DB.withConnection { implicit connection =>
      SQL("""
        select * from vendor 
        where id = {id} and coordinator_id = {coordinator_id}
      """).on(
        'id -> id,
        'coordinator_id -> coordinatorId
      ).as(Vendor.simple.singleOpt)
    }
  }

  def findById(id: Long, coordinator: User): Option[Vendor] = {
    findById(id, coordinator.id.get)
  }

  def findByCoordinatorId(coordinatorId: Long): Seq[Vendor] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select * from vendor
          where vendor.coordinator_id = {coordinator_id}
        """
      ).on(
        'coordinator_id -> coordinatorId
      ).as(Vendor.simple *)
    }
  }

  def findByCoordinator(coordinator: User): Seq[Vendor] = {
    coordinator.id.map { id =>
      findByCoordinatorId(id)
    }.getOrElse(Nil)
  }

  def findByRole(role: String, coordinator: User): Seq[Vendor] = {
    coordinator.id.map { coordinatorId =>
      DB.withConnection { implicit connection =>
        SQL("""
              select * from vendor
              where role = {role} and coordinator_id = {coordinatorId}
        """).on(
          'role -> role,
          'coordinatorId -> coordinatorId
        ).as(Vendor.simple *)
      }
    }.getOrElse(Nil)
  }

  implicit object VendorFormat extends Format[Vendor] {

    def writes(v: Vendor): JsValue = {
      JsObject(Seq(
        "id" -> JsNumber(v.id.get),
        "name" -> JsString(v.name),
        "role" -> JsString(v.role),
        "phone" -> JsString(v.phone),
        "coordinator_id" -> JsNumber(v.coordinatorId)
      ))
    }

    def reads(json: JsValue): Vendor = Vendor(
      (json \ "id").asOpt[Long].map(id =>
        Id(id)
      ).getOrElse(NotAssigned),
      (json \ "name").as[String],
      (json \ "role").as[String],
      (json \ "phone").as[String],
      (json \ "coordinator_id").as[Long]
    )

  }


}
