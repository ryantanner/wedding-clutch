import play.api._

import models.auth._
import anorm._

object Global extends GlobalSettings {

  override def onStart(app: Application) {

    Seq(
      Account(Id(1), "jane.smith@gmail.com", "password", "Jane Smith", Administrator),
      Account(Id(2), "mary.kate@gmail.com", "password", "Mary Kate", NormalUser),
      Account(Id(3), "ellen.joy@gmail.com", "password", "Ellen Joy", NormalUser),
      Account(Id(4), "cassie.constanzo@gmail.com", "password", "Cassie Costanzo", NormalUser)
    ) foreach Account.update

  }

}

