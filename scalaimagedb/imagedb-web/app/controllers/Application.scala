package controllers

import com.mongodb.casbah.Imports.MongoDBObject

import models.MetadataDAO
import play.api.mvc.Action
import play.api.mvc.Controller

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(MetadataDAO.find(MongoDBObject.empty).toSeq))
  }

}