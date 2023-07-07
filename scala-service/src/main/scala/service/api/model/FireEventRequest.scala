package service.api.model

import cats.effect.Concurrent
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

case class FireEventRequest(reason: String)

object FireEventRequest {
  implicit val codec: Codec[FireEventRequest] = deriveCodec
  implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, FireEventRequest] = jsonOf
}