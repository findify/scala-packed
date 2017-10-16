package io.findify.scalapacked.types

import io.findify.scalapacked.pool.MemoryPool

object FloatCodec extends Codec[Float] {
  override def read(buffer: MemoryPool, offset: Int): Float = buffer.readFloat(offset)
  override def size(buffer: MemoryPool, offset: Int): Int = 4
  override def size(item: Float): Int = 4
  override def write(value: Float, buffer: MemoryPool): Int = buffer.writeFloat(value)
}
