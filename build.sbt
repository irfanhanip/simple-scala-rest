name := "simple-scala-rest"

version := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  evolutions,
  "com.h2database" % "h2" % "1.4.191",
  cache,
  ws,
  specs2 % Test)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
