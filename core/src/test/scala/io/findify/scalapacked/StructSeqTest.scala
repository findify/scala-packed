package io.findify.scalapacked

import io.findify.scalapacked.StructSeq.StructCanBuildFrom
import io.findify.scalapacked.example.{Foo, FooCodec}
import org.scalatest.{FlatSpec, Matchers}


/**
  * Created by shutty on 11/19/16.
  */


class StructSeqTest extends FlatSpec with Matchers {
  implicit val encoder = new FooCodec()
  val buf = Range(0, 10).map(r => Foo(r, r.toFloat, r.toString)).to[StructSeq](new StructCanBuildFrom[Foo]())

  "case class with int" should "convert from normal seq" in {
    buf.size shouldBe 10
    buf.head shouldBe Foo(0, 0.0f, "0")
    buf.last shouldBe Foo(9, 9.0f, "9")
    buf.map(_.i1).sum shouldBe 45
    buf.map(_.s).mkString shouldBe "0123456789"
  }

  it should "construct seq from element vararg" in {
    val buf2 = StructSeq(Foo(111, 111.0f, "xxx"))
    buf2.head.i1 shouldBe 111
    val buf2a = StructSeq(Foo(111, 111.0f, "xxx"), Foo(222, 222.0f, "yyy"))
    buf2a.size shouldBe 2
  }

  it should "append elements to list" in {
    val buf2 = StructSeq(Foo(111, 111.0f, "xxx"))
    val buf3 = buf ++ buf2
    buf3.size shouldBe 11
  }

}
