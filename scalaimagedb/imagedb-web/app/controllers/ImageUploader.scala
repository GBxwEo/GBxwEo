package controllers

import java.io.PipedInputStream

import scala.actors.Actor

import persistence.ImageBinaryComponent

/**
 * This class is responsible for uploading image binary.
 *
 */
class ImageUploader(val imageBinaryComponent: ImageBinaryComponent, val input: PipedInputStream) extends Actor {

  def act() = {
    val imageBinaryId = imageBinaryComponent.imageBinaryManager.saveImageBinary(input)

    react {
      case ImageUploader.ImageBinaryIdMsg => reply(imageBinaryId)
    }
  }
}

object ImageUploader {

  //Message that can be sent to the uploader in order to receive the image binary id
  object ImageBinaryIdMsg

}