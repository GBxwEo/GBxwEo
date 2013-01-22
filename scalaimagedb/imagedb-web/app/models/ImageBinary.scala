package models

import java.io.InputStream
import java.util.Calendar

import scala.compat.Platform

import org.apache.jackrabbit.core.TransientRepository

import javax.jcr.Node
import javax.jcr.Session
import javax.jcr.SimpleCredentials

object ImageBinary {

  val repository = new TransientRepository

  def getFolderNode: Node = {

    val session = repository.login(new SimpleCredentials("userid", "password".toCharArray))
    try {
      val rootNode = session.getRootNode
      if (!rootNode.hasNode("imagebinary")) {
        val node = rootNode.addNode("imagebinary", "nt:folder")
        session.save()
        node
      } else {
        rootNode.getNode("imagebinary")
      }
    } finally {
      session.logout
    }
  }

  def addImage(imageIs: InputStream, imageId: String): Node = {

    val folderNode = getFolderNode
    
    // create the file node
    val fileNode = folderNode.addNode(imageId, "nt:file");

    // create the mandatory child node - jcr:content
    val resNode = fileNode.addNode("jcr:content", "nt:resource");
    val lastModified: Calendar = Calendar.getInstance();
    lastModified.setTimeInMillis(Platform.currentTime);
    resNode.setProperty("jcr:lastModified", lastModified);
    resNode.setProperty("jcr:mimeType", "image/jpeg");
    resNode.setProperty("jcr:data", imageIs);

    fileNode;
  }

}
