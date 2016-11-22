package io.findify.scalapacked.types

import java.nio.ByteBuffer

import io.findify.scalapacked.PackedProduct

import scala.reflect.ClassTag

/**
  * Created by shutty on 11/20/16.
  */
object DefaultTypes {

  implicit object IntPacked extends PackedType[Int] {
    override def size(value:Int): Int = 4
    override def write(value:Int, buffer: ByteBuffer): Unit = buffer.putInt(value)
    override def read(buffer: ByteBuffer, offset: Int): Int = {
      buffer.getInt(offset)
    }
  }

  implicit object LongPacked extends PackedType[Long] {
    override def size(value:Long): Int = 8
    override def write(value:Long, buffer: ByteBuffer): Unit = buffer.putLong(value)
    override def read(buffer: ByteBuffer, offset: Int): Long = buffer.getLong(offset)
  }

  implicit object StringPacked extends PackedType[String] {
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

}
