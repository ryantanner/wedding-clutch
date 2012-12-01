package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import play.api.mvc.AnyContentAsJson
import play.api.libs.json.Json.parse

class RouteSpec extends Specification {

  val adminEmail = ("email" -> "jane.smith@gmail.com")
  val adminPass = ("password" -> "password")

  val badEmail = ("email" -> "totallywrong@gmail.com")
  val badPass = ("email" -> "totallywrong")
  
  "Router" should {

    "get index" in {
      val Some(result) = routeAndCall(FakeRequest(GET, "/"))

      status(result) must equalTo(OK)
    }

    "get login page" in {
      val Some(result) = routeAndCall(FakeRequest(GET, "/login"))

      status(result) must equalTo(OK)
    }

    "post admin login" in {
      running(TestServer(3333)) {
        val Some(result) = routeAndCall(FakeRequest(POST, "/login")
                           .withFormUrlEncodedBody(adminEmail, adminPass))

        status(result) must equalTo(SEE_OTHER)
      }
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

  

    /*
  "REST API as admin" should {

    "login as admin" in {
        val Some(result) = routeAndCall(FakeRequest(POST, "/login")
                             .withFormUrlEncodedBody(adminEmail, adminPass)
                           )

        status(result) must equalTo(SEE_OTHER)
      }
    }

    "respond to GET /users" in {
      running(FakeApplication()) {
        val Some(result) = routeAndCall(FakeRequest(GET, "/api/users").withHeaders(adminEmail))

        status(result) must equalTo(OK)
      }
    }
    */

  }

}
