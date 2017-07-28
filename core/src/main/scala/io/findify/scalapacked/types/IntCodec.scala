package io.findify.scalapacked.types

import io.findify.scalapacked.Codec
import io.findify.scalapacked.pool.MemoryPool

object IntCodec extends Codec[Int] {
  override def read(buffer: MemoryPool, offset: Int): Int = buffer.readInt(offset)
  override def size(buffer: MemoryPool, offset: Int): Int = 4
  override def size(item: Int): Int = 4
  override def write(value: Int, buffer: MemoryPool): Int = buffer.writeInt(value)
}
