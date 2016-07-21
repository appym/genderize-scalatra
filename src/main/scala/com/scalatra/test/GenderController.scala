package com.scalatra.test

import org.scalatra._
// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

// JSON handling support from Scalatra
import org.scalatra.json._

/**
  * Created by Apratim Mishra on 7/15/16.
  */
class GenderController extends GenderScalatraStack with JacksonJsonSupport {

  // Sets up automatic case class to JSON output serialization
  protected implicit val jsonFormats: Formats = DefaultFormats
  val genderRepo = new GenderRepository;

  get("/genderize/:name") {
    val name = params("name").toLowerCase
    // convert response to json and return as OK
    genderRepo.getGenderFromName(name) match {
      case Some(x) => Ok(x);
      case None => NotFound("Name " + name + " not found");
    }
  }
}