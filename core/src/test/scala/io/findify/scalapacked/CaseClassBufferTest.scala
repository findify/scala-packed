package io.findify.scalapacked

import io.findify.scalapacked.StructSeq.StructCanBuildFrom
import io.findify.scalapacked.example.{Foo, FooCodec}
import io.findify.scalapacked.pool.HeapPool
import org.scalatest.{FlatSpec, Matchers}

class CaseClassBufferTest extends FlatSpec with Matchers {
  implicit val codec = new FooCodec()

  "case class with int" should "be written to buffer" in {
    val in = Foo(17, 24.1f, "123")
    val pool = new HeapPool()
    codec.write(in, pool)
    val out = codec.read(pool, 0)
    out.i1 shouldBe 17
    out.f2 shouldBe 24.1f
    out.s shouldBe "123"
  }

}
