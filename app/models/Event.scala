package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Event(id: Pk[Long], name: String, order: Int, wedding: Wedding, vendors: Seq[Vendor])

object Event   {

  val simple = {
    get[Pk[Long]]("event.id") ~
    get[String]("event.name") ~
    get[String]("event.phone") map {
      case id~name~phone => event(
        id, name, phone
      )
    }
  }

  def findById(id: Long): Option[event] = {
    DB.withConnection { implicit connection =>
      SQL("select * from event where id = {id}").on(
        'id -> id
      ).as(event.simple.singleOpt)
    }
  }

}

