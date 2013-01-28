package controllers

import java.io.PipedInputStream
import java.util.UUID
import scala.actors.Actor
import persistence.ImageBinaryComponent
import play.api.mvc.BodyParser
import play.api.mvc.BodyParsers._
import play.api.mvc.MultipartFormData
import java.io.PipedOutputStream
import play.api.libs.iteratee.Iteratee
import exception.SystemException
import exception.ErrorCode

/**
 * This class is responsible for uploading image binary.
 *
 */
class ImageBinaryUploader(val imageBinaryComponent: ImageBinaryComponent, val input: PipedInputStream, val imageId: String) extends Actor {

  def act() = {
    imageBinaryComponent.imageBinaryManager.saveImageBinary(input, imageId)
  }

}
