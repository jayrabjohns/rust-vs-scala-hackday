import sbt.Keys.{name, scalaVersion, scalacOptions, version}
import sbt._

object Common {
  lazy val values: Seq[Setting[_]] = Seq(
    name := "scala-service",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.10",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-language:higherKinds",
      "-language:postfixOps",
      "-feature",
      "-Xfatal-warnings"
    ),
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  )
}
