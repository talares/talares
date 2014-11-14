name := "talares"

organization in ThisBuild := "org.talares"

version in ThisBuild := "0.1.0"

scalaVersion in ThisBuild := "2.11.4"

crossScalaVersions in ThisBuild := Seq("2.10.4", "2.11.4")

resolvers in ThisBuild ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

scalacOptions in ThisBuild ++= Seq(
  "-deprecation", "-unchecked", "-feature",
  "-language:postfixOps", "-language:implicitConversions", "-language:existentials"
)

parallelExecution in Test in ThisBuild := false

lazy val commonSettings = {
  pomExtra := {
    <url>https://talares.github.io/talares</url>
      <licenses>
        <license>
          <name>Apache 2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:talares/talares.git</url>
        <connection>scm:git:git@github.com:talares/talares.git</connection>
      </scm>
      <developers>
        <developer>
          <id>DennisVis</id>
          <name>Dennis Vis</name>
          <url>https://github.com/DennisVis</url>
        </developer>
      </developers>
  }
}

lazy val root =
  (project in file("."))
    .settings(commonSettings: _*)
    .aggregate(talares, talaresJava)

lazy val talares =
  (project in file("src/talares"))
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-actor" % "2.3.7",
        "com.typesafe.play" %% "play-json" % "2.3.6",
        "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
        "com.typesafe.akka" %% "akka-testkit" % "2.3.7" % "test",
        "org.specs2" %% "specs2" % "2.4.2" % "test"
      )
    )

lazy val talaresJava =
  (project in file("src/talares-java"))
    .settings(commonSettings: _*)
    .settings(
      name := "talares-java",
      libraryDependencies ++= Seq(
        "com.typesafe.play" %% "play-java" % "2.3.6",
        "com.novocode" % "junit-interface" % "0.11" % "test"
      )
    )
    .dependsOn(talares % "compile->compile;test->test")

autoAPIMappings := true

pgpReadOnly := false

publishMavenStyle := true

publishTo in ThisBuild := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false}

sonatypeSettings

git.remoteRepo := "git@github.com:talares/talares.git"

site.settings

site.includeScaladoc()

site.jekyllSupport()

ghpages.settings