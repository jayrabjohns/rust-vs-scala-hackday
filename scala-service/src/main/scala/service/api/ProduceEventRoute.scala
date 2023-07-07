package service.api

import cats.data.EitherT
import cats.effect.Async
import cats.implicits._
import fs2.kafka.{KafkaProducer, ProducerRecord, ProducerRecords}
import io.circe.syntax.EncoderOps
import org.http4s.dsl.Http4sDsl
import org.http4s.{Request, Response}
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import service.api.model.{Event, FireEventRequest}

class ProduceEventRoute[F[_]: Async](kafkaProducer: KafkaProducer[F, Unit, Array[Byte]])
  extends Http4sDsl[F] {

  private val logger: SelfAwareStructuredLogger[F] = Slf4jLogger.getLogger[F] // bad

  val route: PartialFunction[Request[F], F[Response[F]]] = {
    case request @ POST -> Root / "fire" / id =>
      (for {
        fireEventRequest <- request
          .attemptAs[FireEventRequest]
          .leftMap[String](e => e.message)
        idLong = id.toLong // bad
        _ <- EitherT.right[String](produceEvent(idLong, fireEventRequest.reason))
      } yield ()).value.flatMap {
        case Right(_) =>
          Ok()
        case Left(_) => InternalServerError()
      }
  }


  def produceEvent(id: Long, reason: String): F[Unit] = {
    val event = Event(id, reason)
    val record = ProducerRecord("testing-topic", (), event.asJson.noSpaces.getBytes())
    logger.info(s"Firing event with id $id because $reason") *>
      kafkaProducer.produce(ProducerRecords.one(record)).flatten.void
  }
}
