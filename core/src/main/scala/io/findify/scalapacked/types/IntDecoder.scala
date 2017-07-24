package io.findify.scalapacked.types

import io.findify.scalapacked.Decoder
import io.findify.scalapacked.pool.MemoryPool

object IntDecoder extends Decoder[Int] {
  override def read(buffer: MemoryPool, offset: Int): Int = buffer.readInt(offset)
  override def size(buffer: MemoryPool, offset: Int): Int = 4
  override def size(item: Int): Int = 4
}
