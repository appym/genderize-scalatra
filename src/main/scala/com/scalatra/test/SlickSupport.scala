package com.scalatra.test

import com.scalatra.test.PersonDAO.Person

import scala.slick.jdbc.JdbcBackend.Database
import scala.slick.jdbc.StaticQuery
import scala.slick.jdbc.meta.MTable
import scala.slick.lifted.{TableQuery, Tag}
import scala.slick.driver.SQLiteDriver.simple._
import com.github.tototoshi.csv._

/**
  * Created by dt205202 on 7/15/16.
  */
trait SlickSupport {
  import Database.dynamicSession

  val database = Database.forURL(
    "jdbc:sqlite:%s.db" format "genderDB",
    driver = "org.sqlite.JDBC")


  def configureDB() {
    implicit class DatabaseOps(database: Database) {
      def apply(sql: String) {
        database withDynSession {
          StaticQuery.updateNA(sql).execute
        }
      }

     def tableNames(): Set[String] = database withDynSession {
        (MTable.getTables.list map { _.name.name }).toSet
      }
    }
    if (!database.tableNames().contains("us_names")) {
      database("create table us_names (name text, predicted_gender text);")
    }
    val data = CSVConverter.convert("src/main/resources/usprocessed.csv")
    PersonDAO.insertIntoTableQuery(data)

  }
}

object PersonDAO extends SlickSupport {
  implicit val session: Session = database.createSession()
  case class Person(name: String, predicted_gender: String)

  class PersonData(tag: Tag) extends Table[Person](tag,"us_names") {
    def name = column[String]("name", O.PrimaryKey)
    def predicted_gender = column[String]("predicted_gender")
    def * = (name,  predicted_gender) <> (Person.tupled, Person.unapply)
  }

  val persons = TableQuery[PersonData]

  def insertIntoTableQuery(data: List[Person]) = {
      persons ++= data
  }
}

object CSVConverter {
  import java.io.File
  import scala.collection.mutable.ListBuffer

  def convert(filename: String) = {
    val reader = CSVReader.open(new File(filename))
    val rawList = reader.iterator.toList
    val personList = new ListBuffer[Person]
    rawList.foreach(line => personList ++= List(Person(line(0), line(4))))
    personList.toList
  }
}