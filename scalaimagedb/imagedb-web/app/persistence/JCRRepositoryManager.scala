package persistence

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

object JCRRepositoryManager {

  val url = "http://localhost:8080/server"

  lazy val repository = getRepository(url)

  private def getRepository(serverUrl: String): Repository = {

    val idFactory = IdFactoryImpl.getInstance()
    val nameFactory = NameFactoryImpl.getInstance()
    val pathFactory = PathFactoryImpl.getInstance()
    val qvalueFactory = QValueFactoryImpl.getInstance()
    val repoService = new RepositoryServiceImpl(url, idFactory, nameFactory, pathFactory, qvalueFactory)
    val config = getConfig(repoService);

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
        return 200;
      }

      def getCacheBehaviour: CacheBehaviour = {
        CacheBehaviour.INVALIDATE
      }

      def getPollTimeout(): Int = {
        200
      }
    }
  }

  def getSession: Session = {
    repository.login()
  }

}