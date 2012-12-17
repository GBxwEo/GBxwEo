package controllers

import com.mongodb.casbah.Imports.MongoDBObject
import com.mongodb.casbah.Imports.ObjectId

import models._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formatter
import play.api.data.validation.Constraints._
import play.api.mvc._
import views._

import se.radley.plugin.salat.Formats._

object Images extends Controller {

  /**
   * This result directly redirect to the application home.
   */
  val Home = Redirect(routes.Images.list(0, 2, ""))

  /**
   * Describe the image form (used in both edit and create screens).
   */
  def imageForm(id: ObjectId = new ObjectId) = Form(
    mapping(
      "id" -> ignored(id),
      "name" -> nonEmptyText)(Image.apply)(Image.unapply))

  /**
   * Handle default path requests, redirect to images list
   */
  def index = Action { Home }

  /**
   * Display the paginated list of images.
   *
   * @param page Current page number (starts from 0)
   * @param orderBy Column to be sorted
   * @param filter Filter applied on image names
   */
  def list(page: Int, orderBy: Int, filter: String) = Action { implicit request =>
    Ok(html.images.list(
      Image.list(page = page, orderBy = orderBy, filter = filter),
      orderBy, filter))
  }

  /**
   * Display the 'edit form' of a existing Image.
   *
   * @param id Id of the image to edit
   */
  def edit(id: ObjectId) = Action {
    Image.findOneById(id).map { image =>
      Ok(html.images.editForm(id, imageForm(id).fill(image)))
    }.getOrElse(NotFound)
  }

  /**
   * Handle the 'edit form' submission
   *
   * @param id Id of the image to edit
   */
  def update(id: ObjectId) = Action { implicit request =>
    imageForm(id).bindFromRequest.fold(
      formWithErrors => BadRequest(html.images.editForm(id, formWithErrors)),
      image => {
        Image.save(image.copy(id = id))
        Home.flashing("success" -> "Image %s has been updated".format(image.name))
      })
  }

  /**
   * Display the 'new image form'.
   */
  def create = Action {
    Ok(html.images.createForm(imageForm()))
  }

  /**
   * Handle the 'new image form' submission.
   */
  def save = Action { implicit request =>
    imageForm().bindFromRequest.fold(
      formWithErrors => BadRequest(html.images.createForm(formWithErrors)),
      image => {
        Image.insert(image)
        Home.flashing("success" -> "Image %s has been created".format(image.name))
      })
  }

  /**
   * Handle image deletion.
   */
  def delete(id: ObjectId) = Action {
    Image.remove(MongoDBObject("_id" -> id))
    Home.flashing("success" -> "Image has been deleted")
  }

}