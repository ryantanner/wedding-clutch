package models

import play.api.db._
import play.api.Play.current

import play.api.libs.json.Json
import play.api.libs.json._

import anorm._
import anorm.SqlParser._

import java.util.Date

case class Wedding(id: Pk[Long], name: String, date: Date, venue: String, owner: String)

object Wedding {

  val simple = {
    get[Pk[Long]]("wedding.id") ~
    get[String]("wedding.name") ~
    get[Date]("wedding.date") ~
    get[String]("wedding.venue") ~
    get[String]("wedding.owner") map {
      case id~name~date~venue~owner => Wedding(
        id, name, date, venue, owner
      )
    }
  }

  def findById(id: Long): Option[Wedding] = {
    DB.withConnection { implicit connection =>
      SQL("select * from wedding where id = {id}").on(
        'id -> id
      ).as(Wedding.simple.singleOpt)
    }
  }

  // def findWeddingInvolving
  // allow searching by vendor

  def findByOwner(owner: String): Seq[Wedding] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select * from wedding
          where wedding.owner = {owner}
        """
      ).on(
        'owner -> owner 
      ).as(Wedding.simple *)
    }
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

  def create(wedding: Wedding): Wedding = {
    DB.withConnection { implicit connection =>

      // Get the wedding id
      val id: Long = wedding.id.getOrElse {
        SQL("select next value for wedding_seq").as(scalar[Long].single)
      }

      SQL(
        """
          insert into wedding values (
            {id}, {name}, {date}, {venue}
          )
        """
      ).on(
        'id -> wedding.id,
        'name -> wedding.name,
        'date -> wedding.date,
        'venue -> wedding.venue
      ).executeUpdate()

      wedding.copy(id = Id(id))

    }
  }

  def isOwner(wedding: Long, owner: String): Boolean = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select count(wedding.id) = 1 from wedding
          where wedding.id = {id} and wedding.owner = {owner}
        """
      ).on(
        'id -> wedding,
        'owner -> owner
      ).as(scalar[Boolean].single)
    }
  }

  def toJson(wedding: Wedding): JsValue = {
    Json.toJson(
      Map(
        "id" -> Json.toJson(wedding.id.get),
        "name" -> Json.toJson(wedding.name),
        "owner" -> Json.toJson(wedding.owner),
        "date" -> Json.toJson(wedding.date.toString),
        "venue" -> Json.toJson(wedding.venue)
      )
    )
  }


}
