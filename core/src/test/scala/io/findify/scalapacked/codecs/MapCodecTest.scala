package io.findify.scalapacked.codecs

import io.findify.scalapacked.types.{Codec, MapCodec, StringCodec}
import org.scalatest.{FlatSpec, Matchers}



class MapCodecTest extends FlatSpec with Matchers with CodecTest {
  import io.findify.scalapacked.codec._
  import io.findify.scalapacked.codec.generic._
  "map codec" should "roundtrip" in {
    roundtrip(Map("foo" -> "bar")) shouldBe Map("foo" -> "bar")
  }

  it should "work with complex values" in {
    case class Complex(s: String, i: Int)
    val orig = Map("foo" -> Complex("bar", 1), "baz" -> Complex("qux", 2))
    roundtrip(orig) shouldBe orig
  }

}
