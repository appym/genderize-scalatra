//package com.scalatra.test
//
//import org.scalatra.test.specs2._
//
//// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
//class TestServletSpec extends ScalatraSpec { def is =
//  "GET /genderize on TestServlet"                     ^
//    "should return status 200"                  ! root200^
//                                                end
//
//  addServlet(classOf[GenderController], "/genderize/*")
//
//  def genderize200 = get("/genderize/bob") {
//    status must_== 200
//  }
//}
