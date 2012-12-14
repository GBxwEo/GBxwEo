package models

import com.mongodb.casbah.Imports.MongoConnection
import com.mongodb.casbah.Imports.ObjectId
import com.novus.salat.dao.SalatDAO
import com.novus.salat.global.ctx

object MetadataDAO extends SalatDAO[Metadata, ObjectId](collection = MongoConnection()("image_metadata")("metadata"))