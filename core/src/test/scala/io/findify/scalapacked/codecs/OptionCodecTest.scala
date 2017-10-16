package io.findify.scalapacked.codecs

import io.findify.scalapacked.types.{Codec, OptionCodec, StringCodec}
import org.scalatest.{FlatSpec, Matchers}

class OptionCodecTest extends FlatSpec with Matchers with CodecTest {

  import io.findify.scalapacked.codec._
  import io.findify.scalapacked.codec.generic._
  "option codec" should "roundtrip for None" in {
    val x: Option[String] = None
    roundtrip(x) shouldBe None
  }

  it should "work with Option[String]" in {
    val x: Option[String] = Some("foo")
    roundtrip(x) shouldBe Some("foo")
  }

  it should "work with contravariant types" in {
    roundtrip(None) shouldBe None
  }
}
