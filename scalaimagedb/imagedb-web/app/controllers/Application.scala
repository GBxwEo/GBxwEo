package controllers

import com.mongodb.casbah.Imports.MongoDBObject

import play.api._
import play.api.data.Forms._
import play.api.mvc._

object Application extends Controller {

  def index = Action { Images.Home }

}