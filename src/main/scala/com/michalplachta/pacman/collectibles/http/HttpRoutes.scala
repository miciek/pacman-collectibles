package com.michalplachta.pacman.collectibles.http
import akka.http.scaladsl.server.{Directives, Route}
import com.michalplachta.pacman.collectibles.data.Position
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

object HttpRoutes extends Directives with FailFastCirceSupport {
  def createCollectiblesRoute(createCollectibles: (Int, Set[Position]) => Unit): Route = {
    path("collectibles" / IntNumber) { collectiblesId =>
      put {
        entity(as[Set[Position]]) { positions =>
          createCollectibles(collectiblesId, positions)
          complete(collectiblesId)
        }
      }
    }
  }

  def getRemainingCollectiblesRoute(getRemaining: Int => Set[Position]): Route = {
    path("collectibles" / IntNumber) { collectiblesId =>
      get {
        val remainingCollectibles = getRemaining(collectiblesId)
        complete(remainingCollectibles)
      }
    }
  }

  def collectCollectibleRoute(getRemaining: Int => Set[Position],
                              updateRemaining: (Int, Set[Position]) => Unit): Route = {
    path("collectibles" / IntNumber / "collect") { collectiblesId =>
      put {
        entity(as[Position]) { position =>
          val collectibles = getRemaining(collectiblesId)
          updateRemaining(collectiblesId, collectibles - position)
          complete(collectiblesId)
        }
      }
    }
  }
}
