import play.api._
import play.api.mvc._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

  override def onError(request: RequestHeader, ex: Throwable) = {
    Logger.error("Error: " + ex.getMessage, ex)
    super.onError(request, ex)
  }

}
