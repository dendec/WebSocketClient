name := """WebSocket client"""

version := "1.0"

scalaVersion := "2.11.6"

unmanagedJars in Compile += Attributed.blank(file("/usr/lib/jvm/java-7-oracle/jre/lib/jfxrt.jar"))

resolvers += "Spray" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "com.wandoulabs.akka" %% "spray-websocket" % "0.1.4",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalafx" %% "scalafx" % "2.2.76-R11")
