package io.findify.scalapacked.codecs

import io.findify.scalapacked.types.DoubleCodec
import org.scalatest.{FlatSpec, Matchers}

class DoubleCodecTest extends FlatSpec with Matchers with CodecTest {
  implicit val doubleCodec = DoubleCodec
  "codec for doubles" should "convert 0.0" in {
    roundtrip(0.0) shouldBe 0.0
  }

  it should "convert NaN" in {
    val value = roundtrip(Double.NaN)
    java.lang.Double.isNaN(value) shouldBe true
  }
}
