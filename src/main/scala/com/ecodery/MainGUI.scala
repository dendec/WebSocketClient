package com.ecodery

import spray.can.websocket.Send
import spray.can.websocket.frame.{CloseFrame, TextFrame}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.{Platform, JFXApp}
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{ListView, Button, TextField}
import scalafx.scene.layout.{HBox, BorderPane}
import scalafx.Includes._

/**
 * Created by denis on 04.06.15. MainGUI
 */
object MainGUI extends JFXApp {
  val lvResponses = new ListView[String]

  val wsClient = WebSocket.getWebSocketActorRef("127.0.0.1", 9000, "/api/v1/ws", { frame =>
    Platform.runLater(lvResponses.items() += frame.payload.utf8String)
  })

  val tfMessage = new TextField
  val bSendButton = new Button("Send")
  val sendAction = (e: ActionEvent) => {
    wsClient ! Send(TextFrame(tfMessage.text.value))
  }
  tfMessage.onAction = sendAction
  bSendButton. onAction = sendAction

  val topPane = new HBox {
    spacing = 10.0
    padding.value = Insets(5.0)
    children = List(tfMessage, bSendButton)
  }

  stage = new PrimaryStage() {
    title = "WebSocket client"
    width = 400
    height = 300
    scene = new Scene {
      root = new BorderPane {
        top = topPane
        center = lvResponses
      }
    }
  }

  override def stopApp() = {
    wsClient ! Send(CloseFrame())
    WebSocket.system.shutdown()
  }
}
