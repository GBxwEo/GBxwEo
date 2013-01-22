package controllers

import java.io.File
import java.io.FileOutputStream
import java.io.PipedInputStream
import scala.actors.threadpool.Callable
import error.ErrorCode
import error.SystemException
import scala.actors.Actor

class UploadFileWorker(val path: String, val pis: PipedInputStream) extends Actor {

  def act() = {

    val file = new File(path)
    if (!file.exists()) {
      file.createNewFile();
    }

    val fos = new FileOutputStream(file, false)
    try {
      Iterator
        .continually(pis.read)
        .takeWhile(-1 !=)
        .foreach(fos.write)
      fos.flush()

    } finally {
      try { fos.close() } catch { case ex => SystemException.wrap(ErrorCode.TECHNICAL_ERROR, ex) }
      try { pis.close() } catch { case ex => SystemException.wrap(ErrorCode.TECHNICAL_ERROR, ex) }
    }
  }

}