package io.findify.scalapacked.example

import io.findify.scalapacked.PackedSeq
import org.scalatest.{FlatSpec, Matchers}

class ReadmeExample extends FlatSpec with Matchers {
  "readme example" should "work" in {
    // A simple case class to pack
    case class HelloPacked(i: Int, l: Long, s: String)

    // codecs for default scala types
    import io.findify.scalapacked.codec._
    // shapeless-based codec auto-deriver for case classes
    import io.findify.scalapacked.codec.generic._

    // a sequence of 1k objects
    val list = (0 to 1000).map(i => HelloPacked(i, i.toLong, i.toString)).to[PackedSeq]

    // use it as a typical scala collection
    list.filter(_.i % 10 == 0)

  }
}
