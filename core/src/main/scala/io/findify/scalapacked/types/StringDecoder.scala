package io.findify.scalapacked.types

import io.findify.scalapacked.Decoder
import io.findify.scalapacked.pool.MemoryPool

object StringDecoder extends Decoder[String] {
  override def read(buffer: MemoryPool, offset: Int): String = {
    val len = buffer.readInt(offset)
    val stringBuffer = buffer.readBytes(offset + 4, len)
    new String(stringBuffer)
  }
  override def size(buffer: MemoryPool, offset: Int): Int = {
    buffer.readInt(offset) + 4
  }

  override def size(item: String): Int = {
    4 + item.getBytes.length
  }
}
