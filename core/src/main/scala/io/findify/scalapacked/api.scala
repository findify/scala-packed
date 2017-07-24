package io.findify.scalapacked
import io.findify.scalapacked.pool.MemoryPool
import io.findify.scalapacked.types._

object api {
  implicit val stringDecoder: Decoder[String] = StringDecoder
  implicit val stringEncoder: Encoder[String] = StringEncoder
  implicit val intDecoder: Decoder[Int] = IntDecoder
  implicit val intEncoder: Encoder[Int] = IntEncoder
  implicit val floatDecoder: Decoder[Float] = FloatDecoder
  implicit val floatEncoder: Encoder[Float] = FloatEncoder
}
