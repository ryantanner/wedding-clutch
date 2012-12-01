package test

import org.specs2.mutable._

import play.api.mvc.Session
import play.api.Logger
import play.api.test._
import play.api.test.Helpers._

import play.api.mvc.AnyContentAsJson
import play.api.libs.json.Json.parse

class AccountRouteSpec extends Specification with SessionHelper {

  val adminEmail = ("email" -> "jane.smith@gmail.com")
  val adminPass = ("password" -> "password")

  val badEmail = ("email" -> "totallywrong@gmail.com")
  val badPass = ("email" -> "totallywrong")

  val normalEmail = ("email" -> "mary.kate@gmail.com")
  val normalPass = ("password" -> "password")

  "Router" should {

    "get index" in {
      val Some(result) = routeAndCall(FakeRequest(GET, "/"))

      status(result) must equalTo(OK)
    }

    "get login page" in {
      val Some(result) = routeAndCall(FakeRequest(GET, "/login"))

      status(result) must equalTo(OK)
    }

    "refuse bad password" in {
      running(TestServer(3333)) {
        val Some(result) = routeAndCall(FakeRequest(POST, "/login")
                           .withFormUrlEncodedBody(adminEmail, badPass))

        status(result) must equalTo(BAD_REQUEST)
      }
    }

    "refuse bad email/pass" in {
      running(TestServer(3333)) {
        val Some(result) = routeAndCall(FakeRequest(POST, "/login")
                           .withFormUrlEncodedBody(badEmail, badPass))

        status(result) must equalTo(BAD_REQUEST)

      }
    }

    "post admin login" in {
      running(TestServer(3333)) {
        val Some(result) = routeAndCall(FakeRequest(POST, "/login")
                           .withFormUrlEncodedBody(adminEmail, adminPass))

        status(result) must equalTo(SEE_OTHER)
      }
    }

    "get all users if admin" in {
      running(TestServer(3333)) { 
        val Some(login) = routeAndCall(FakeRequest(POST, "/login") 
            .withFormUrlEncodedBody(adminEmail, adminPass)) 

        val adminSession = session(login)

        val Some(result) = routeAndCall(FakeRequest(GET, "/api/users")
                            .withSession(adminSession:_*))

        status(result) must equalTo(OK)
      }
    }

    "get forbidden is getting all users not as admin" in {
      running(TestServer(3333)) {
        val Some(login) = routeAndCall(FakeRequest(POST, "/login")
                          .withFormUrlEncodedBody(normalEmail, normalPass))

        val normalSession = session(login)

        val Some(result) = routeAndCall(FakeRequest(GET, "/api/users")
                           .withSession(normalSession:_*))

        status(result) must equalTo(FORBIDDEN)
      }
    }


  }

}

trait SessionHelper {

  implicit def sessionToData(session: Session): List[(String,String)] = {
    session.data.map { case (k,v) => (k,v) }(collection.breakOut)
  }


}


