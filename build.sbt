name := """example"""
organization := "example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.2"

updateConfiguration in updateSbtClassifiers := (updateConfiguration in updateSbtClassifiers).value.withMissingOk(true)

libraryDependencies += guice
libraryDependencies ++= Seq(
  jdbc,
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  cacheApi,
  evolutions,
  javaWs
)
libraryDependencies += "com.google.code.gson" % "gson" % "1.7.1"
libraryDependencies += "org.hibernate" % "hibernate" % "3.2.0.cr5"