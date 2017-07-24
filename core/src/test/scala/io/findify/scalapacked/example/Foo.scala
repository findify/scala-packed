package io.findify.scalapacked.example

import com.typesafe.scalalogging.LazyLogging
import io.findify.scalapacked.{Decoder, Encoder}
import io.findify.scalapacked.pool.MemoryPool
import io.findify.scalapacked.types._

case class Foo(i1: Int, f2: Float, s: String)

class FooEncoder extends Encoder[Foo] with LazyLogging {
  override def write(value: Foo, buffer: MemoryPool): Int = {
    val offset = buffer.size
    val size = IntDecoder.size(value.i1) + FloatDecoder.size(value.f2) + StringDecoder.size(value.s) + 4
    IntEncoder.write(size, buffer)
    IntEncoder.write(value.i1, buffer)
    FloatEncoder.write(value.f2, buffer)
    StringEncoder.write(value.s, buffer)
    logger.info(s"wrote $size bytes at offset $offset")
    offset
  }
}

class FooDecoder extends Decoder[Foo] {
  override def read(buffer: MemoryPool, offset: Int): Foo = {
    val i1 = IntDecoder.read(buffer, offset + 4)
    val f2 = FloatDecoder.read(buffer, offset + 4 + IntDecoder.size(i1))
    val s = StringDecoder.read(buffer, offset + 4 + IntDecoder.size(i1) + FloatDecoder.size(f2))
    Foo(i1, f2, s)
  }

  override def size(buffer: MemoryPool, offset: Int): Int = {
    IntDecoder.read(buffer, offset)
  }

  override def size(item: Foo): Int = ???
}
