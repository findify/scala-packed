package io.findify.scalapacked.example

import com.typesafe.scalalogging.LazyLogging
import io.findify.scalapacked.Codec
import io.findify.scalapacked.pool.MemoryPool
import io.findify.scalapacked.types._

case class Foo(i1: Int, f2: Float, s: String)

class FooCodec extends Codec[Foo] with LazyLogging {
  override def write(value: Foo, buffer: MemoryPool): Int = {
    val offset = buffer.size
    val size = IntCodec.size(value.i1) + FloatCodec.size(value.f2) + StringCodec.size(value.s) + 4
    IntCodec.write(size, buffer)
    IntCodec.write(value.i1, buffer)
    FloatCodec.write(value.f2, buffer)
    StringCodec.write(value.s, buffer)
    logger.info(s"wrote $size bytes at offset $offset")
    offset
  }
  override def read(buffer: MemoryPool, offset: Int): Foo = {
    val i1 = IntCodec.read(buffer, offset + 4)
    val f2 = FloatCodec.read(buffer, offset + 4 + IntCodec.size(i1))
    val s = StringCodec.read(buffer, offset + 4 + IntCodec.size(i1) + FloatCodec.size(f2))
    Foo(i1, f2, s)
  }

  override def size(buffer: MemoryPool, offset: Int): Int = {
    IntCodec.read(buffer, offset)
  }

  override def size(item: Foo): Int = ???
}

