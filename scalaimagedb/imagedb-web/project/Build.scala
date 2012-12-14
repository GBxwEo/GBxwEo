import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "imagedb-web"
  val appVersion = "1.0-SNAPSHOT"

  val salat = "com.novus" %% "salat" % "1.9.1"

  val appDependencies = Seq(
    salat)

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings()

}
