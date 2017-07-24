package io.findify.scalapacked.types

import com.typesafe.scalalogging.LazyLogging
import io.findify.scalapacked.Decoder
import io.findify.scalapacked.pool.MemoryPool

object StringDecoder extends Decoder[String] with LazyLogging {
  override def read(buffer: MemoryPool, offset: Int): String = {
    val len = buffer.readInt(offset)
    //logger.debug(s"string len $len")
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
