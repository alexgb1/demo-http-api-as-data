package org.example

import cats.effect.{ExitCode, IO, IOApp}
import org.example.model.Appointment
import org.http4s.HttpRoutes
import sttp.model.StatusCode
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.swagger.http4s.SwaggerHttp4s

object DemoServerApp extends IOApp {

  /**
   * Define API
   **/

  import io.circe.generic.auto._
  import sttp.tapir._
  import sttp.tapir.json.circe._

  sealed trait Error

  case class NotFound(message: String) extends Error

  val getAppointmentById: Endpoint[String, Error, Appointment, Nothing] = endpoint
    .in("appointments")
    .in(query[String]("id"))
    .out(jsonBody[Appointment])
    .errorOut(
      oneOf[Error](
        statusMapping(StatusCode.NotFound, jsonBody[NotFound])
      )
    )

  /**
   * Define API docs and route
   **/

  import sttp.tapir.docs.openapi._
  import sttp.tapir.openapi.circe.yaml._

  val docs: OpenAPI = getAppointmentById.toOpenAPI("My API", "1")
  val docsRoute: HttpRoutes[IO] = new SwaggerHttp4s(docs.toYaml).routes

  /**
   * Convert to http4s route and add business logic
   **/

  import sttp.tapir.server.http4s._

  val getAppointment: String => IO[Either[Error, Appointment]] =
    id => IO {
      Appointment.appointments.get(id).toRight(NotFound("Appointment is not found"))
    }

  val route: HttpRoutes[IO] = getAppointmentById.toRoutes(getAppointment)

  /**
   * Integrate with the http server
   **/

  import cats.syntax.semigroupk._
  import org.http4s.implicits._
  import org.http4s.server.blaze.BlazeServerBuilder

  import scala.concurrent.ExecutionContext.global


  override def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO](global)
      .bindHttp(8081, "localhost")
      .withHttpApp((route <+> docsRoute).orNotFound)
      .serve
      .compile
      .drain
      .map(_ => ExitCode.Success)
  }
}
