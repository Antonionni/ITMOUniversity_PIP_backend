resolvers += "Typesafe repository1" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "Typesafe repository2" at "https://dl.bintray.com/typesafe/maven-releases/"
resolvers += "Typesafe repository3" at "https://mvnrepository.com/artifact/com.typesafe.play/play"
resolvers += "Keks rep" at "https://dl.bintray.com/playframework/sbt-plugin-releases"
resolvers += Resolver.sonatypeRepo("snapshots")
scalaVersion := "2.10.6"
// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.0")


