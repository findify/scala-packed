scalaVersion in ThisBuild := "2.11.8"

lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  resolvers += Resolver.url(
    "scalameta",
    url("http://dl.bintray.com/scalameta/maven"))(Resolver.ivyStylePatterns),
  addCompilerPlugin(
    "org.scalameta" % "paradise" % "3.0.0.132" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  scalacOptions in (Compile, console) := Seq(), // macroparadise plugin doesn't work in repl yet.
  sources in (Compile, doc) := Nil // macroparadise doesn't work with scaladoc yet.
)

lazy val macros = (project in file("macros")).settings(
  name := "scala-packed-macro",
  metaMacroSettings,
  libraryDependencies += "org.scalameta" %% "scalameta" % "1.4.0.544",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1"
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