package controllers

import com.mongodb.casbah.Imports.MongoDBObject
import play.api._
import play.api.data.Forms._
import play.api.mvc._
import persistence.jcr.JCRImageBinaryComponent
import com.typesafe.config.ConfigFactory
import persistence.ImageBinaryComponent
import persistence.jcr.JCRRepositoryComponent
import utils.Settings

object Application extends Controller {

  def index = Action { ImageController.Home }

}

trait ControllerServices {

  val config = ConfigFactory.load("env-prod")

  val imageBinaryComponent: ImageBinaryComponent = new JCRImageBinaryComponent with JCRRepositoryComponent {
    def serverUri = config.getString(Settings.JCR_REPOSITORY_URI)
    def username = config.getString(Settings.JCR_REPOSITORY_USERNAME)
    def password = config.getString(Settings.JCR_REPOSITORY_PASSWORD)
  }

}