package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Vendor(id: Pk[Long], name: String, phone: String)

object Vendor   {

  val simple = {
    get[Pk[Long]]("vendor.id") ~
    get[String]("vendor.name") ~
    get[String]("vendor.phone") map {
      case id~name~phone => Vendor(
        id, name, phone
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

}
