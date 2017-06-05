package io.findify.scalapacked

import java.nio.ByteBuffer

import com.typesafe.scalalogging.LazyLogging
import io.findify.scalapacked.StructSeq.StructCanBuildFrom
import io.findify.scalapacked.pool.{HeapPool, MemoryPool}
import io.findify.scalapacked.types.{FloatPacked, IntPacked, PackedType, StringPacked}
import org.scalatest.{FlatSpec, Matchers}
import shapeless._

import scala.collection.generic.CanBuildFrom
import scala.collection.{TraversableLike, mutable}

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


case class Foo(i1: Int, f2: Float, s: String) extends Struct

class FooEncoder extends Encoder[Foo] with LazyLogging {
  override def write(value: Foo, buffer: MemoryPool): Int = {
    val offset = buffer.size
    val size = IntPacked.size(value.i1) + FloatPacked.size(value.f2) + StringPacked.size(value.s) + 4
    IntPacked.write(size, buffer)
    IntPacked.write(value.i1, buffer)
    FloatPacked.write(value.f2, buffer)
    StringPacked.write(value.s, buffer)
    logger.info(s"wrote $size bytes at offset $offset")
    offset
  }
}

class FooDecoder extends Decoder[Foo] {
  override def read(buffer: MemoryPool, offset: Int): Foo = {
    val i1 = IntPacked.read(buffer, offset + 4)
    val f2 = FloatPacked.read(buffer, offset + 4 + IntPacked.size(i1))
    val s = StringPacked.read(buffer, offset + 4 + IntPacked.size(i1) + FloatPacked.size(f2))
    Foo(i1, f2, s)
  }

  override def size(buffer: MemoryPool, offset: Int): Int = {
    IntPacked.read(buffer, offset)
  }
}



class SimpleTest extends FlatSpec with Matchers {

  "case class with int" should "be written to buffer" in {
    val in = Foo(17, 24.1f, "123")
    in.i1 shouldBe 17
    in.f2 shouldBe 24.1f
    in.s shouldBe "123"
  }

  it should "convert from normal seq" in {
    implicit val encoder = new FooEncoder()
    implicit val decoder = new FooDecoder()
    implicit def cbf = new StructCanBuildFrom[Foo]()
    val buf = Range(0, 10).map(r => Foo(r, r.toFloat, r.toString))
    buf.size shouldBe 10
    buf.head shouldBe Foo(0, 0.0f, "0")
    buf.last shouldBe Foo(9, 9.0f, "9")
    buf.map(_.i1).sum shouldBe 45
    buf.map(_.s).mkString shouldBe "0123456789"
  }

}
