logLevel := Level.Info

classpathTypes += "maven-plugin"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.9"

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.3")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.6")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.13")

addSbtPlugin("ch.epfl.scala" % "sbt-bloop" % "1.5.6")

addDependencyTreePlugin

addSbtPlugin("net.vonbuchholtz" % "sbt-dependency-check" % "4.3.0")

addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.6")
