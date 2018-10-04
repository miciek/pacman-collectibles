package com.michalplachta.pacman.collectibles
import akka.http.scaladsl.server.{HttpApp, Route}
import com.michalplachta.pacman.collectibles.http.StatefulHttpRoute
import com.typesafe.config.ConfigFactory

object Main extends App {
  val config = ConfigFactory.load()
  val host   = config.getString("app.host")
  val port   = config.getInt("app.port")

  val server = new StatefulHttpRoute
  val httpApp = new HttpApp {
    override protected def routes: Route =
      Route.seal(server.route)
  }

  httpApp.startServer(host, port)
}
