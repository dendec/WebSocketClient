package com.ecodery

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import spray.can.Http
import spray.can.server.UHttp
import spray.can.websocket.{Send, WebSocketClientWorker}
import spray.can.websocket.frame.{TextFrame, Frame}
import spray.http.HttpRequest

/**
 * Created by denis on 04.06.15. WebSocket actor
 */
object WebSocket {

  implicit val system = ActorSystem()

  class WebSocketClient(connect: Http.Connect, val upgradeRequest: HttpRequest, val onReceive: Frame => Unit) extends WebSocketClientWorker {
    IO(UHttp) ! connect

    override def businessLogic: Receive = {
      case frame: Frame =>
        println(s"receive: ${frame.payload.utf8String}")
        onReceive(frame)

      case _: Http.ConnectionClosed =>
        println("connection closed")
        context.stop(self)

      case Send(frame) =>
        if (frame.isInstanceOf[TextFrame]) println(s"send: ${frame.payload.utf8String}")
        connection ! frame
    }
  }

  def getWebSocketActorRef(host: String, port: Int, uriPath: String, onReceive: Frame => Unit = _ => ()) = {
    val wsApiRequest = spray.can.websocket.basicHandshakeRepuset(uriPath)
    val connect = Http.Connect(host, port)
    system.actorOf(Props(
      new WebSocketClient(connect, wsApiRequest, onReceive)))
  }
}