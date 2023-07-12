package service.api.model

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class Event(id: Long, reason: String)

object Event {
  implicit val encoder: Encoder[Event] = deriveEncoder
}
