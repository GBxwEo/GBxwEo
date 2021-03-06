package controllers

import java.io.PipedInputStream
import java.io.PipedOutputStream

import com.mongodb.casbah.Imports.MongoDBObject
import com.mongodb.casbah.Imports.ObjectId

import exception.ErrorCode
import exception.SystemException
import models.Image
import play.api.data.Form
import play.api.data.Forms.ignored
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText
import play.api.libs.iteratee.Iteratee
import play.api.mvc.Action
import play.api.mvc.BodyParser
import play.api.mvc.Controller
import play.api.mvc.MultipartFormData
import views.html

object ImageController extends Controller with ControllerServices {

  /**
   * This result directly redirect to the application home.
   */
  val Home = Redirect(routes.ImageController.list(0, 2, ""))

  /**
   * Handle default path requests, redirect to images list
   */
  def index = Action {
    Home
  }

  /**
   * Display the paginated list of images.
   *
   * @param page Current page number (starts from 0)
   * @param orderBy Column to be sorted
   * @param filter Filter applied on image names
   */
  def list(page: Int, orderBy: Int, filter: String) = Action {
    implicit request =>
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
    Image.findOneById(id).map {
      image =>
        Ok(html.images.editForm(id, imageForm(id).fill(image)))
    }.getOrElse(NotFound)
  }

  /**
   * Handle the 'edit form' submission
   *
   * @param id Id of the image to edit
   */
  def update(id: ObjectId) = Action {
    implicit request =>
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

  def myPartHandler: parse.Multipart.PartHandler[MultipartFormData.FilePart[String]] = {
    parse.Multipart.handleFilePart {
      case parse.Multipart.FileInfo(partName, filename, contentType) =>

        //Set up the PipedOutputStream here, give the input stream to a uploader thread
        val pos = new PipedOutputStream()
        val pis = new PipedInputStream(pos)
        val uploader = new ImageUploader(imageBinaryComponent, pis)

        //Do the upload job
        uploader.start

        //Read content to the POS
        Iteratee.fold[Array[Byte], PipedOutputStream](pos) {
          (os, data) =>
            os.write(data)
            os
        }.mapDone {
          os =>
            try {
              os.close()
            } catch {
              case ex => SystemException.wrap(ErrorCode.TECHNICAL_ERROR, ex)
            }

            // Retrieve the image binary id from the uploader
            val result = uploader !! ImageUploader.ImageBinaryIdMsg
            result() match {
              case imageBinaryId: String => imageBinaryId
            }
        }
    }
  }

  val myBodyParser = BodyParser {
    request =>
      parse.multipartFormData(myPartHandler).apply(request)
  }

  /**
   * Handle the 'new image form' submission.
   */
  def save = Action(myBodyParser) {
    implicit request =>
      imageForm().bindFromRequest.fold(
        formWithErrors => BadRequest(html.images.createForm(formWithErrors)),
        image => {
          request.body.file("binary").map {
            binary =>
              Image.insert(image.copy(binaryId = Some(binary.ref)))
              Home.flashing("success" -> "Image %s has been created".format(binary.ref))
          }.getOrElse {
            Home.flashing("error" -> "Image %s has not been created".format(image.name))
          }
        })
  }

  /**
   * Handle image deletion.
   */
  def delete(id: ObjectId) = Action {
    Image.remove(MongoDBObject("_id" -> id))
    Home.flashing("success" -> "Image has been deleted")
  }

  /**
   * Describe the image form (used in both edit and create screens).
   */
  def imageForm(id: ObjectId = new ObjectId) = Form(
    mapping(
      "id" -> ignored(id),
      "binaryId" -> ignored(Option(null.asInstanceOf[String])),
      "name" -> nonEmptyText)(Image.apply)(Image.unapply))

}
