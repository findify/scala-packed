package io.findify.scalapacked

import io.findify.scalapacked.example.{Foo, FooCodec}
import io.findify.scalapacked.immutable.PackedMap
import org.scalatest.{FlatSpec, Matchers}

class PackedMapTest extends FlatSpec with Matchers {
  implicit val foocodec = new FooCodec()
  import codec._
  val map = PackedMap("x" -> Foo(1, 1.0f, "x"))


  "map" should "have size" in {
    map.size shouldBe 1
  }

  it should "contain element" in {
    map.contains("x") shouldBe true
  }

  it should "extract element" in {
    map.get("x") shouldBe Some(Foo(1, 1.0f, "x"))
    map.get("y") shouldBe None
  }
}
