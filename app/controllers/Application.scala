package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import views._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )

  /**
   * Login page.
   */
  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => Redirect(routes.Weddings.index(0)).withSession("email" -> user._1)
    )
  }
  
}

trait Secured {

  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("email")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)
  
  // --
  
  /** 
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }

  /**
   * Check if the connected user is a owner of this task.
   */
  def IsCoordinatorOf(weddingId: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { userEmail => request =>
    User.findByEmail(userEmail).map { user =>
      Wedding.findById(weddingId, user).map { wedding =>
        if(Wedding.isCoordinator(wedding, user)) {
          f(userEmail)(request)
        } else {
          Results.Forbidden
        }
      }.getOrElse(Results.Forbidden)
    }.getOrElse(Results.Forbidden)
  }

  /**
   * Check if user is an admin
   */
  def IsAdmin(id: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user => request =>
    if(User.isAdmin(id)) {
      f(user)(request)
    } else {
      Results.Forbidden
    }
  }

}