package io.findify.scalapacked.types

import java.nio.ByteBuffer

/**
  * Created by shutty on 11/22/16.
  */
object LongPacked extends PackedType[Long] {
  override def size(value:Long): Int = 8
  override def write(value:Long, buffer: ByteBuffer): Unit = buffer.putLong(value)
  override def read(buffer: ByteBuffer, offset: Int): Long = buffer.getLong(offset)
}