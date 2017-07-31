package io.findify.scalapacked

import org.scalatest.{FlatSpec, Matchers}

class StructMapImplTest extends FlatSpec with Matchers {
  import codec._
  "map" should "add elements" in {
    val map = new StructMapImpl[String,String](1024)
    map.put("foo", "bar")
    map.get("foo") shouldBe Some("bar")
  }

  it should "deal with collisions" in {
    val map = new StructMapImpl[String,String](1024)
    Range(1,512).foreach(i => map.put(i.toString, i.toString))
    val results = Range(1,512).map(i => map.get(i.toString).get)
    results.size shouldBe 511
    Range(1,512).zip(results).foreach(x => x._1.toString shouldBe x._2)
  }

  it should "rebuild on full" in {
    val map = new StructMapImpl[String,String](8)
    Range(1,512).foreach(i => map.put(i.toString, i.toString))
    val results = Range(1,512).map(i => map.get(i.toString).get)
    results.size shouldBe 511

  }
}
