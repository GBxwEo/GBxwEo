package persistence

import org.specs2.mutable

class JCRRepositoryManagerSuite extends mutable.Specification {

  "JCRRepositoryManager.getSession" should {
    "not be null" in {
      JCRRepositoryManager.getSession must not beNull
    }
  }

}
