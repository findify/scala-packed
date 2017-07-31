package io.findify.scalapacked.codecs

import io.findify.scalapacked.types.StringCodec
import org.scalatest.{FlatSpec, Matchers}

class StringCodecTest extends FlatSpec with Matchers with CodecTest {
  implicit val stringCodec = StringCodec
  "string codec" should "convert empty string" in {
    roundtrip("") shouldBe ""
  }

  it should "get correct size for unpacked item" in {
    size("foo") shouldBe 7
  }

  it should "get correct size for packed item" in {
    sizeSerialized("foo") shouldBe 7
  }
}
