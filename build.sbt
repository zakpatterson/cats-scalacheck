import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val root = project.in(file("."))
  .disablePlugins(MimaPlugin)
  .aggregate(
    coreJVM,
    coreJS
  )
  .settings(noPublishSettings)
  .settings(commonSettings, releaseSettings)


lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("core"))
  .settings(commonSettings, releaseSettings)
  .settings(
    name := "cats-scalacheck"
  )

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

lazy val docs = project.in(file("cats-scalacheck-docs"))
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(MdocPlugin)
  .settings(noPublishSettings)
  .settings(commonSettings, micrositeSettings)
  .settings(
    name := "cats-scalacheck-docs",
    crossScalaVersions := Seq("2.12.11"),
    moduleName := "cats-scalacheck-docs",
    mdocVariables := Map(
      "VERSION" -> version.value
    ),
  )
  .dependsOn(coreJVM)

val catsV = "2.0.0"
val catsTestkitV = "1.0.0-RC1"
val scalacheckV = "1.14.3"

lazy val contributors = Seq(
  "ChristopherDavenport" -> "Christopher Davenport"
)

lazy val commonSettings = Seq(
  organization := "io.chrisdavenport",

  crossScalaVersions := Seq("2.11.12", "2.12.11", "2.13.2"),

  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.10.3" cross CrossVersion.binary),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),

  libraryDependencies ++= Seq(
    "org.typelevel"               %%% "cats-core"                  % catsV,
    "org.scalacheck"              %%% "scalacheck"                 % scalacheckV,

    "org.typelevel"               %%% "cats-laws"                  % catsV % Test,
    "org.typelevel"               %%% "cats-testkit-scalatest"     % catsTestkitV % Test
  )
)

lazy val releaseSettings = {
  Seq(
    publishArtifact in Test := false,
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/ChristopherDavenport/cats-scalacheck"),
        "git@github.com:ChristopherDavenport/cats-scalacheck.git"
      )
    ),
    homepage := Some(url("https://github.com/ChristopherDavenport/cats-scalacheck")),
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    publishMavenStyle := true,
    pomIncludeRepository := { _ =>
      false
    },
    pomExtra := {
      <developers>
        {for ((username, name) <- contributors) yield
        <developer>
          <id>{username}</id>
          <name>{name}</name>
          <url>http://github.com/{username}</url>
        </developer>
        }
      </developers>
    }
  )
}

lazy val micrositeSettings = Seq(
  micrositeName := "cats-scalacheck",
  micrositeDescription := "Cats Instances for Scalacheck",
  micrositeAuthor := "Christopher Davenport",
  micrositeGithubOwner := "ChristopherDavenport",
  micrositeGithubRepo := "cats-scalacheck",
  micrositeBaseUrl := "/cats-scalacheck",
  micrositeDocumentationUrl := "https://christopherdavenport.github.io/cats-scalacheck",
  micrositeFooterText := None,
  micrositeHighlightTheme := "atom-one-light",
  micrositePalette := Map(
    "brand-primary" -> "#3e5b95",
    "brand-secondary" -> "#294066",
    "brand-tertiary" -> "#2d5799",
    "gray-dark" -> "#49494B",
    "gray" -> "#7B7B7E",
    "gray-light" -> "#E5E5E6",
    "gray-lighter" -> "#F4F3F4",
    "white-color" -> "#FFFFFF"
  ),
)

lazy val noPublishSettings = {
  Seq(
    publish := {},
    publishLocal := {},
    publishArtifact := false
  )
}
