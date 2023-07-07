val catsCoreVersion = "2.9.0"
val catsEffectVersion = "3.5.0"
val circeVersion = "0.14.3"
val http4sVersion = "0.23.18"
val http4sBlazeVersion = "0.23.14"
val commonsIoVersion = "2.11.0"
val logstashVersion = "7.3"

lazy val compileDeps: Seq[ModuleID] = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.11",
  "org.typelevel" %% "log4cats-slf4j" % "2.4.0",
  "com.github.fd4s" %% "fs2-kafka-vulcan" % "3.0.1",
  "commons-io" % "commons-io" % commonsIoVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-generic-extras" % "0.14.3",
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sBlazeVersion,
  "org.typelevel" %% "cats-core" % catsCoreVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "net.logstash.logback" % "logstash-logback-encoder" % logstashVersion
)

lazy val deps: Seq[Def.Setting[Seq[ModuleID]]] = Seq(
  libraryDependencies ++= compileDeps
)

lazy val root = (project in file("."))
  .enablePlugins(JavaAgent, DockerPlugin, JavaAppPackaging)
  .settings(Common.values)
  .settings(deps)
  .settings(scalafmtConfig := file(".scalafmt.conf"))
  .settings(DockerSettings.dockerSettings)

lazy val mainClassName = "service.Main"
lazy val debuggingPort = sys.env.getOrElse("DEBUGGING_PORT", 5025)

addCommandAlias("dockerFileTask", "docker:stage")

ThisBuild / useCoursier := false
