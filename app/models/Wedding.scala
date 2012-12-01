package models

import play.api.db._
import play.api.Play.current
import play.api.Logger

import play.api.libs.json.Json
import play.api.libs.json._

import anorm._
import anorm.SqlParser._

import java.util.Date
import java.text.SimpleDateFormat

import auth._

case class Wedding(id: Pk[Long], name: String, date: Date, venue: String, coordinatorId: Long)

object Wedding {

  val simple = {
    get[Pk[Long]]("wedding.id") ~
    get[String]("wedding.name") ~
    get[Date]("wedding.date") ~
    get[String]("wedding.venue") ~
    get[Long]("wedding.coordinator_id") map {
      case id~name~date~venue~coordinatorId => Wedding(
        id, name, date, venue, coordinatorId
      )
    }
  }

  def findAll: Seq[Wedding] = {
    Logger.debug("Entering Wedding.findAll")

    val result = DB.withConnection { implicit connection =>
      SQL("select * from wedding").as(Wedding.simple *)
    }

    Logger.debug("Returning from Wedding.findAll")
    result
  }

  def findById(id: Long, coordinatorId: Long): Option[Wedding] = {
    DB.withConnection { implicit connection =>
      SQL("select * from wedding where id = {id} and coordinator_id = {coordinator_id}").on(
        'id -> id,
        'coordinator_id -> coordinatorId
      ).as(Wedding.simple.singleOpt)
    }
  }

  def findById(id: Long, coordinator: Account): Option[Wedding] =
    findById(id, coordinator.id.get)

  // def findWeddingInvolving
  // allow searching by vendor

  def findByCoordinatorId(coordinatorId: Long): Seq[Wedding] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select * from wedding
          where wedding.coordinator_id = {coordinator_id}
        """
      ).on(
        'coordinator_id -> coordinatorId 
      ).as(Wedding.simple *)
    }
  }

  def findByCoordinator(coordinator: Account): Seq[Wedding] = {
    Logger.debug("Entering Wedding.findByCoordinator with parameter coordinator.id = %d".format(coordinator.id.get))
    val result = coordinator.id.map { id =>
      findByCoordinatorId(id)
    }.getOrElse(Nil)

    Logger.debug("Returning Wedding.findByCoordinator with list length = %d".format(result.length))
    result
  }

  def findByVenue(venue: String): Seq[Wedding] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select * from wedding
          where wedding.venue = {venue}
        """
      ).on(
        'venue -> venue
      ).as(Wedding.simple *)
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit connection =>
      SQL("delete from wedding where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

  def create(wedding: Wedding): Option[Long] = {
    DB.withConnection { implicit connection =>

      val newWeddingId = SQL(
        """
          insert into wedding (name, date, venue, coordinator_id) values (
            {name}, {date}, {venue}, {coordinator_id}
          )
        """
      ).on(
        'name -> wedding.name,
        'date -> wedding.date,
        'venue -> wedding.venue,
        'coordinator_id -> wedding.coordinatorId
      ).executeInsert()
      
      Logger.info("[Wedding] new wedding created " + newWeddingId)
      newWeddingId

    }
  }

  def update(wedding: Wedding): Int = {
    DB.withConnection { implicit connection =>
      
      SQL(
        """
          update wedding 
          set name = {name},
          date = {date},
          venue = {venue},
          coordinator_id = {coordinator_id}
          where id = {id};
        """
      ).on(
        'name -> wedding.name,
        'date -> wedding.date,
        'venue -> wedding.venue,
        'coordinator_id -> wedding.coordinatorId,
        'id -> wedding.id.get
      ).executeUpdate()

    }
  }


  def isCoordinator(weddingId: Long, coordinator: Account): Boolean = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select count(wedding.id) = 1 from wedding
          where wedding.id = {id} and wedding.coordinator_id = {coordinator_id}
        """
      ).on(
        'id -> weddingId,
        'coordinator_id -> coordinator.id.get
      ).as(scalar[Boolean].single)
    }
  }

  def isCoordinator(wedding: Wedding, coordinator: Account): Boolean = 
    isCoordinator(wedding.id.get, coordinator)


  implicit object WeddingFormat extends Format[Wedding] {

    def writes(w: Wedding): JsValue = JsObject(Seq(
      "id" -> JsNumber(w.id.get),
      "name" -> JsString(w.name),
      "date" -> JsString(w.date.toString),
      "venue" -> JsString(w.venue),
      "coordinator_id" -> JsNumber(w.coordinatorId)
    ))

    def reads(json: JsValue): Wedding = Wedding(
      (json \ "id").asOpt[Long].map(id =>
        Id(id)
      ).getOrElse(NotAssigned),
      (json \ "name").as[String],
      (new SimpleDateFormat("dd-MM-yyyy")).parse((json \ "date").as[String]),
      (json \ "venue").as[String],
      (json \ "coordinator_id").as[Long]
    )

  }


}
