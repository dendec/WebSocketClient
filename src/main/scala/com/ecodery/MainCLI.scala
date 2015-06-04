package com.ecodery

import spray.can.websocket.Send
import spray.can.websocket.frame.{CloseFrame, TextFrame}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by denis on 04.06.15. web socket cli "hello" send
 */
object MainCLI extends App {
  val wsClient = WebSocket.getWebSocketActorRef("127.0.0.1", 9000, "/api/v1/ws")
  wsClient ! Send(TextFrame("hello"))
  WebSocket.system.scheduler.scheduleOnce(2 second){
    wsClient ! Send(CloseFrame())
    WebSocket.system.shutdown()
  }
}