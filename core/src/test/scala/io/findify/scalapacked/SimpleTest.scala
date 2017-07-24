package io.findify.scalapacked

import com.typesafe.scalalogging.LazyLogging
import io.findify.scalapacked.pool.{HeapPool, MemoryPool}
import io.findify.scalapacked.types.{FloatPacked, IntPacked, PackedType, StringPacked}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by shutty on 11/19/16.
  */

object DoubleFormat {
  implicit val doubleFormat = new PackedType[Double] {
    override def size(value:Double): Int = 8
    override def write(value:Double, buffer: MemoryPool): Int = buffer.writeDouble(value)
    override def read(buffer: MemoryPool, offset: Int): Double = buffer.readDouble(offset)
  }
}


case class Foo(offset: Int) extends AnyVal {
  def i1(implicit buffer: MemoryPool): Int = IntPacked.read(buffer, offset + 4)
  def f2(implicit buffer: MemoryPool): Float = FloatPacked.read(buffer, offset + 4 + IntPacked.size(i1))
  def s(implicit buffer: MemoryPool): String = StringPacked.read(buffer, offset + 4 + IntPacked.size(i1) + FloatPacked.size(f2))
}


class FooCodec extends Codec[Foo] {
  override def build(offset: Int): Foo = new Foo(offset)
}
case object Foo extends LazyLogging  {
  def apply(i1: Int, f2: Float, s: String)(implicit buffer: MemoryPool): Foo = {
    val offset = buffer.size
    val size = IntPacked.size(i1) + FloatPacked.size(f2) + StringPacked.size(s) + 4
    IntPacked.write(size, buffer)
    IntPacked.write(i1, buffer)
    FloatPacked.write(f2, buffer)
    StringPacked.write(s, buffer)
    logger.info(s"wrote $size bytes at offset $offset")
    new Foo(offset)
  }

}


class SimpleTest extends FlatSpec with Matchers {

  "case class with int" should "be written to buffer" in {
    implicit val pool = new HeapPool()
    val in = Foo(17, 24.1f, "123")
    in.i1 shouldBe 17
    in.f2 shouldBe 24.1f
    in.s shouldBe "123"
  }

  it should "convert from normal seq" in {
    implicit val pool = new HeapPool()
    implicit val codec = new FooCodec()
    val buf = Range(0, 10).foreach(r => Foo(r, r.toFloat, r.toString))
    val seq = new StructBuffer[Foo](pool)
    seq.size shouldBe 10
    seq.foreach(x => { x.i1.toString shouldBe x.s})
  }

}
