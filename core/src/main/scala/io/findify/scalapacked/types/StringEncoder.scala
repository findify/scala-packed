package io.findify.scalapacked.types

import io.findify.scalapacked.Encoder
import io.findify.scalapacked.pool.MemoryPool

object StringEncoder extends Encoder[String] {
  override def write(value: String, buffer: MemoryPool): Int = {
    val stringBuffer = value.getBytes
    val offset = buffer.writeInt(stringBuffer.length)
    buffer.writeBytes(stringBuffer)
    offset
  }
}
