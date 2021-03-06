organization := "com.scet"

name := "play-authenticate-hibernate"

scalaVersion := "2.11.0"

version := "1.0-SNAPSHOT"

lazy val root = project.in(file(".")).enablePlugins(PlayJava)
resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += "Typesafe repository1" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "Typesafe repository2" at "https://dl.bintray.com/typesafe/maven-releases/"
resolvers += "Typesafe repository3" at "https://mvnrepository.com/artifact/com.typesafe.play/play"
resolvers += "Keks rep" at "https://dl.bintray.com/playframework/sbt-plugin-releases"
libraryDependencies ++= Seq(
  javaJpa,
  "be.objectify"  %% "deadbolt-java"     % "2.5.0",
// Comment the next line for local development of the Play Authentication core:
  "com.feth"      %% "play-authenticate" % "0.8.3",
  //"org.postgresql"    %  "postgresql"        % "9.4-1201-jdbc41",
  "org.hibernate" % "hibernate-entitymanager" % "5.2.7.Final",
  "org.postgresql"    %  "postgresql"        % "9.4-1206-jdbc42",
  javaJdbc,
  cache,
  javaWs,
  "org.webjars" % "bootstrap" % "3.2.0",
  "org.easytesting" % "fest-assert" % "1.4" % "test",
  "dom4j" % "dom4j" % "1.6.1",
  "com.google.inject" % "guice" % "4.0",
  "org.jetbrains" % "annotations" % "13.0",
  "com.fasterxml.jackson.module" % "jackson-modules-java8" % "2.8.5" pomOnly(),
  "org.immutables" % "value" % "2.6.1" % "provided",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-guava" % "2.8.5",
  "com.rabbitmq" % "amqp-client" % "2.8.1",
  "org.telegram" % "telegrambots" % "3.6.1",
  "org.igniterealtime.smack" % "smack" % "3.2.1",
  "com.google.guava" % "guava" % "23.0",
  "org.telegram" % "telegrambotsextensions" % "3.6.1"
)
sources in doc in Compile := List()
PlayKeys.externalizeResources := false
// add resolver for deadbolt and easymail snapshots


//  Uncomment the next line for local development of the Play Authenticate core:
//lazy val playAuthenticate = project.in(file("modules/play-authenticate")).enablePlugins(PlayJava)
