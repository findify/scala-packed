package io.findify.scalapacked

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by shutty on 11/22/16.
  */


@Packed class One(a:Int, b:Long, s:String)
@Packed class Two(a:Int)

class PackedSeqTest extends FlatSpec with Matchers {
  val seq = List(One(1,1,"1"), One(2,2,"2"))
  val packed = PackedSeq[One](seq)

  "packed seq" should "be buildable from normal seqs" in {
    packed.byteSize shouldBe 34
  }

  it should "have map method" in {
    val transformed = packed.map(x => One(x.a * 2, x.b * 2, x.s + "1"))
    transformed.byteSize shouldBe 36
  }

  it should "transform one class to another" in {
    val transformed = packed.map(x => Two(x.a * 3))
    transformed.byteSize shouldBe 8
  }
}
