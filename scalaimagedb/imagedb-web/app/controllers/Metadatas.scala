package controllers

import com.mongodb.casbah.Imports.MongoDBObject

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.mvc._

import models._

object Metadatas extends Controller {

  val metadataForm: Form[Metadata] = Form(
    mapping(
      "name" -> nonEmptyText)(Metadata.apply)(Metadata.unapply))

  def form = Action {
    Ok(views.html.metadata.form(metadataForm))
  }

  def submit = Action { implicit request =>
    metadataForm.bindFromRequest.fold(
      errors => BadRequest(views.html.metadata.form(errors)),
      metadata => Ok(views.html.metadata.summary(metadata)))
  }

}