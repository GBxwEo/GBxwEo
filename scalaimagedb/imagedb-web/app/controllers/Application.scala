package controllers

import com.typesafe.config.ConfigFactory
import play.api.mvc.{Controller, Action}
import persistence.BinaryManagerComponent
import persistence.jcr.{JCRRepositoryComponent, JCRBinaryManagerComponent}
import utils.Settings


object Application extends Controller {

  def index = Action {
    ImageController.Home
  }

}

trait ControllerServices {

  val config = ConfigFactory.load("env-prod")

  val imageBinaryComponent: BinaryManagerComponent = new JCRBinaryManagerComponent with JCRRepositoryComponent {

    def serverUri = config.getString(Settings.JCR_REPOSITORY_URI)

    def username = config.getString(Settings.JCR_REPOSITORY_USERNAME)

    def password = config.getString(Settings.JCR_REPOSITORY_PASSWORD)

  }

}