package service

import cats.effect._
import fs2.kafka.{Acks, KafkaProducer, ProducerSettings, Serializer}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import service.api.HttpRouter

import scala.concurrent.duration.DurationInt

object Main extends IOApp {

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  def run(args: List[String]): IO[ExitCode] = {
    kafkaProducer.use { producer =>
      for {
        _ <- logger.info("Starting scala-service...")
        _ <- new ServerStream[IO](producer)
          .stream()
          .compile
          .drain
      } yield ExitCode.Success
    }
  }

  private def kafkaProducer = {
   val settings: ProducerSettings[IO, Unit, Array[Byte]] =
     ProducerSettings.apply(Serializer[IO, Unit], Serializer[IO,Array[Byte]])
       .withBootstrapServers("localhost:9092")
       .withAcks(Acks.One)
    KafkaProducer.resource(settings)
  }

  class ServerStream[F[_]: Async](
    kafkaProducer: KafkaProducer[F, Unit, Array[Byte]]) {
    def stream(): fs2.Stream[F, ExitCode] = {
      val router: HttpRouter[F] = new HttpRouter[F](kafkaProducer)

      val app = Router("" -> router.service).orNotFound

//      val appWithMiddlewares =
//        ResponseLogger.httpApp(logHeaders = true, logBody = false)(
//          RequestLogger.httpApp(logHeaders = true, logBody = false)(app))

      BlazeServerBuilder
        .apply[F]
        .bindHttp(7878, "0.0.0.0")
        .withHttpApp(app)
        .withResponseHeaderTimeout(59 seconds)
        .serve
    }
  }
}
