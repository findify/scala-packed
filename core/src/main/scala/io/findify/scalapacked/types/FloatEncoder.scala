package io.findify.scalapacked.types

import io.findify.scalapacked.Encoder
import io.findify.scalapacked.pool.MemoryPool

object FloatEncoder extends Encoder[Float] {
  override def write(value: Float, buffer: MemoryPool): Int = buffer.writeFloat(value)
}
