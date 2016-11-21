// scala.meta macros are at the moment only supported in 2.11.
scalaVersion in ThisBuild := "2.11.8"

lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  // New-style macro annotations are under active development.  As a result, in
  // this build we'll be referring to snapshot versions of both scala.meta and
  // macro paradise.
  resolvers += Resolver.url(
    "scalameta",
    url("http://dl.bintray.com/scalameta/maven"))(Resolver.ivyStylePatterns),
  // A dependency on macro paradise 3.x is required to both write and expand
  // new-style macros.  This is similar to how it works for old-style macro
  // annotations and a dependency on macro paradise 2.x.
  addCompilerPlugin(
    "org.scalameta" % "paradise" % "3.0.0.132" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  // temporary workaround for https://github.com/scalameta/paradise/issues/10
  scalacOptions in (Compile, console) := Seq(), // macroparadise plugin doesn't work in repl yet.
  // temporary workaround for https://github.com/scalameta/paradise/issues/55
  sources in (Compile, doc) := Nil // macroparadise doesn't work with scaladoc yet.
)

// Define macros in this project.
lazy val macros = (project in file("macros")).settings(
  name := "scala-packed-macro",
  metaMacroSettings,
  // A dependency on scala.meta is required to write new-style macros, but not
  // to expand such macros.  This is similar to how it works for old-style
  // macros and a dependency on scala.reflect.
  libraryDependencies += "org.scalameta" %% "scalameta" % "1.4.0.544",
  libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.1"
)

// Use macros in this project.
lazy val core = (project in file("core")).settings(
  name := "scala-packed-core",
  metaMacroSettings
).dependsOn(macros)