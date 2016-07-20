import sbt._
import Keys._
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
        "org.eclipse.jetty" % "jetty-webapp" % "9.2.10.v20150310" % "container",
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
      }
    )
//    dockerBaseImage := "java",
//    maintainer in Docker  := "Apratim",
//    packageName in Docker := name.value,
//    version in Docker     := version.value,
//    dockerExposedPorts in Docker := Seq(8080)
  ).enablePlugins(DockerPlugin)

  // THE FOLLOWING SECTION IS UNUSED AS I HAD TO MANUALLY CREATE A DOCKER FILE. THIS WAS ORIGINALLY INTENDED TO MAKE
  // SBT DOCKER PLUGIN WORK.
  lazy val dockerSettings = Seq(
    // Make the docker task depend on the assembly task, which generates a fat JAR file
    docker <<= (docker dependsOn (AssemblyKeys.assembly in project)),
    dockerfile in docker :=  {
      val artifact = (AssemblyKeys.assemblyOutputPath in AssemblyKeys.assembly in project).value
      val artifactTargetPath = s"/app/${artifact.name}"
      new Dockerfile {
        from("ubuntu")
        maintainer("Apratim Mishra<amishra@dstsystems.com>")
        run("apt-get", "-y", "install", "openjdk-8-jre-headless")
        expose(8080)
        //copy(artifact, artifactTargetPath)
        entryPoint("sbt")
        cmd("container:start")
      }
    },
    imageNames in docker := Seq(
      sbtdocker.ImageName(namespace = Some("genderize-scalatra"),
        repository = "genderize-scalatra",tag = Some("0.1.0"))
    )
  )
}
