name := """example"""
organization := "example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.11"

updateConfiguration in updateSbtClassifiers := (updateConfiguration in updateSbtClassifiers).value.withMissingOk(true)

libraryDependencies ++= Seq(
  "org.webjars" % "bootstrap" % "3.2.0",
  jdbc,
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  cacheApi,
  evolutions,
  javaWs,
  "be.objectify"  %% "deadbolt-java"     % "2.5.0",
  "com.feth"      %% "play-authenticate" % "0.8.3",
  "org.hibernate" % "hibernate-core" % "5.2.2.Final"
)
resolvers += Resolver.sonatypeRepo("snapshots")


