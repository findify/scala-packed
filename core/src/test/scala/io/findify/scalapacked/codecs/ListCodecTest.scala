package io.findify.scalapacked.codecs

import org.scalatest.{FlatSpec, Matchers}

class ListCodecTest extends FlatSpec with Matchers with CodecTest {
  import io.findify.scalapacked.codec._
  "seq codec" should "work" in {
    roundtrip(List(1,2,3)) shouldBe List(1,2,3)
  }

  it should "work with empty seq" in {
    roundtrip(List.empty[Int]) shouldBe List.empty[Int]
  }

}
