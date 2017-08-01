package io.findify.scalapacked.example

import io.findify.scalapacked.pool.MemoryPool
import io.findify.scalapacked.types.Codec
import io.findify.scalapacked.{PackedMap, PackedSeq}
import org.scalatest.{FlatSpec, Matchers}

class ReadmeExample extends FlatSpec with Matchers {
  "readme example" should "work with simple example" in {
    // A simple case class to pack
    case class HelloPacked(i: Int, l: Long, s: String)

    // codecs for default scala types
    import io.findify.scalapacked.codec._
    // shapeless-based codec auto-deriver for case classes
    import io.findify.scalapacked.codec.generic._

    // a sequence of 1k objects
    val list = PackedSeq((0 to 1000).map(i => HelloPacked(i, i.toLong, i.toString)): _*)

    // use it as a typical scala collection
    list.count(_.i % 10 == 0) shouldBe 101
  }

  it should "work with complex map example" in {
    // custom type codec
    implicit val byteCodec = new Codec[Byte] {
      // read byte from buffer
      override def read(buffer: MemoryPool, offset: Int): Byte = buffer.readByte(offset)
      // packed object size in buffer
      override def size(buffer: MemoryPool, offset: Int): Int = 1
      // object size
      override def size(item: Byte): Int = 1
      // write object to buffer and return it's offset
      override def write(value: Byte, buffer: MemoryPool): Int = buffer.writeByte(value)
    }

    // case class with cutsom type
    case class NestedFoo(b: Byte)
    case class RootFoo(n: NestedFoo)

    // codecs for default scala types
    import io.findify.scalapacked.codec._
    // shapeless-based codec auto-deriver for case classes
    import io.findify.scalapacked.codec.generic._

    // build a packed map
    val map = PackedMap( (0 to 100).map(i => s"key$i" -> RootFoo(NestedFoo(i.toByte))): _*)

    // use it as a regular one
    map.get("key75").isDefined shouldBe true
  }
}
