lazy val root = project
  .in(file("."))
  .settings(
    name         := "scala-scripts",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := "3.3.4",
    scalacOptions := Seq(
      "-rewrite",
      "-indent",
      "-Wunused:all",
    ),
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.2" % Test
    )
  )

Global / onChangedBuildSource := ReloadOnSourceChanges
