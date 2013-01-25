import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "imagedb-web"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.scalaz" %% "scalaz-core" % "6.0.4",
    "com.novus" %% "salat" % "1.9.1",
    "se.radley" %% "play-plugins-salat" % "1.1",
    "javax.jcr" % "jcr" % "2.0",
    "org.apache.jackrabbit" % "jackrabbit-core" % "2.4.3",
    "org.apache.jackrabbit" % "jackrabbit-jcr2dav" % "2.4.3")

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    routesImport += "se.radley.plugin.salat.Binders._", templatesImport += "org.bson.types.ObjectId")

}
