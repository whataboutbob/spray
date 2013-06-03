import sbt._

object Dependencies {

  val resolutionRepos = Seq(
    "spray repo" at "http://repo.spray.io/"
  )

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")

  val scalaReflect  = "org.scala-lang"                          %   "scala-reflect"               % "2.10.1"
  val akkaActor     = "com.typesafe.akka"                       %%  "akka-actor"                  % "2.2-SNAPSHOT"
  val akkaOsgi      = "com.typesafe.akka"                       %%  "akka-osgi"                   % "2.2-SNAPSHOT"
  val akkaSlf4j     = "com.typesafe.akka"                       %%  "akka-slf4j"                  % "2.2-SNAPSHOT"
  val akkaTestKit   = "com.typesafe.akka"                       %%  "akka-testkit"                % "2.2-SNAPSHOT"
  val tsConfig      = "com.typesafe"                            %   "config"                      % "1.0.1"
  val parboiled     = "org.parboiled"                           %%  "parboiled-scala"             % "1.1.5"
  val shapeless     = "com.chuusai"                             %%  "shapeless"                   % "1.2.4"
  val scalatest     = "org.scalatest"                           %%  "scalatest"                   % "1.9.1"
  val specs2        = "org.specs2"                              %%  "specs2"                      % "1.14"
  val sprayJson     = "io.spray"                                %%  "spray-json"                  % "1.2.4"
  val twirlApi      = "io.spray"                                %%  "twirl-api"                   % "0.6.2"
  val clHashMap     = "com.googlecode.concurrentlinkedhashmap"  %   "concurrentlinkedhashmap-lru" % "1.3.2"
  val jettyWebApp   = "org.eclipse.jetty"                       %   "jetty-webapp"                % "8.1.10.v20130312"
  val servlet30     = "org.eclipse.jetty.orbit"                 %   "javax.servlet"               % "3.0.0.v201112011016" artifacts Artifact("javax.servlet", "jar", "jar")
  val logback       = "ch.qos.logback"                          %   "logback-classic"             % "1.0.12"
  val mimepull      = "org.jvnet.mimepull"                      %   "mimepull"                    % "1.9.2"
  val pegdown       = "org.pegdown"                             %   "pegdown"                     % "1.2.1"
  val liftJson      = "net.liftweb"                             %%  "lift-json"                   % "2.5-RC5"
  val json4sNative  = "org.json4s"                              %%  "json4s-native"               % "3.2.4"
  val json4sJackson = "org.json4s"                              %%  "json4s-jackson"              % "3.2.4"
  val osgiCore      = "org.osgi"                                %   "org.osgi.core"               % "4.2.0"
}
