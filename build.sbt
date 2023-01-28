ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "2.12.16"

val sparkVersion = "3.1.0"
val core = "org.apache.spark" %% "spark-core" % sparkVersion
val sql = "org.apache.spark" %% "spark-sql" % sparkVersion

lazy val root = (project in file("."))
  .settings(
    name := "test_beeline",
    libraryDependencies := Seq(core, sql)
  )
