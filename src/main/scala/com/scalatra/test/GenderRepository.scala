package com.scalatra.test

import scala.slick.jdbc.JdbcBackend._
import Database.dynamicSession
import scala.slick.jdbc.StaticQuery.interpolation

/**
  * Created by Apratim Mishra on 7/15/16.
  */
class GenderRepository extends SlickSupport {

  def getGenderFromName(inp_name: String): Option[String] = {
    val sql = sql"select predicted_gender from us_names where lower(name) like $inp_name".as[(String)]

    database.withDynSession {
      sql.firstOption
    }
  }


}
