package controllers

import java.io.PipedInputStream

import scala.actors.Actor

import persistence.BinaryManagerComponent

/**
 * This class is responsible for uploading image binary.
 *
 */
class ImageUploader(val imageBinaryComponent: BinaryManagerComponent, val input: PipedInputStream) extends Actor {

  def act() = {
    val imageBinaryId = imageBinaryComponent.binaryManager.saveBinary(input)

    react {
      case ImageUploader.ImageBinaryIdMsg => reply(imageBinaryId)
    }
  }
}

object ImageUploader {

  //Message that can be sent to the uploader in order to receive the image binary id
  object ImageBinaryIdMsg

}