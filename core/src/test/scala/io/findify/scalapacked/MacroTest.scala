package io.findify.scalapacked

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by shutty on 11/21/16.
  */

@Packed class Bar(a:Int)

class MacroTest extends FlatSpec with Matchers {
  "packed class" should "have size method" in {
    val bar = new Bar(1)
    bar.size shouldBe 4
  }
}
