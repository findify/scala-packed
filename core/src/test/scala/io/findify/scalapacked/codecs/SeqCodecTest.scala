package io.findify.scalapacked.codecs

import org.scalatest.{FlatSpec, Matchers}

class SeqCodecTest extends FlatSpec with Matchers with CodecTest{
  import io.findify.scalapacked.codec._
  "seq codec" should "work" in {
    roundtrip(Seq(1,2,3)) shouldBe Seq(1,2,3)
  }

  it should "work with empty seq" in {
    roundtrip(Seq.empty[Int]) shouldBe Seq.empty[Int]
  }


}
