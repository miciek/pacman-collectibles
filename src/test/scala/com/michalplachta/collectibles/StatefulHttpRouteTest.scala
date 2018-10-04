package com.michalplachta.collectibles

import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.michalplachta.pacman.collectibles.data.Position
import com.michalplachta.pacman.collectibles.http.StatefulHttpRoute
import org.scalatest.{GivenWhenThen, Matchers, WordSpec}
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

class StatefulHttpRouteTest extends WordSpec with Matchers with ScalatestRouteTest with GivenWhenThen {
  "Stateful HTTP Route" should {
    "support the full happy path" in {
      Given("freshly started server")
      val route = new StatefulHttpRoute().route

      When("a new collectibles is added")
      val positionsEntity =
        HttpEntity(`application/json`, s"""[ { "x": 1, "y": 2 }, { "x": 3, "y": 4 } ]""")
      Put("/collectibles/1", positionsEntity) ~> route ~> check {
        responseAs[Int] should be(1)
      }

      And("one position is collected")
      val collectEntity =
        HttpEntity(`application/json`, s"""{ "x": 1, "y": 2 }""")
      Put("/collectibles/1/collect", collectEntity) ~> route ~> check {
        responseAs[Int] should be(1)
      }

      Then("the collectibles should contain one remaining position")
      Get("/collectibles/1") ~> route ~> check {
        responseAs[Set[Position]] should be(Set(Position(3, 4)))
      }
    }
  }
}
