package io.findify.scalapacked

import java.nio.ByteBuffer

import io.findify.scalapacked.types.{IntPacked, LongPacked}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by shutty on 11/19/16.
  */


class Foo(var buffer: ByteBuffer, var offset: Int) extends PackedProduct {
  def this() = this(ByteBuffer.allocate(0), 0)
  def i1: Int = IntPacked.read(buffer, offset + 0)
  def i2: Long = LongPacked.read(buffer, offset + IntPacked.size(i1))
  def size = IntPacked.size(i1) + LongPacked.size(i2)
}

object Foo {
  def apply(i1: Int, i2: Long) = {
    val buffer = ByteBuffer.allocate(IntPacked.size(i1) + LongPacked.size(i2))
    IntPacked.write(i1, buffer)
    LongPacked.write(i2, buffer)
    new Foo(buffer, 0)
  }
}

class SimpleTest extends FlatSpec with Matchers {
  "case class with int" should "be written to bytebuffer" in {
    val in = Foo(17, 24)
    val buff = ByteBuffer.allocate(in.size)
    buff.put(in.buffer.array(), 0, in.buffer.capacity())
    val result1 = buff.getInt(0)
    result1 shouldBe 17
    val result2 = buff.getLong(4)
    result2 shouldBe 24
  }

  it should "not fail on empty instance" in {
    val empty = new Foo()
    empty.offset shouldBe 0
  }

  it should "be insertable to PackedSeq" in {
    val seq = PackedSeq(Seq(Foo(1, 2), Foo(3, 4)))
    var count = 0
    seq.foreach { item => {
      count += item.i1
      count += item.i2.toInt
    }}
    count shouldBe 10
  }

  it should "map thru elements" in {
    val seq = PackedSeq(Seq(Foo(1, 2), Foo(3, 4))).map(item => Foo(item.i1 * 2, item.i2 * 2))
    var count = 0
    seq.foreach { item => {
      count += item.i1
      count += item.i2.toInt
    }}
    count shouldBe 20
  }
}
