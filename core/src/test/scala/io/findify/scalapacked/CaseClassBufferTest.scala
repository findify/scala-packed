package io.findify.scalapacked

import io.findify.scalapacked.StructSeq.StructCanBuildFrom
import io.findify.scalapacked.example.{Foo, FooDecoder, FooEncoder}
import io.findify.scalapacked.pool.HeapPool
import org.scalatest.{FlatSpec, Matchers}

class CaseClassBufferTest extends FlatSpec with Matchers {
  implicit val encoder = new FooEncoder()
  implicit val decoder = new FooDecoder()
  implicit def cbf = new StructCanBuildFrom[Foo]()

  "case class with int" should "be written to buffer" in {
    val in = Foo(17, 24.1f, "123")
    val pool = new HeapPool()
    encoder.write(in, pool)
    val out = decoder.read(pool, 0)
    out.i1 shouldBe 17
    out.f2 shouldBe 24.1f
    out.s shouldBe "123"
  }

}
