package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Event(id: Pk[Long], name: String, order: Int, coordinator: User, wedding: Wedding, vendors: Seq[Vendor])

object Event   {

  val simple = {
    get[Pk[Long]]("event.id") ~
    get[String]("event.name") ~
    get[Int]("event.timeline_order") ~
    get[Long]("event.coordinator_id") ~
    get[Long]("event.wedding_id") map {
      case id~name~order~coordinatorId~weddingId => Event(
        id, name, order, 
        User.findById(coordinatorId).get, 
        Wedding.findById(weddingId, coordinatorId).get, 
        Event.findVendors(id.get)
      )
    }
  }

  def findById(id: Long): Option[Event] = {
    DB.withConnection { implicit connection =>
      SQL("select * from event where id = {id}").on(
        'id -> id
      ).as(Event.simple.singleOpt)
    }
  }

  def findVendors(id: Long): Seq[Vendor] = Nil

}

