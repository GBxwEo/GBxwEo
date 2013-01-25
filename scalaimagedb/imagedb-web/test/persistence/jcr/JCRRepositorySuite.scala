package persistence.jcr

import org.specs2._

import com.typesafe.config.ConfigFactory

import utils.Settings

class JCRRepositorySuite extends mutable.Specification {

  val config = ConfigFactory.load("test")

  trait Fixture {
    val repo = new JCRRepositoryManager(config.getString(Settings.JCR_REPOSITORY_URI))
  }

  new Fixture {
    "JCRRepositoryManager.getSession" should {
      "not be null" in {
        repo.getSession must not beNull
      }
    }
  }

}
