name := "demo-http-api-as-data"

version := "0.1"

scalaVersion := "2.13.3"

val tapirVersion = "0.16.15"
val circeVersion = "0.13.0"
val sttpVersion = "2.2.5"

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
  // Http4s
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
  // OpenApi doc
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
  // Expose API doc
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-redoc-http4s" % tapirVersion,
  // Json serialization/de-serialization
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion
) ++ client

val client = Seq(
  "com.softwaremill.sttp.client" %% "core" % sttpVersion,
  "com.softwaremill.sttp.client" %% "http4s-backend" % sttpVersion,
  "com.softwaremill.sttp.client" %% "circe" % sttpVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-sttp-client" % tapirVersion,
  "io.circe" %% "circe-generic" % circeVersion
)