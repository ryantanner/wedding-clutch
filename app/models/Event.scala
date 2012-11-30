package models

import play.api.db._
import play.api.Play.current

import play.api.libs.json._
import play.api.libs.json.Json

import anorm._
import anorm.SqlParser._

import auth._

case class Event(id: Pk[Long], name: String, order: Int, coordinatorId: Long, weddingId: Long, vendorIds: Seq[Long])

object Event   {

  val simple = {
    get[Pk[Long]]("event.id") ~
    get[String]("event.name") ~
    get[Int]("event.timeline_order") ~
    get[Long]("event.coordinator_id") ~
    get[Long]("event.wedding_id") map {
      case id~name~order~coordinatorId~weddingId => Event(
        id, name, order, 
        coordinatorId, weddingId,
        Event.findVendors(id.get).map(_.id.get)
      )
    }
  }
 
  def findById(id: Long, coordinatorId: Long): Option[Event] = {
    DB.withConnection { implicit connection =>
      SQL("""
        select * from event 
        where id = {id} and coordinator_id = {coordinator_id}
      """).on(
        'id -> id,
        'coordinator_id -> coordinatorId
      ).as(Event.simple.singleOpt)
    }
  }

  def findByWeddingId(weddingId: Long, coordinatorId: Long): Seq[Event] = {
    DB.withConnection { implicit connection =>
      SQL("""
        select * from event
        where wedding_id = {wedding_id} and coordinator_id = {coordinator_id}
      """).on(
        'wedding_id -> weddingId,
        'coordinator_id -> coordinatorId
      ).as(Event.simple *)
    }
  }

  def findByCoordinatorId(coordinatorId: Long): Seq[Event] = {
    DB.withConnection { implicit connection =>
      SQL("""
        select * from event
        where coordinator_id = {coordinator_id}
      """).on(
        'coordinator_id -> coordinatorId
      ).as(Event.simple *)
    }
  }

  def findByCoordinator(coordinator: Account): Seq[Event] =
    coordinator.id.map { id =>
      findByCoordinatorId(coordinator.id.get)
    }.getOrElse(Nil)

  def findVendors(eventId: Long): Seq[Vendor] = {
    DB.withConnection { implicit connection =>
      SQL("""
        select * from events_vendors 
        where event_id = {event_id}
      """).on(
        'event_id -> eventId
      ).apply().map(row =>
        row[Long]("event_id") -> (row[Long]("vendor_id"), row[Long]("coordinator_id"))
      ).toList.map(_._2).map(vendorAndCoordinatorIds =>
        Vendor.findById(vendorAndCoordinatorIds._1, vendorAndCoordinatorIds._2).get
      )
    }
  }

  implicit object EventFormat extends Format[Event] {

    def writes(e: Event): JsValue = JsObject(Seq(
      "id" -> JsNumber(e.id.get),
      "name" -> JsString(e.name),
      "timeline_order" -> JsNumber(e.order),
      "coordinator_id" -> JsNumber(e.coordinatorId),
      "wedding_id" -> JsNumber(e.weddingId),
      "vendor_ids" -> JsArray(e.vendorIds.map(JsNumber(_)))
    ))

    def reads(json: JsValue): Event = Event(
      (json \ "id").asOpt[Long].map(id =>
        Id(id)
      ).getOrElse(NotAssigned),
      (json \ "name").as[String],
      (json \ "timeline_order").as[Int],
      (json \ "coordinator_id").as[Long],
      (json \ "wedding_id").as[Long],
      (json \ "vendor_ids").as[Seq[Long]]
    )

  }

}

