import sbtcrossproject.CrossPlugin.autoImport.crossProject

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(
  List(
    scalaVersion := "2.13.6",
    organization := "com.rpiaggio",
    homepage := Some(url("https://github.com/rpiaggio/crystal")),
    licenses += ("BSD 3-Clause", url(
      "http://opensource.org/licenses/BSD-3-Clause"
    )),
    developers := List(
      Developer(
        "rpiaggio",
        "Raúl Piaggio",
        "rpiaggio@gmail.com",
        url("http://rpiaggio.com")
      )
    ),
    addCompilerPlugin(
      ("org.typelevel" % "kind-projector" % "0.13.0").cross(CrossVersion.full)
    )
  )
)

lazy val root = project
  .in(file("."))
  .aggregate(crystalJVM, crystalJS)
  .settings(
    name := "crystal",
    publish := {},
    publishLocal := {}
  )

lazy val crystal = crossProject(JVMPlatform, JSPlatform)
  .in(file("."))
  .settings(
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-Xfatal-warnings",
      "-encoding",
      "UTF-8"
    ),
    Test / scalacOptions += "-Ymacro-annotations",
    libraryDependencies ++=
      Settings.Libraries.CatsJS.value ++
        Settings.Libraries.CatsEffectJS.value ++
        Settings.Libraries.Fs2JS.value ++
        Settings.Libraries.Monocle.value ++
        Settings.Libraries.Log4Cats.value ++
        (
          Settings.Libraries.MUnit.value ++
            Settings.Libraries.Discipline.value ++
            Settings.Libraries.DisciplineMUnit.value ++
            Settings.Libraries.CatsLaws.value ++
            Settings.Libraries.MonocleMacro.value
        ).map(_ % Test),
    scmInfo := Some(
      ScmInfo(
        url("https://https://github.com/rpiaggio/crystal"),
        "scm:git:git@github.com:rpiaggio/crystal.git",
        Some("scm:git:git@github.com:rpiaggio/crystal.git")
      )
    ),
    pomIncludeRepository := { _ => false },
    testFrameworks += new TestFramework("munit.Framework")
  )
  .jsSettings(
    libraryDependencies ++=
      Settings.Libraries.ReactScalaJS.value,
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
  )

lazy val crystalJS = crystal.js

lazy val crystalJVM = crystal.jvm

sonatypeProfileName := "com.rpiaggio"

root / packagedArtifacts := Map.empty
