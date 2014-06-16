scalaVersion := "2.9.2"
//scalaVersion := "2.10.2"

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.4.0")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.2" % "test"

// coveralls
//resolvers += Classpaths.sbtPluginReleases
//addSbtPlugin("org.scoverage" %% "sbt-scoverage" % "0.99.5")
//or com.sksamuel.scoverage?
//addSbtPlugin("com.sksamuel.scoverage" %% "sbt-coveralls" % "0.0.5")