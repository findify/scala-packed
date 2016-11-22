package io.findify.scalapacked.types

import java.nio.ByteBuffer

/**
  * Created by shutty on 11/22/16.
  */
object StringPacked extends PackedType[String] {
  override def size(value:String): Int = value.getBytes.length + 4
  override def write(value:String, buffer: ByteBuffer): Unit = {
    val stringBuffer = value.getBytes
    buffer.putInt(stringBuffer.length)
    buffer.put(stringBuffer)
  }
  override def read(buffer: ByteBuffer, offset: Int): String = {
    val len = buffer.getInt(offset)
    val stringBuffer = new Array[Byte](len)
    buffer.position(offset + 4)
    buffer.get(stringBuffer)
    new String(stringBuffer)
  }
}