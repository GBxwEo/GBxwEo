package models

import java.io.InputStream
import java.util.Calendar

import scala.compat.Platform

import org.apache.jackrabbit.JcrConstants

import javax.jcr.Node
import persistence.jcr.JCRRepositoryManager

object ImageBinary {

  val repositoryManager: JCRRepositoryManager = new JCRRepositoryManager("http://localhost:8080/server")

  def getImageFolderNode: Node = {

    val session = repositoryManager.getSession
    try {
      val nodeName = "images"
      val rootNode = session.getRootNode
      if (!rootNode.hasNode(nodeName)) {
        val node = rootNode.addNode(nodeName, JcrConstants.NT_FOLDER)
        session.save()
        node
      } else {
        rootNode.getNode(nodeName)
      }
    } finally {
      session.logout
    }
  }

  def addImage(input: InputStream, imageId: String): Node = {

    val session = repositoryManager.getSession
    try {
      val folderNode = getImageFolderNode

      //Create the file node
      val fileNode = folderNode.addNode(imageId, JcrConstants.NT_FILE)

      //Create the mandatory child node - jcr:content
      val contentNode = fileNode.addNode(JcrConstants.JCR_CONTENT, JcrConstants.NT_RESOURCE)

      //Set the last modified and mime type properties
      val lastModified: Calendar = Calendar.getInstance()
      lastModified.setTimeInMillis(Platform.currentTime)
      contentNode.setProperty(JcrConstants.JCR_LASTMODIFIED, lastModified)
      contentNode.setProperty(JcrConstants.JCR_MIMETYPE, "image/jpeg")

      //Set the content data
      //      val valueFactory = session.getValueFactory();
      //       contentNode.setProperty(JcrConstants.JCR_DATA, valueFactory.createBinary(input))

      session.save()
      fileNode

    } finally {
      session.logout
    }
  }

}