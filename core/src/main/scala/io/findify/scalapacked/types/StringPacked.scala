package io.findify.scalapacked.types

import java.nio.ByteBuffer

import io.findify.scalapacked.pool.MemoryPool

/**
  * Created by shutty on 11/22/16.
  */
object StringPacked extends PackedType[String] {
  override def size(value:String): Int = value.getBytes.length + 4
  override def write(value:String, buffer: MemoryPool): Int = {
    val stringBuffer = value.getBytes
    val offset = buffer.writeInt(stringBuffer.length)
    buffer.writeBytes(stringBuffer)
    offset
  }
  override def read(buffer: MemoryPool, offset: Int): String = {
    val len = buffer.readInt(offset)
    val stringBuffer = buffer.readBytes(offset + 4, len)
    new String(stringBuffer)
  }
}