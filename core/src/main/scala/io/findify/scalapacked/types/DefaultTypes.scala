package io.findify.scalapacked.types

import java.nio.ByteBuffer

/**
  * Created by shutty on 11/20/16.
  */
object DefaultTypes {

  implicit object IntPacked extends PackedType[Int] {
    override def size(value:Int): Int = 4
    override def write(value:Int, buffer: ByteBuffer, offset: Int): Unit = buffer.putInt(offset, value)
    override def read(buffer: ByteBuffer, offset: Int): Int = buffer.getInt(offset)
  }

  implicit object LongPacked extends PackedType[Long] {
    override def size(value:Long): Int = 8
    override def write(value:Long, buffer: ByteBuffer, offset: Int): Unit = buffer.putLong(offset, value)
    override def read(buffer: ByteBuffer, offset: Int): Long = buffer.getLong(offset)
  }

}
