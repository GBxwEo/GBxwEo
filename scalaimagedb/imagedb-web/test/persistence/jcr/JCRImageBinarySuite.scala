package persistence.jcr

import java.io.ByteArrayInputStream
import java.util.UUID

import org.specs2.mutable

import com.typesafe.config.ConfigFactory

import persistence.BinaryManagerComponent
import utils.Settings

class JCRImageBinarySuite extends mutable.Specification {

  val config = ConfigFactory.load("env-test")

  trait Fixture {
    val imageBinaryComponent: BinaryManagerComponent = new JCRBinaryManagerComponent with JCRRepositoryComponent {
      def serverUri = config.getString(Settings.JCR_REPOSITORY_URI)
      def username = config.getString(Settings.JCR_REPOSITORY_USERNAME)
      def password = config.getString(Settings.JCR_REPOSITORY_PASSWORD)
    }
    val imageBinaryManager = imageBinaryComponent.binaryManager
  }

  new Fixture {
    "JCRBinaryManager.saveBinary" should {
      "not produce exception" in {
        {
          val imageId = UUID.randomUUID().toString()
          val input = new ByteArrayInputStream(imageId.getBytes())
          val imageBinaryId = imageBinaryManager.saveBinary(input)
        } must not(throwA[Throwable])
      }
    }
  }

}
