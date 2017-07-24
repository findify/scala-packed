package io.findify.scalapacked

import io.findify.scalapacked.StructSeq.StructCanBuildFrom
import io.findify.scalapacked.example.{Foo, FooDecoder, FooEncoder}
import org.scalatest.{FlatSpec, Matchers}

class StructMapTest extends FlatSpec with Matchers {
  implicit val encoder = new FooEncoder()
  implicit val decoder = new FooDecoder()
  import api._
  val map = StructMap("x" -> Foo(1, 1.0f, "x"))


  "map" should "have size" in {
    map.size shouldBe 1
  }

  it should "contain element" in {
    map.contains("x") shouldBe true
  }

  it should "extract element" in {
    map.get("x") shouldBe Some(Foo(1, 1.0f, "x"))
  }
}
