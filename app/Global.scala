import play.api._

import models.auth._
import anorm._

object Global extends GlobalSettings {

  override def onStart(app: Application) {

    Account.findAll.foreach { account =>
      Account.update(account)
    }

  }

}

