package io.findify.scalapacked.codecs

import org.scalatest.{FlatSpec, Matchers}

class SetCodecTest extends FlatSpec with Matchers with CodecTest {

  import io.findify.scalapacked.codec._
  import io.findify.scalapacked.codec.generic._

  "set codec" should "work with simple sets" in {
    roundtrip(Set("foo", "bar")) shouldBe Set("foo", "bar")
  }

  it should "check for inclusion" in {
    roundtrip(Set("foo", "bar", "baz")).contains("bar") shouldBe true
  }
}
