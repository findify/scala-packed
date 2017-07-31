package io.findify.scalapacked.codecs

import io.findify.scalapacked.types.{Codec, OptionCodec, StringCodec}
import org.scalatest.{FlatSpec, Matchers}

class OptionCodecTest extends FlatSpec with Matchers with CodecTest {
  implicit val stringCodec: Codec[String] = StringCodec
  implicit val noneCodec = OptionCodec.NoneCodec
  implicit def someCodec[T](implicit cdc: Codec[T]) = new OptionCodec.SomeCodec[T]()(cdc)

  "option codec" should "roundtrip for None" in {
    roundtrip(None) shouldBe None
  }

  it should "work with Option[String]" in {
    roundtrip(Some("foo")) shouldBe Some("foo")
  }
}
