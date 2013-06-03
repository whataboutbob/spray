import sbt._
import Keys._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._
import scala._
import scala.Some
import spray.revolver.RevolverPlugin.Revolver
import twirl.sbt.TwirlPlugin.Twirl
import ls.Plugin._

import com.typesafe.sbt.osgi.OsgiKeys
import com.typesafe.sbt.osgi.SbtOsgi._

object BuildSettings {
  val VERSION = "1.1-M8-SNAPSHOT"

  lazy val basicSettings = seq(
    version               := NightlyBuildSupport.buildVersion(VERSION),
    homepage              := Some(new URL("http://spray.io")),
    organization          := "io.spray",
    organizationHomepage  := Some(new URL("http://spray.io")),
    description           := "A suite of lightweight Scala libraries for building and consuming RESTful " +
                             "web services on top of Akka",
    startYear             := Some(2011),
    licenses              := Seq("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    scalaVersion          := "2.10.1",
    resolvers             ++= Dependencies.resolutionRepos,
    scalacOptions         := Seq(
      "-encoding", "utf8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-target:jvm-1.6",
      "-language:_",
      "-Xlog-reflective-calls"
    )
  )

  lazy val sprayModuleSettings =
    basicSettings ++ formatSettings ++
    NightlyBuildSupport.settings ++
    net.virtualvoid.sbt.graph.Plugin.graphSettings ++
    seq(
      // scaladoc settings
      (scalacOptions in doc) <++= (name, version).map { (n, v) => Seq("-doc-title", n, "-doc-version", v) },

      // publishing
      crossPaths := false,
      publishMavenStyle := true,
      publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository"))),

      // LS
      (LsKeys.tags in LsKeys.lsync) := Seq("http", "server", "client", "async"),
      (LsKeys.docsUrl in LsKeys.lsync) := Some(new URL("http://spray.github.com/spray/api/spray-can/")),
      (externalResolvers in LsKeys.lsync) := Seq("spray repo" at "http://repo.spray.io")
    )

  lazy val noPublishing = seq(
    publish := (),
    publishLocal := ()
  )

  lazy val generateSprayVersionConf = TaskKey[Seq[File]]("generate-spray-version-conf",
    "Create a reference.conf file in the managed resources folder that contains a spray.version = ... setting")

  lazy val sprayVersionConfGeneration = seq(
    (unmanagedResources in Compile) <<= (unmanagedResources in Compile).map(_.filter(_.getName != "reference.conf")),
    resourceGenerators in Compile <+= generateSprayVersionConf,
    generateSprayVersionConf <<= (unmanagedResourceDirectories in Compile, resourceManaged in Compile, version) map {
      (sourceDir, targetDir, version) => {
        val source = sourceDir / "reference.conf"
        val target = targetDir / "reference.conf"
        val conf = IO.read(source.get.head)
        IO.write(target, conf.replace("<VERSION>", version))
        Seq(target)
      }
    }
  )

  lazy val siteSettings = basicSettings ++ formatSettings ++ noPublishing ++ Twirl.settings ++ Revolver.settings ++
    SiteSupport.settings

  lazy val docsSettings = basicSettings ++ noPublishing ++ seq(
    unmanagedSourceDirectories in Test <<= baseDirectory { _ ** "code" get }
  )

  lazy val exampleSettings = basicSettings ++ noPublishing

  lazy val benchmarkSettings = basicSettings ++ noPublishing ++ Revolver.settings ++ assemblySettings ++ Seq(
    mainClass in assembly := Some("spray.examples.Main"),
    jarName in assembly := "benchmark.jar",
    test in assembly := {},
    javaOptions in Revolver.reStart ++= Seq("-verbose:gc", "-XX:+PrintCompilation")
  )

  lazy val osgiExampleSettings = sprayModuleSettings ++ osgiSettings ++ Seq(OsgiKeys.bundleActivator := Option("spray.examples.Activator")) ++ Seq(OsgiKeys.exportPackage := Seq("spray.examples"))

  import com.github.siasia.WebPlugin._
  lazy val jettyExampleSettings = exampleSettings ++ webSettings // ++ disableJettyLogSettings

  import com.github.siasia.PluginKeys._
  lazy val disableJettyLogSettings = inConfig(container.Configuration) {
    seq(
      start <<= (state, port, apps, customConfiguration, configurationFiles, configurationXml) map {
        (state, port, apps, cc, cf, cx) =>
          state.get(container.attribute).get.start(port, None, Utils.NopLogger, apps, cc, cf, cx)
      }
    )
  }

  lazy val formatSettings = SbtScalariform.scalariformSettings ++ Seq(
    ScalariformKeys.preferences in Compile := formattingPreferences,
    ScalariformKeys.preferences in Test    := formattingPreferences
  )

  import scalariform.formatter.preferences._
  def formattingPreferences =
    FormattingPreferences()
      .setPreference(RewriteArrowSymbols, true)
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)

}