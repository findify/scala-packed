scalaVersion in ThisBuild := "2.12.3"

lazy val core = (project in file("core")).settings(
  name := "scala-packed-core",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.4",
    "com.chuusai" %% "shapeless" % "2.3.2",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )
)

lazy val benchmark = (project in file("benchmark")).settings(
  name := "scala-packed-benchmark",
  libraryDependencies += "com.github.jbellis" % "jamm" % "0.3.2"
).dependsOn(core).enablePlugins(JmhPlugin)