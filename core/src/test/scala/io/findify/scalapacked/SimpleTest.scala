package io.findify.scalapacked

import java.nio.ByteBuffer

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by shutty on 11/19/16.
  */


class Foo(var buffer: ByteBuffer, var offset: Int) extends PackedProduct {
  def this() = this(ByteBuffer.allocate(0), 0)
  import io.findify.scalapacked.types.DefaultTypes._
  def i1: Int = PackedMember.memberRead[Int](buffer, offset + 0)
  def i2: Long = PackedMember.memberRead[Long](buffer, offset + PackedMember.memberSize(i1))
  def size = PackedMember.memberSize(i1) + PackedMember.memberSize(i2)
}

object Foo {
  def apply(i1: Int, i2: Long) = {
    import io.findify.scalapacked.types.DefaultTypes._
    val i1size = PackedMember.memberSize(i1)
    val i2size = PackedMember.memberSize(i2)
    val buffer = ByteBuffer.allocate(i1size + i2size)
    val i1offset = 0
    PackedMember.memberWrite(i1, buffer)
    val i2offset = i1offset + i1size
    PackedMember.memberWrite(i2, buffer)
    new Foo(buffer, 0)
  }
}

class SimpleTest extends FlatSpec with Matchers {
  "case class with int" should "be written to bytebuffer" in {
    val in = Foo(17, 24)
    val buff = ByteBuffer.allocate(in.size)
    buff.put(in.buffer)
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
