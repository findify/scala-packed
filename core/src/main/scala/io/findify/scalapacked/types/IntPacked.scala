package io.findify.scalapacked.types

import java.nio.ByteBuffer

/**
  * Created by shutty on 11/22/16.
  */
object IntPacked extends PackedType[Int] {
  override def size(value:Int): Int = 4
  override def write(value:Int, buffer: ByteBuffer): Unit = buffer.putInt(value)
  override def read(buffer: ByteBuffer, offset: Int): Int = {
    buffer.getInt(offset)
  }
}