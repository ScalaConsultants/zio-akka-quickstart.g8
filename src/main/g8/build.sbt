val akkaHttpVersion       = "10.2.2"
val akkaVersion           = "2.6.10"
val slickVersion          = "3.3.3"
val zioVersion            = "1.0.3"
val zioLoggingVersion     = "0.5.4"
val zioConfigVersion      = "1.0.0-RC31-1"
val flywayVersion         = "7.4.0"
val testContainersVersion = "0.38.8"
$if(add_caliban_endpoint.truthy)$
val calibanVersion        = "0.9.4"
$endif$

val dockerReleaseSettings = Seq(
  dockerExposedPorts := Seq(8080),
  dockerExposedVolumes := Seq("/opt/docker/logs"),
  dockerBaseImage := "adoptopenjdk/openjdk12:x86_64-ubuntu-jre-12.0.2_10"
)

lazy val It = config("it").extend(Test)

val root = (project in file("."))
  .configs(It)
  .settings(
    inConfig(It)(Defaults.testSettings),
    inThisBuild(
      List(
        organization := "$organization$",
        scalaVersion := "$scala_version$"
      )
    ),
    name := "$name$",
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka"     %% "akka-http"                       % akkaHttpVersion,
      "de.heikoseeberger"     %% "akka-http-play-json"             % "1.35.2",
      "com.typesafe.akka"     %% "akka-actor-typed"                % akkaVersion,
      "com.typesafe.akka"     %% "akka-stream"                     % akkaVersion,
      "com.typesafe.slick"    %% "slick"                           % slickVersion,
      "com.typesafe.slick"    %% "slick-hikaricp"                  % slickVersion,
      "dev.zio"               %% "zio"                             % zioVersion,
      "dev.zio"               %% "zio-streams"                     % zioVersion,
      "dev.zio"               %% "zio-config"                      % zioConfigVersion,
      "dev.zio"               %% "zio-config-magnolia"             % zioConfigVersion,
      "dev.zio"               %% "zio-config-typesafe"             % zioConfigVersion,
      "io.scalac"             %% "zio-akka-http-interop"           % "0.4.0",
      "io.scalac"             %% "zio-slick-interop"               % "0.2.0",
      "dev.zio"               %% "zio-interop-reactivestreams"     % "1.3.0.7-2",
      "ch.qos.logback"        % "logback-classic"                  % "1.2.3",
      "dev.zio"               %% "zio-logging"                     % zioLoggingVersion,
      "dev.zio"               %% "zio-logging-slf4j"               % zioLoggingVersion,
      "org.postgresql"        % "postgresql"                       % "42.2.18",
      "org.flywaydb"          % "flyway-core"                      % flywayVersion,
      $if(add_caliban_endpoint.truthy)$
      "com.github.ghostdogpr" %% "caliban"                         % calibanVersion,
      "com.github.ghostdogpr" %% "caliban-akka-http"               % calibanVersion,
      $endif$
      "com.typesafe.akka"     %% "akka-http-testkit"               % akkaHttpVersion % Test,
      "com.typesafe.akka"     %% "akka-stream-testkit"             % akkaVersion % Test,
      "com.typesafe.akka"     %% "akka-actor-testkit-typed"        % akkaVersion % Test,
      "dev.zio"               %% "zio-test-sbt"                    % zioVersion % Test,
      "com.dimafeng"          %% "testcontainers-scala-postgresql" % testContainersVersion % It
    ),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
    dockerReleaseSettings
  )
  .enablePlugins(DockerPlugin, JavaAppPackaging)
