package models

import play.api.db._
import play.api.Play.current

import play.api.libs.json._

import anorm._
import anorm.SqlParser._

case class User(id: Pk[Long] = NotAssigned, email: String, name: String, password: String)

object User {

  val simple = {
    get[Pk[Long]]("user.id") ~
    get[String]("user.email") ~
    get[String]("user.name") ~
    get[String]("user.password") map {
      case id~email~name~password => User(id, email, name, password)
    }
  }

  def findById(id: Long): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user where id = {id}").on(
        'id -> id 
      ).as(User.simple.singleOpt)
    }
  }


  def findByEmail(email: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user where email = {email}").on(
        'email -> email
      ).as(User.simple.singleOpt)
    }
  }

  def findAll: Seq[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user").as(User.simple *)
    }
  }

  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select * from user where
          email = {email} and password = {password}
        """
      ).on(
        'email -> email,
        'password -> password
      ).as(User.simple.singleOpt)
    }
  }

  def create(user: User): Option[Long] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into user (email, name, password) values (
            {email}, {name}, {password}
          )
        """
      ).on(
        'email -> user.email,
        'name -> user.name,
        'password -> user.password
      ).executeInsert()
    }
  }

  def isAdmin(id: Long): Boolean = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select count(user_admins.user_id) = 1 from user_admins
          where user_admins.user_id = {id}
        """
      ).on(
        'id -> id
      ).as(scalar[Boolean].single)
    }
  }

  implicit object UserFormat extends Format[User] {

    def writes(u: User): JsValue = {
      JsObject(Seq(
        "id" -> JsNumber(u.id.get),
        "name" -> JsString(u.name),
        "email" -> JsString(u.email)
      ))
    }

    def reads(json: JsValue): User = User(
      (json \ "id").asOpt[Long].map(id =>
        Id(id)
      ).getOrElse(NotAssigned),
      (json \ "name").as[String],
      (json \ "email").as[String],
      (json \ "password").as[String]
    )

  }
  
}
