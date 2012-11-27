package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class EventModelSpec extends Specification {

  import models._
    
  // -- User model

  "Event model" should {

    "be retrieved by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val Some(music) = Event.findById(1, 1)
        music.name must equalTo("Start the Music")

        val Some(food) = Event.findById(2, 1)
        food.name must equalTo("Plate the Food")

        val Some(smth) = Event.findById(3, 1)
        smth.name must equalTo("Do something else")


      }
    }

    "retrieve all for coodinator id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Event.findByCoordinatorId(1) must haveLength(4)

      }
    }

    "retrieve nil for invalid coordinator id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Event.findByCoordinatorId(10000) must haveLength(0)

      }
    }

    
    "not be retrieved by invalid id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Event.findById(10000, 1) must beNone

      }
    }

    "be retrieved by event" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Event.findVendors(1) must haveLength(2)

      }
    }


  }
  

}
