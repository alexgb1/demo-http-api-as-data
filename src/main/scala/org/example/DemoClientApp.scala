package org.example

import cats.effect.{ExitCode, IO, IOApp}

object DemoClientApp extends IOApp {

  /**
   * 1. Define the http request
   **/

  import DemoServerApp.getAppointmentById
  import sttp.client._
  import sttp.tapir.client.sttp._

  val request = getAppointmentById.toSttpRequest(uri"http://localhost:8081")

  /**
   * 2. Create the http client as a resource
   *
   * Obs. Use the global execution context. (For PROD, use a separate execution context for Blocker)
   **/

  import cats.effect.Blocker
  import org.http4s.client.blaze.BlazeClientBuilder
  import sttp.client.http4s.Http4sBackend

  import scala.concurrent.ExecutionContext.global

  val httpClientResource = Http4sBackend.usingClientBuilder(
    BlazeClientBuilder[IO](global),
    Blocker.liftExecutionContext(global)
  )

  def consoleLog(message: String) = IO.delay(println(message))

  /**
   * 3. Execute the request
   **/

  override def run(args: List[String]): IO[ExitCode] = {
    httpClientResource.use {
      client =>
        client.send(request("1"))
          .flatMap(response => consoleLog(s"Received: ${response.body}"))
          .as(ExitCode.Success)
    }
  }
}
