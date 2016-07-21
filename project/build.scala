import sbt._
import sbt.Keys._
import org.scalatra.sbt._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._
import sbtassembly.AssemblyKeys
import sbtdocker.mutable.Dockerfile

import sbtdocker.{BuildOptions, DockerPlugin}
import sbtdocker.DockerKeys._

object genderizescalatraBuild extends Build {
  val Organization = "github.com/appym"
  val Name = "genderize_scalatra"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.7"
  val ScalatraVersion = "2.4.1"

  lazy val project = Project (
    "genderize_scalatra",
    file("."),
    settings = ScalatraPlugin.scalatraSettings ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-json" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "9.2.10.v20150310" % "container;compile",
        "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
        "com.typesafe.slick" %% "slick" % "2.1.0",
        "org.json4s"   %% "json4s-jackson" % "3.4.0",
        "net.liftweb" %% "lift-json" % "2.6",
        "org.xerial" % "sqlite-jdbc" % "3.8.7",
        "com.github.tototoshi" %% "scala-csv" % "1.3.3"
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      },
      exportJars := true,

      // Make the docker task depend on the assembly task, which generates a fat JAR file
      docker <<= (docker dependsOn AssemblyKeys.assembly),
      dockerfile in docker :=  {
        val artifact = (AssemblyKeys.assemblyOutputPath in AssemblyKeys.assembly).value
        println(artifact)
        val artifactTargetPath = s"/app/${artifact.name}"
        println(artifactTargetPath)
        new Dockerfile {
          from("java:8")
          maintainer("Apratim Mishra<amishra@dstsystems.com>")
          expose(8080,8080)
          //copy(artifact, artifactTargetPath)
          add(artifact, artifactTargetPath)
          entryPoint("java","-jar",artifactTargetPath)
        }
      },
      imageNames in docker := Seq(
        sbtdocker.ImageName(repository = "genderize-scalatra",tag = Some("0.1.0"))
      )
    )
  ).enablePlugins(DockerPlugin)
}
