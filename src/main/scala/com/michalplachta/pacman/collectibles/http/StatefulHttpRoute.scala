package com.michalplachta.pacman.collectibles.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteConcatenation._
import com.michalplachta.pacman.collectibles.data.Position

import scala.collection.concurrent.TrieMap

class StatefulHttpRoute extends {
  private val state                        = TrieMap.empty[Int, Set[Position]]
  def getFromState(id: Int): Set[Position] = state.getOrElse(id, Set.empty)

  val route: Route = {
    HttpRoutes.createCollectiblesRoute(state.put) ~
    HttpRoutes.getRemainingCollectiblesRoute(getFromState) ~
    HttpRoutes.collectCollectibleRoute(getFromState, state.put)
  }
}
