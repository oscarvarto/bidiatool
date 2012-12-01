import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtScalariform._
import sbtassembly.Plugin._
import AssemblyKeys._

object Build extends Build {

  lazy val root = Project(
    "bidiatool",
    file("."),
    settings = commonSettings ++ Seq(
      libraryDependencies ++= Seq(
      )
    )
  ) aggregate(ui, plotting, fixedPoints, parsing, stability)

  // User Interface
  lazy val ui = Project(
    id = "bidiatool-ui",
    base = file("ui"),
    settings = commonSettings ++
    Seq(
      libraryDependencies ++= Seq(
        Dependencies.Compile.Swing
      )
    )
  ) dependsOn(parsing)
  
  import AddZipJar._
  
  lazy val plotting = Project(
    id = "bidiatool-plotting",
    base = file("plotting"),
    settings = commonSettings ++
    Seq(
      libraryDependencies ++= Seq(
        Dependencies.Compile.Swing,
        Dependencies.Compile.BreezeViz,
        Dependencies.Compile.Jzy3d  
      ),
      mergeStrategy in assembly <<= (mergeStrategy in assembly) { old ⇒
        {
          case x if x startsWith "org/eclipse/swt" ⇒ MergeStrategy.first
          case x ⇒ old(x)
        }
      }
    ) ++
    addZipJar(Dependencies.Compile.Jzy3dDeps, Compile)
  )

  lazy val fixedPoints = Project(
    id = "bidiatool-fixed-points",
    base = file("fixedPoints"),
    settings = commonSettings ++ 
    Seq(
      libraryDependencies ++= Seq(
        Dependencies.Compile.Cilib,
        Dependencies.Compile.Simulator
      )
    )
  ) dependsOn(parsing, plotting, stability)

  lazy val parsing = Project(
    id = "bidiatool-parsing",
    base = file("parsing"),
    settings = commonSettings
  )

  // Este proyecto hay que actualizarlo para que dependa de BreezeMath y no de Scalala
  // Scalala has been superseded by Breeze
  // lazy val stability = Project(
  //   id = "bidiatool-stability",
  //   base = file("stability"),
  //   settings = commonSettings ++
  //   Seq(
  //     resolvers ++= Seq("Clojars Repository" at "http://clojars.org/repo"),
  //     libraryDependencies ++= Seq(
  //       "org.scalala" %% "scalala" % "1.0.0.RC3-SNAPSHOT",
  //       "de.sciss" %% "scalainterpreterpane" % "0.21"
  //     )
  //   )
  // ) 

  lazy val stability = Project(
    id = "bidiatool-stability",
    base = file("stability"),
    settings = commonSettings
  ) dependsOn(parsing)


  lazy val formatSettings = scalariformSettings ++ Seq(
    ScalariformKeys.preferences in Compile := formattingPreferences,
    ScalariformKeys.preferences in Test    := formattingPreferences
  )

  def formattingPreferences = {
    import scalariform.formatter.preferences._
    FormattingPreferences()
    .setPreference(RewriteArrowSymbols, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
  }

  def commonSettings = 
    Defaults.defaultSettings ++ 
    formatSettings ++
    Seq(
      organization := "mx.umich.fie.dep",
      scalaVersion := "2.9.2",
      scalacOptions ++= Seq("-unchecked", "-deprecation"),
      libraryDependencies ++= Seq(
        Dependencies.Compile.ScalazCore,
        Dependencies.Compile.BreezeMath,
        Dependencies.Test.Specs2,
        Dependencies.Test.ScalaCheck,
        Dependencies.Test.Mockito,
        Dependencies.Test.Hamcrest
      ),
      resolvers ++= Seq(
        "Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
        "Releases"  at "http://oss.sonatype.org/content/repositories/releases"
      )
    ) ++
    assemblySettings

  object Dependencies {
    //val arch = "macosx" // "windows-amd64" "windows-i586" "linux-amd64" "linux-i586"

    object Compile {
      val Config = "com.typesafe" % "config" % "0.5.2"
      val ScalazCore = "org.scalaz" %% "scalaz-core" % "7.0.0-M3" cross CrossVersion.full
      val Swing = "org.scala-lang" % "scala-swing" % "2.9.2"
      val BreezeMath = "org.scalanlp" %% "breeze-math" % "0.1"
      val BreezeViz  = "org.scalanlp" %% "breeze-viz" % "0.1"
      val Jzy3d = "org.jzy3d" % "jzy3d" % "0.9" from "http://www.jzy3d.org/release/0.9/org.jzy3d-0.9.jar"
      val Jzy3dDeps = "org.jzy3d" % "jzy3d-deps" % "0.9" from "http://www.jzy3d.org/release/0.9/org.jzy3d-0.9-dependencies.zip"
      val Cilib = "net.cilib" %% "library" % "0.7.5"
      val Simulator = "simulator" %% "simulator" % "0.7.5"
    }

    object Test {
      val Specs2 = "org.specs2" %% "specs2" % "1.12.1" % "test" cross CrossVersion.full
      val ScalaCheck = "org.scalacheck" %% "scalacheck" % "1.10.0" % "test" cross CrossVersion.full
      val Mockito = "org.mockito" % "mockito-all" % "1.9.0" % "test"
      val Hamcrest = "org.hamcrest" % "hamcrest-all" % "1.1" % "test"
    }
  }
}

object AddZipJar extends Plugin {

  /** Adds a dependency on a ZIP file, and modifies `unmanagedJars` to include the contents */
  def addZipJar(module: ModuleID, config: Configuration) = Seq[Project.Setting[_]](
    libraryDependencies += module,
    unmanagedJars in config <++= (update, cacheDirectory, target) map {
      (updateReport, cache, target) =>
      val moduleReports = updateReport.configuration(config.name).get.modules
      moduleReports.find(mr => (mr.module.organization, mr.module.name) == (module.organization, module.name)) match {
	case Some(x) =>
	val zipFile = x.artifacts.head._2	
	val cachedUnzip = FileFunction.cached(cache / "zipJar", inStyle = FilesInfo.lastModified, outStyle = FilesInfo.exists) { (in: Set[File]) =>
          IO.unzip(in.head, target)
	}
	cachedUnzip(Set(zipFile)).toSeq
	case None =>
	sys.error("could not find artifact [%s] in [%s]".format(module, moduleReports.map(_.module).mkString("\n")))
      }
    }
  )
}
