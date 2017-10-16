package io.findify.scalapacked.types

import io.findify.scalapacked.pool.MemoryPool

object DoubleCodec extends Codec[Double] {
  override def read(buffer: MemoryPool, offset: Int): Double = buffer.readDouble(offset)
  override def size(buffer: MemoryPool, offset: Int): Int = 8
  override def size(item: Double): Int = 8
  override def write(value: Double, buffer: MemoryPool): Int = buffer.writeDouble(value)
}
