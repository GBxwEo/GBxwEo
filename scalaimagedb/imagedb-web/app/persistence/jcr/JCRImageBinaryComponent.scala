package persistence.jcr

import java.io.InputStream
import java.util.Calendar
import java.util.UUID

import scala.compat.Platform

import org.apache.jackrabbit.JcrConstants

import javax.jcr.Node
import javax.jcr.ValueFactory
import persistence.ImageBinaryComponent

trait JCRImageBinaryComponent extends ImageBinaryComponent {
  this: JCRRepositoryComponent =>

  def imageBinaryManager: ImageBinaryManager = new JCRImageBinaryManager

  class JCRImageBinaryManager extends ImageBinaryManager {

    def saveImageBinary(input: InputStream): String = {

      val session = repositoryManager.getSession
      try {
        //Get the image folder node
        val rootNode = session.getRootNode
        val folderNode = getImageFolderNode(rootNode)

        //Create a file node
        val imageBinaryId = UUID.randomUUID().toString()
        val fileNode = createFileNode(folderNode, imageBinaryId)

        //Create a content node
        val valueFactory = session.getValueFactory();
        createContentNode(fileNode, valueFactory, input)

        session.save()

        imageBinaryId

      } finally {
        session.logout
      }
    }

    private def getImageFolderNode(rootNode: Node): Node = {

      val nodeName = "images"

      if (!rootNode.hasNode(nodeName)) {
        val node = rootNode.addNode(nodeName, JcrConstants.NT_FOLDER)
        node
      } else {
        rootNode.getNode(nodeName)
      }
    }

    private def createFileNode(imageFolderNode: Node, imageId: String): Node = {
      //Create the file node - nt:file
      val fileNode = imageFolderNode.addNode(imageId, JcrConstants.NT_FILE)
      fileNode
    }

    private def createContentNode(fileNode: Node, valueFactory: ValueFactory, input: InputStream) = {

      //Create the content node - jcr:content
      val contentNode = fileNode.addNode(JcrConstants.JCR_CONTENT, JcrConstants.NT_RESOURCE)

      //Set the last modified and mime type properties
      val lastModified: Calendar = Calendar.getInstance()
      lastModified.setTimeInMillis(Platform.currentTime)
      contentNode.setProperty(JcrConstants.JCR_LASTMODIFIED, lastModified)
      contentNode.setProperty(JcrConstants.JCR_MIMETYPE, "image/jpeg")

      //Set the content data      
      contentNode.setProperty(JcrConstants.JCR_DATA, valueFactory.createBinary(input))
    }
  }
}