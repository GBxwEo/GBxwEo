import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "imagedb-web"
  val appVersion = "1.0-SNAPSHOT"

  val salat = "com.novus" %% "salat" % "1.9.1"
  val play_salat = "se.radley" %% "play-plugins-salat" % "1.1"

  val appDependencies = Seq(
    salat, play_salat)

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    routesImport += "se.radley.plugin.salat.Binders._", templatesImport += "org.bson.types.ObjectId")

}
