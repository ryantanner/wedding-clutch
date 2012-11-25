package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Vendor(id: Pk[Long], name: String, role: String, phone: String, coordinator: User)

object Vendor   {

  val simple = {
    get[Pk[Long]]("vendor.id") ~
    get[String]("vendor.name") ~
    get[String]("vendor.role") ~
    get[String]("vendor.phone") ~
    get[Long]("vendor.coordinator_id") map {
      case id~name~role~phone~coordinatorId => Vendor(
        id, name, role, phone, User.findById(coordinatorId).get
      )
    }
  }

  def findById(id: Long): Option[Vendor] = {
    DB.withConnection { implicit connection =>
      SQL("select * from vendor where id = {id}").on(
        'id -> id
      ).as(Vendor.simple.singleOpt)
    }
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

}
