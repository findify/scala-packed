package io.findify.scalapacked.types

import io.findify.scalapacked.{Decoder, Encoder}
import io.findify.scalapacked.pool.MemoryPool

object IntEncoder extends Encoder[Int]{
  override def write(value: Int, buffer: MemoryPool): Int = buffer.writeInt(value)
}
