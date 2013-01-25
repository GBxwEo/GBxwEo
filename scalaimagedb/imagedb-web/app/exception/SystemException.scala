package exception

import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import play.api.Logger

case class ErrorCode(number: Int)

object ErrorCode {
  val TECHNICAL_ERROR = ErrorCode(100)
}

case class SystemException(errorCode: ErrorCode, cause: Throwable = null) extends RuntimeException {

  lazy val properties = new HashMap[String, Object]

  def setProperty(name: String, value: String) = {
    properties.put(name, value)
    this
  }

  def getProperty(name: String) = {
    properties.get(name)
  }

}

object SystemException {

  def wrap(errorCode: ErrorCode, exception: Throwable): SystemException = {

    Logger.error("AHHHH! - " + exception.getMessage())
    
    if (exception.isInstanceOf[SystemException]) {
      exception.asInstanceOf[SystemException];
    } else {
      new SystemException(errorCode, exception);
    }
  }

}