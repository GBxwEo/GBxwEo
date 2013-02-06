package persistence

import java.io.InputStream
import javax.jcr.Node

trait BinaryManagerComponent {

  def binaryManager: BinaryManager

  trait BinaryManager {
    def saveBinary(input: InputStream): String
  }
}