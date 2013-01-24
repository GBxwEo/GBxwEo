import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "imagedb-web"
  val appVersion = "1.0-SNAPSHOT"

  val salat = "com.novus" %% "salat" % "1.9.1"
  val play_salat = "se.radley" %% "play-plugins-salat" % "1.1"

  val jcr = "javax.jcr" % "jcr" % "2.0"
  val jackrabbit = "org.apache.jackrabbit" % "jackrabbit-core" % "2.4.3"
  val jcr2dav = "org.apache.jackrabbit" % "jackrabbit-jcr2dav" % "2.4.3"

  val appDependencies = Seq(
    salat, play_salat, jackrabbit, jcr, jcr2dav)

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    routesImport += "se.radley.plugin.salat.Binders._", templatesImport += "org.bson.types.ObjectId")

}
