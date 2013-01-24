package controllers

import java.io.PipedInputStream
import java.util.UUID

import scala.actors.Actor

import models.ImageBinary

class UploadFileWorker(val path: String, val pis: PipedInputStream) extends Actor {

  def act() = {
    ImageBinary.addImage(pis, UUID.randomUUID().toString())
  }

  //  def act() = {
  //
  //    val file = new File(path)
  //    if (!file.exists()) {
  //      file.createNewFile();
  //    }
  //
  //    val fos = new FileOutputStream(file, false)
  //    try {
  //      Iterator
  //        .continually(pis.read)
  //        .takeWhile(-1 !=)
  //        .foreach(fos.write)
  //      fos.flush()
  //
  //    } finally {
  //      try { fos.close() } catch { case ex => SystemException.wrap(ErrorCode.TECHNICAL_ERROR, ex) }
  //      try { pis.close() } catch { case ex => SystemException.wrap(ErrorCode.TECHNICAL_ERROR, ex) }
  //    }
  //  }

}