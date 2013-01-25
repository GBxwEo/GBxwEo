package persistence.jcr

import java.io.ByteArrayInputStream
import java.util.UUID

import org.specs2.mutable

import com.typesafe.config.ConfigFactory

import persistence.ImageBinaryComponent
import utils.Settings

class JCRImageBinarySuite extends mutable.Specification {

  val testConfig = ConfigFactory.load("test")

  trait Fixture {
    val imageBinaryComponent: ImageBinaryComponent = new JCRImageBinaryComponent with JCRRepositoryComponent {
      val serverUri = testConfig.getString(Settings.JCR_REPOSITORY_URI)
    }
  }

  new Fixture {
    "ImageBinaryComponent.ImageBinaryManager.saveImageBinary" should {
      "not produce exception" in {
        {
          val imageId = UUID.randomUUID().toString()
          val input = new ByteArrayInputStream(imageId.getBytes())
          imageBinaryComponent.imageBinaryManager.saveImageBinary(input, imageId)
        } must not(throwA[Throwable])
      }
    }
  }

}
