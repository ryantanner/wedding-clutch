package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class UserModelSpec extends Specification {

  import models._
    
  // -- User model

  "User model" should {
  
    "be retrieved by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val Some(jane) = User.findById(1)
      
        jane.name must equalTo("Jane Smith")
        jane.email must equalTo("jane.smith@gmail.com")
        jane.password must equalTo("password")
        
        val Some(mary) = User.findById(2)
      
        mary.name must equalTo("Mary Kate")
        mary.email must equalTo("mary.kate@gmail.com")
        mary.password must equalTo("password")
        
        val Some(ellen) = User.findById(3)
      
        ellen.name must equalTo("Ellen Joy")
        ellen.email must equalTo("ellen.joy@gmail.com")
        ellen.password must equalTo("password")
        
        val Some(cassie) = User.findById(4)
      
        cassie.name must equalTo("Cassie Costanzo")
        cassie.email must equalTo("cassie.constanzo@gmail.com")
        cassie.password must equalTo("password")
        
      }
    }

    "not be retrieved by id if not exist" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        User.findById(-1234) must beNone

      }
    }


    "be retrieved by email" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val Some(jane) = User.findByEmail("jane.smith@gmail.com")
      
        jane.name must equalTo("Jane Smith")
        jane.id.get must equalTo(1)
        jane.password must equalTo("password")
        
        val Some(mary) = User.findByEmail("mary.kate@gmail.com")
      
        mary.name must equalTo("Mary Kate")
        mary.id.get must equalTo(2)
        mary.password must equalTo("password")
        
        val Some(ellen) = User.findByEmail("ellen.joy@gmail.com")
      
        ellen.name must equalTo("Ellen Joy")
        ellen.id.get must equalTo(3)
        ellen.password must equalTo("password")
        
        val Some(cassie) = User.findByEmail("cassie.constanzo@gmail.com")
      
        cassie.name must equalTo("Cassie Costanzo")
        cassie.id.get must equalTo(4)
        cassie.password must equalTo("password")
        
      }    
    }

    "not be retrieved by email if not exist" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        User.findByEmail("NON EXISTANT EMAIL") must beNone
      }
    }

    "have 4 sample users" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        User.findAll must haveLength(4)
        
      }
    }

    "authenticate valid users" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val Some(jane) = User.authenticate("jane.smith@gmail.com", "password")
      
        jane.name must equalTo("Jane Smith")
        jane.id.get must equalTo(1)
        jane.password must equalTo("password")
        
        val Some(mary) = User.authenticate("mary.kate@gmail.com", "password")
      
        mary.name must equalTo("Mary Kate")
        mary.id.get must equalTo(2)
        mary.password must equalTo("password")
        
        val Some(ellen) = User.authenticate("ellen.joy@gmail.com", "password")
      
        ellen.name must equalTo("Ellen Joy")
        ellen.id.get must equalTo(3)
        ellen.password must equalTo("password")
        
        val Some(cassie) = User.authenticate("cassie.constanzo@gmail.com", "password")
      
        cassie.name must equalTo("Cassie Costanzo")
        cassie.id.get must equalTo(4)
        cassie.password must equalTo("password")

      }
    }

    "not authenticate invalid users" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        User.authenticate("INVALID EMAIL", "INVALID PASSWORD") must beNone

      }
    }

    "not authenticate invalid passwords" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        User.authenticate("jane.smith@gmail.com", "INVALID PASSWORD") must beNone

      }
    }

    "create new user" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val newUser = User(anorm.NotAssigned, "new.email@gmail.com", "New Test User", "password")

        User.create(newUser) must beSome

      }
    }

    "have Jane Smith as admin" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        // id = 1 is Jane Smith
        User.isAdmin(1) must beTrue

      }
    }

    "have no other user as admin" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        User.isAdmin(2) must beFalse
        User.isAdmin(3) must beFalse
        User.isAdmin(4) must beFalse
        User.isAdmin(5) must beFalse

      }
    }



  }
      
    
    /*
    "be listed along its companies" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val computers = Computer.list()

        computers.total must equalTo(574)
        computers.items must have length(10)

      }
    }
    
    "be updated if needed" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        Computer.update(21, Computer(name="The Macintosh", introduced=None, discontinued=None, companyId=Some(1)))
        
        val Some(macintosh) = Computer.findById(21)
        
        macintosh.name must equalTo("The Macintosh")
        macintosh.introduced must beNone
        
      }
    }
    */

}
