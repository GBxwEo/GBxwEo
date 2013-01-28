package persistence.jcr

import org.apache.jackrabbit.jcr2spi.RepositoryImpl
import org.apache.jackrabbit.jcr2spi.config.CacheBehaviour
import org.apache.jackrabbit.jcr2spi.config.RepositoryConfig
import org.apache.jackrabbit.spi.RepositoryService
import org.apache.jackrabbit.spi.commons.identifier.IdFactoryImpl
import org.apache.jackrabbit.spi.commons.name.NameFactoryImpl
import org.apache.jackrabbit.spi.commons.name.PathFactoryImpl
import org.apache.jackrabbit.spi.commons.value.QValueFactoryImpl
import org.apache.jackrabbit.spi2dav.RepositoryServiceImpl
import javax.jcr.Repository
import javax.jcr.Session
import javax.jcr.SimpleCredentials

trait JCRRepositoryComponent {

  def serverUri: String

  def username: String

  def password: String

  val repositoryManager = new JCRRepositoryManager(serverUri, username, password)
}

class JCRRepositoryManager(serverUri: String, username: String, password: String) {

  def getSession: Session = {
    val creds = new SimpleCredentials(username, password.toCharArray())
    repository.login(creds, "default")
  }

  private val repository = getRepository

  private def getRepository: Repository = {

    val repositoryService = new RepositoryServiceImpl(serverUri,
      IdFactoryImpl.getInstance(), NameFactoryImpl.getInstance(),
      PathFactoryImpl.getInstance(), QValueFactoryImpl.getInstance())
    val config = getConfig(repositoryService);
    RepositoryImpl.create(config);
  }

  private def getConfig(repoService: RepositoryService) = {
    new RepositoryConfig() {

      def getRepositoryService: RepositoryService = {
        repoService
      }

      def getDefaultWorkspaceName: String = {
        "default"
      }

      def getItemCacheSize: Int = {
        return 5000;
      }

      def getCacheBehaviour: CacheBehaviour = {
        CacheBehaviour.INVALIDATE
      }

      def getPollTimeout(): Int = {
        1000
      }
    }
  }
}

