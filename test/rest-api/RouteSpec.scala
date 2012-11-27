package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import play.api.mvc.AnyContentAsJson
import play.api.libs.json.Json.parse

class RouteSpec extends Specification {

  val adminHeaders = Map("user" -> Seq("jane.smith@gmail.com"))

  val adminEmail = ("email" -> "jane.smith@gmail.com")

  val adminPass = ("password" -> "password")

  "REST API as admin" should {

    "login as admin" in {
      running(FakeApplication()) {
        val Some(result) = routeAndCall(FakeRequest(POST, "/login")
                             .withFormUrlEncodedBody(adminEmail, adminPass)
                           )

        status(result) must equalTo(SEE_OTHER)
      }
    }

    /*
    "respond to GET /users" in {
      running(FakeApplication()) {
        val Some(result) = routeAndCall(FakeRequest(GET, "/api/users").withHeaders(adminEmail))

        status(result) must equalTo(OK)
      }
    }
    */

  }

}
