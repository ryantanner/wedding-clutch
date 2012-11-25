package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class VendorModelSpec extends Specification {

  import models._
    
  // -- User model

  "Vendor model" should {

    "be retrieved by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val Some(betty) = Vendor.findById(1)
        betty.name must equalTo("Betty the Florist")

        val Some(sally) = Vendor.findById(2)
        sally.name must equalTo("Sally the Florist")

        val Some(jenny) = Vendor.findById(3)
        jenny.name must equalTo("Jenny the Florist")


      }
    }
    
    "not be retrieved by invalid id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Vendor.findById(10000) must beNone

      }
    }

    "be retrieved by role and coordinator" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val testUser = User.findById(1).get

        Vendor.findByRole("Florist", testUser) must haveLength(2)

      }
    }

    "not be retrieved by nonexistant role" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val testUser = User.findById(1).get

        Vendor.findByRole("aslfsd", testUser) must haveLength(0)

      }
    }


  }
  

}
