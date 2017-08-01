scalaVersion in ThisBuild := "2.12.3"


lazy val core = (project in file("core")).settings(
  name := "scalapacked",
  version := "0.1",
  organization := "io.findify",
  licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  publishMavenStyle := true,
  homepage := Some(url("https://github.com/findify/scala-packed")),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <scm>
      <url>git@github.com:findify/scala-packed.git</url>
      <connection>scm:git:git@github.com:findify/scala-packed.git</connection>
    </scm>
      <developers>
        <developer>
          <id>romangrebennikov</id>
          <name>Roman Grebennikov</name>
          <url>http://www.dfdx.me</url>
        </developer>
      </developers>),
  libraryDependencies ++= Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
    "com.chuusai" %% "shapeless" % "2.3.2",
    "org.scalatest" %% "scalatest" % "3.0.3" % "test"
  )
)

lazy val benchmark = (project in file("benchmark")).settings(
  name := "scala-packed-benchmark",
  libraryDependencies += "com.github.jbellis" % "jamm" % "0.3.1"
).dependsOn(core).enablePlugins(JmhPlugin)