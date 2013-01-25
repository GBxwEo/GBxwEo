package persistence

import java.io.InputStream
import javax.jcr.Node

trait ImageBinaryComponent {

  def imageBinaryManager: ImageBinaryManager

  trait ImageBinaryManager {
    def saveImageBinary(input: InputStream, imageId: String)
  }
}