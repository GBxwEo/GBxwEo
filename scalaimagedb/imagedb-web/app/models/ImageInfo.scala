package models

import java.util.{ Date }
import play.api.Play.current
import com.novus.salat._
import com.novus.salat.dao._
import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import com.novus.salat.global._

case class Image(
  @Key("_id") id: ObjectId = new ObjectId,
  name: String)

object Image extends ModelCompanion[Image, ObjectId] {

  val collection = mongoCollection("images")

  val dao = new SalatDAO[Image, ObjectId](collection = collection) {}

  val columns = List("dummy", "_id", "name", "introduced", "discontinued", "company_id")

  def list(page: Int = 0, pageSize: Int = 6, orderBy: Int = 1, filter: String = ""): Page[Image] = {

    val where = if (filter == "") MongoDBObject.empty else MongoDBObject("name" -> (""".*""" + filter + """.*""").r)
    val ascDesc = if (orderBy > 0) 1 else -1
    val order = MongoDBObject(columns(orderBy.abs) -> ascDesc)

    val totalRows = count(where);
    val offset = pageSize * page
    val images = find(where).sort(order).limit(pageSize).skip(offset).toSeq

    Page(images, page, offset, totalRows)
  }
}

/**
 * Helper for pagination.
 */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

