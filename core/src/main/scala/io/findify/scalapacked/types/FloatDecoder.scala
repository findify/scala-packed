package io.findify.scalapacked.types

import io.findify.scalapacked.Decoder
import io.findify.scalapacked.pool.MemoryPool

object FloatDecoder extends Decoder[Float] {
  override def read(buffer: MemoryPool, offset: Int): Float = buffer.readFloat(offset)
  override def size(buffer: MemoryPool, offset: Int): Int = 4
  override def size(item: Float): Int = 4
}
