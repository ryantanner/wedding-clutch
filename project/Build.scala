import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "wedding-rest"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.webjars" % "bootstrap" % "2.1.1",
      "jp.t2v" % "play20.auth_2.9.1" % "0.4"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers += "t2v.jp repo" at "http://www.t2v.jp/maven-repo/"
    )

}
