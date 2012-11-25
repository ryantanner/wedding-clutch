package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class WeddingModelSpec extends Specification {

  import models._
    
  // -- User model

  "Wedding model" should {
  
    "be retrieved by id with correct coordinatorId" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val Some(wedding1) = Wedding.findById(1, 1)
        wedding1.name must equalTo("Test Wedding 1")
        
        val Some(wedding2) = Wedding.findById(2, 1)
        wedding2.name must equalTo("Test Wedding 2")
        
        val Some(wedding3) = Wedding.findById(3, 1)
        wedding3.name must equalTo("Test Wedding 3")
       
        val Some(wedding4) = Wedding.findById(4, 1)
        wedding4.name must equalTo("Test Wedding 4")
        
        val Some(wedding5) = Wedding.findById(5, 1)
        wedding5.name must equalTo("Test Wedding 5")
        
        val Some(wedding6) = Wedding.findById(6, 1)
        wedding6.name must equalTo("Test Wedding 6")
        
        val Some(wedding7) = Wedding.findById(7, 1)
        wedding7.name must equalTo("Test Wedding 7")
        
        val Some(wedding8) = Wedding.findById(8, 2)
        wedding8.name must equalTo("Test Wedding 8")
        
        val Some(wedding9) = Wedding.findById(9, 2)
        wedding9.name must equalTo("Test Wedding 9")
        
        val Some(wedding10) = Wedding.findById(10, 2)
        wedding10.name must equalTo("Test Wedding 10")
        
        val Some(wedding11) = Wedding.findById(11, 2)
        wedding11.name must equalTo("Test Wedding 11")
        
        val Some(wedding12) = Wedding.findById(12, 2)
        wedding12.name must equalTo("Test Wedding 12")
        
        val Some(wedding13) = Wedding.findById(13, 2)
        wedding13.name must equalTo("Test Wedding 13")
        
        val Some(wedding14) = Wedding.findById(14, 2)
        wedding14.name must equalTo("Test Wedding 14")
        
        val Some(wedding15) = Wedding.findById(15, 3)
        wedding15.name must equalTo("Test Wedding 15")
        
        val Some(wedding16) = Wedding.findById(16, 3)
        wedding16.name must equalTo("Test Wedding 16")
        
        val Some(wedding17) = Wedding.findById(17, 3)
        wedding17.name must equalTo("Test Wedding 17")
        
        val Some(wedding18) = Wedding.findById(18, 3)
        wedding18.name must equalTo("Test Wedding 18")
        
        val Some(wedding19) = Wedding.findById(19, 3)
        wedding19.name must equalTo("Test Wedding 19")
        
        val Some(wedding20) = Wedding.findById(20, 3)
        wedding20.name must equalTo("Test Wedding 20")
        
        val Some(wedding21) = Wedding.findById(21, 3)
        wedding21.name must equalTo("Test Wedding 21")
        
      }
    }

    "be not retrieved by id with incorrect coordinatorId" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Wedding.findById(1, -1000) must beNone

      }
    }

    "find by coordinator ID" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val weddingsForJane = Wedding.findByCoordinatorId(1)

        weddingsForJane must haveLength(7)

        weddingsForJane map { wedding =>
          wedding.coordinatorId must equalTo(1)
        }

      }
    }

    "find nil by nonexistant coordinator ID" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Wedding.findByCoordinatorId(1000) must haveLength(0)

      }
    }

    "find by venue" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Wedding.findByVenue("Country Club") must not beEmpty

      }
    }

    "find all" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Wedding.findAll.length must equalTo(21)

      }
    }

    "delete weddings" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val totalWeddings = Wedding.findAll.length
        Wedding.delete(1)
        (totalWeddings - 1) must equalTo(Wedding.findAll.length)

      }
    }

    "create weddings" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val testDate = (new java.text.SimpleDateFormat("dd-MM-yyyy")).parse("2012-01-01")
        val newWedding = Wedding(anorm.NotAssigned, "Test Wedding", testDate, "Test Venue", 1)
        
        Wedding.create(newWedding) must beSome

      }
    }

    "verify coordinator of wedding" in {  
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val testUser = User.findAll.head

        val weddings = Wedding.findByCoordinator(testUser)

        weddings.map(Wedding.isCoordinator(_, testUser) must beTrue)

      }
    }


  }

}
