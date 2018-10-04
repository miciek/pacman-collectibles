package com.michalplachta.collectibles

import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.michalplachta.pacman.collectibles.data.Position
import com.michalplachta.pacman.collectibles.http.HttpRoutes
import io.circe.parser.{parse => json}
import org.scalatest.{Matchers, WordSpec}

class HttpRoutesTest extends WordSpec with Matchers with ScalatestRouteTest {
  "HTTP Routes" should {
    "allow creating a new collectible set" in {
      val route = HttpRoutes.createCollectiblesRoute((_, _) => ())

      val entity = HttpEntity(`application/json`, s"""[ { "x": 2, "y": 7 } ]""")
      Put(s"/collectibles/27", entity) ~> route ~> check {
        json(responseAs[String]) should be(json("27"))
      }
    }

    "allow getting remaining positions from the collectible set" in {
      val route = HttpRoutes.getRemainingCollectiblesRoute(_ => Set(Position(1, 2)))
      Get(s"/collectibles/27") ~> route ~> check {
        json(responseAs[String]) should be(json("""[ { "x": 1, "y": 2 } ]"""))
      }
    }

    "allow removing a position from the collectible set" in {
      val route = HttpRoutes.collectCollectibleRoute(_ => Set.empty, (_, _) => ())

      val entity = HttpEntity(`application/json`, s"""{ "x": 2, "y": 7 }""")
      Put(s"/collectibles/666/collect", entity) ~> route ~> check {
        json(responseAs[String]) should be(json("666"))
      }
    }
  }
}
