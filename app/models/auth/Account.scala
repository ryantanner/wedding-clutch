package models.auth

import play.api.db._
import play.api.libs.json._
import anorm._
import anorm.SqlParser._
import play.api.Play.current
import java.sql.Clob
//import org.mindrot.jbcrypt.BCrypt

case class Account(id: Pk[Long], email: String, password: String, name: String, permission: Permission)

object Account {

  object Clob {
    def unapply(clob: Clob): Option[String] = Some(clob.getSubString(1, clob.length.toInt))
  }

  implicit val rowToPermission: Column[Permission] = {
    Column.nonNull[Permission] { (value, meta) =>
      value match {
        case Clob("Administrator") => Right(Administrator)
        case Clob("NormalUser") => Right(NormalUser)
        case _ => Left(TypeDoesNotMatch(
          "Cannot convert %s : %s to Permission for column %s".format(value, value.getClass, meta.column)))
      }
    }
  }

  implicit def permissionToString(permission: Permission): String = {
    permission match {
      case Administrator => "administrator"
      case NormalUser => "normal_user"
    }
  }
  
  implicit def stringToPermission(permission: String): Permission = {
    permission match {
      case "administrator" => Administrator
      case "normal_user" => NormalUser
    }
  }

  val simple = {
    get[Pk[Long]]("account.id") ~
    get[String]("account.email") ~
    get[String]("account.password") ~
    get[String]("account.name") ~
    get[Permission]("account.permission") map {
      case id~email~pass~name~perm => Account(id, email, pass, name, perm)
    }
  }

  def authenticate(email: String, password: String): Option[Account] = {
//    findByEmail(email).filter { account => BCrypt.checkpw(password, account.password) }
    findByEmail(email).filter { account => password == account.password }
  }

  def findByEmail(email: String): Option[Account] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT * FROM account WHERE email = {email}").on(
        'email -> email
      ).as(simple.singleOpt)
    }
  }

  def findById(id: Long): Option[Account] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT * FROM account WHERE id = {id}").on(
        'id -> id
      ).as(simple.singleOpt)
    }
  }

  def findAll: Seq[Account] = {
    DB.withConnection { implicit connection =>
      SQL("select * from account").as(simple *)
    }
  }

  def create(account: Account) {
    DB.withConnection { implicit connection =>
      SQL("INSERT INTO account VALUES ({id}, {email}, {pass}, {name}, {permission})").on(
        'id -> account.id,
        'email -> account.email,
//        'pass -> BCrypt.hashpw(account.password, BCrypt.gensalt()),
        'pass -> account.password,
        'name -> account.name,
        'permission -> account.permission.toString
      ).executeUpdate()
    }
  }

  implicit object AccountFormat extends Format[Account] {

    def writes(a: Account): JsValue = JsObject(Seq(
      "id" -> JsNumber(a.id.get),
      "name" -> JsString(a.name),
      "email" -> JsString(a.email),
      "permission" -> JsString(a.permission)
    ))

    def reads(json: JsValue): Account = Account(
      (json \ "id").asOpt[Long].map(id =>
        Id(id)
      ).getOrElse(NotAssigned),
      (json \ "email").as[String],
      (json \ "password").as[String],
      (json \ "name").as[String],
      (json \ "permission").as[String]
    )

  }
}
