name := "vinted"

version := "1.0.0"

//scalaVersion := "2.10.2"
//scalaVersion := "2.10.0"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-deprecation" )

//"-feature"

//ScoverageSbtPlugin.instrumentSettings

//CoverallsPlugin.coverallsSettings

// include only in tests (last param)
libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.2" % "test"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.10.1" % "test"

// This setting allows to restrict the source files that are compiled and tested
// to one specific project. It should be either the empty string, in which case all
// projects are included, or one of the project names from the projectDetailsMap.
//currentProject := ""

//org.scalastyle.sbt.ScalastylePlugin.Settings