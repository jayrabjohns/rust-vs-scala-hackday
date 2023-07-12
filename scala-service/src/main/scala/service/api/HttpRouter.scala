package service.api

import cats.effect.Async
import fs2.kafka.KafkaProducer
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class HttpRouter[F[_]: Async](
  kafkaProducer: KafkaProducer[F, Unit, Array[Byte]])
  extends Http4sDsl[F] {
  private val produceEventRoute = new ProduceEventRoute[F](kafkaProducer)

  val service: HttpRoutes[F] = HttpRoutes.of(produceEventRoute.route)
}
