package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class AccountModelSpec extends Specification {

  import models.auth._
    
  // -- Account model

  "Account model" should {
  
    "be retrieved by id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val Some(jane) = Account.findById(1)
      
        jane.name must equalTo("Jane Smith")
        jane.email must equalTo("jane.smith@gmail.com")
        
        val Some(mary) = Account.findById(2)
      
        mary.name must equalTo("Mary Kate")
        mary.email must equalTo("mary.kate@gmail.com")
        
        val Some(ellen) = Account.findById(3)
      
        ellen.name must equalTo("Ellen Joy")
        ellen.email must equalTo("ellen.joy@gmail.com")
        
        val Some(cassie) = Account.findById(4)
      
        cassie.name must equalTo("Cassie Costanzo")
        cassie.email must equalTo("cassie.constanzo@gmail.com")
        
      }
    }

    "not be retrieved by id if not exist" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Account.findById(-1234) must beNone

      }
    }


    "be retrieved by email" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        val Some(jane) = Account.findByEmail("jane.smith@gmail.com")
      
        jane.name must equalTo("Jane Smith")
        jane.id.get must equalTo(1)
        val Some(mary) = Account.findByEmail("mary.kate@gmail.com")
      
        mary.name must equalTo("Mary Kate")
        mary.id.get must equalTo(2)
        val Some(ellen) = Account.findByEmail("ellen.joy@gmail.com")
      
        ellen.name must equalTo("Ellen Joy")
        ellen.id.get must equalTo(3)
        val Some(cassie) = Account.findByEmail("cassie.constanzo@gmail.com")
      
        cassie.name must equalTo("Cassie Costanzo")
        cassie.id.get must equalTo(4)
        
      }    
    }

    "not be retrieved by email if not exist" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Account.findByEmail("NON EXISTANT EMAIL") must beNone
      }
    }

    "have 4 sample users" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Account.findAll must haveLength(4)
        
      }
    }

    "authenticate valid users" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val Some(jane) = Account.authenticate("jane.smith@gmail.com", "password")
      
        jane.name must equalTo("Jane Smith")
        jane.id.get must equalTo(1)
        
        val Some(mary) = Account.authenticate("mary.kate@gmail.com", "password")
      
        mary.name must equalTo("Mary Kate")
        mary.id.get must equalTo(2)
        
        val Some(ellen) = Account.authenticate("ellen.joy@gmail.com", "password")
      
        ellen.name must equalTo("Ellen Joy")
        ellen.id.get must equalTo(3)
        
        val Some(cassie) = Account.authenticate("cassie.constanzo@gmail.com", "password")
      
        cassie.name must equalTo("Cassie Costanzo")
        cassie.id.get must equalTo(4)

      }
    }

    "not authenticate invalid users" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Account.authenticate("INVALID EMAIL", "INVALID PASSWORD") must beNone

      }
    }

    "not authenticate invalid passwords" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Account.authenticate("jane.smith@gmail.com", "INVALID PASSWORD") must beNone

      }
    }

    "create new user" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val newAccount = Account(anorm.NotAssigned, "new.email@gmail.com", "New Test Account", "password", NormalUser)

        Account.create(newAccount) must beSome

      }
    }

    "have Jane Smith as admin" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        // id = 1 is Jane Smith
        Account.isAdmin(1) must beTrue

      }
    }

    "have no other user as admin" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        Account.isAdmin(2) must beFalse
        Account.isAdmin(3) must beFalse
        Account.isAdmin(4) must beFalse
        Account.isAdmin(5) must beFalse

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
