scalaVersion in ThisBuild := "2.12.2"

lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  resolvers += Resolver.url(
    "scalameta",
    url("http://dl.bintray.com/scalameta/maven"))(Resolver.ivyStylePatterns),
  addCompilerPlugin(
    "org.scalameta" % "paradise" % "3.0.0-M8" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  scalacOptions in (Compile, console) := Seq(), // macroparadise plugin doesn't work in repl yet.
  sources in (Compile, doc) := Nil // macroparadise doesn't work with scaladoc yet.
)

lazy val macros = (project in file("macros")).settings(
  name := "scala-packed-macro",
  metaMacroSettings,
  libraryDependencies ++= Seq(
    "org.scalameta" %% "scalameta" % "1.8.0",
    "org.scalatest" %% "scalatest" % "3.0.3",
    "com.chuusai" %% "shapeless" % "2.3.2",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )
)

// Use macros in this project.
lazy val core = (project in file("core")).settings(
  name := "scala-packed-core",
  metaMacroSettings
).dependsOn(macros)

lazy val benchmark = (project in file("benchmark")).settings(
  name := "scala-packed-benchmark",
  metaMacroSettings,
  libraryDependencies += "com.github.jbellis" % "jamm" % "0.3.1"
).dependsOn(core).enablePlugins(JmhPlugin)