package com.scalatra.test
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler}
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener
/**
  * Created by Apratim Mishra on 7/20/16.
  */
object JettyLauncher { // this is my entry object as specified in sbt project definition
def main(args: Array[String]) {
  val port =  8080

  val server = new Server(port)
  val context = new WebAppContext()
  context setContextPath "/"
  context.setResourceBase("src/main/webapp")
  context.addEventListener(new ScalatraListener)
  context.addServlet(classOf[DefaultServlet], "/")

  server.setHandler(context)

  server.start
  server.join
}
}

