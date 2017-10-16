package io.findify.scalapacked.codecs

import io.findify.scalapacked.pool.HeapPool
import io.findify.scalapacked.types.LongCodec
import org.scalatest.{FlatSpec, Matchers}

class LongCodecTest extends FlatSpec with Matchers with CodecTest {
  implicit val longCodec = LongCodec
  "codec for longs" should "convert 0L" in {
    roundtrip(0L) shouldBe 0L
  }

  it should "convert Long.max & Long.min" in {
    roundtrip(Long.MaxValue) shouldBe Long.MaxValue
    roundtrip(Long.MinValue) shouldBe Long.MinValue
  }
}
