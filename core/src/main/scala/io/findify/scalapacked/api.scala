package io.findify.scalapacked
import io.findify.scalapacked.types._

object api {
  implicit val stringCodec: Codec[String] = StringCodec
  implicit val intCodec: Codec[Int] = IntCodec
  implicit val floatCodec: Codec[Float] = FloatCodec
}
