name := """simple-scala-rest"""

version := "1.0-SNAPSHOT"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-jdbc-evolutions" % "2.5.3",
  "com.typesafe.play" %% "play-jdbc-api" % "2.5.3",
  "com.typesafe.play" %% "play-specs2" % "2.5.3",
  "com.typesafe.play" %% "play-slick" % "2.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.h2database" % "h2" % "1.4.191"
)