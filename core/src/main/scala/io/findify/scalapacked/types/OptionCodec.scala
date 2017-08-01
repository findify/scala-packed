package io.findify.scalapacked.types

import io.findify.scalapacked.pool.MemoryPool

class OptionCodec[T](implicit cdc: Codec[T]) extends Codec[Option[T]] {
  val EXISTS = 1.toByte
  val NOT_EXISTS = 0.toByte

  override def read(buffer: MemoryPool, offset: Int): Option[T] = {
    if (exists(buffer, offset)) {
      Some(cdc.read(buffer, offset + 1))
    } else {
      None
    }
  }
  override def size(buffer: MemoryPool, offset: Int): Int = {
    if (exists(buffer, offset)) {
      1 + cdc.size(buffer, offset + 1)
    } else {
      1
    }
  }
  override def size(item: Option[T]): Int = item match {
    case Some(inner) => 1 + cdc.size(inner)
    case None => 1
  }
  override def write(value: Option[T], buffer: MemoryPool): Int = value match {
    case Some(inner) =>
      val offset = buffer.writeByte(EXISTS)
      cdc.write(value.get, buffer)
      offset
    case None =>
      buffer.writeByte(NOT_EXISTS)
  }

  private def exists(buffer: MemoryPool, offset: Int): Boolean = buffer.readByte(offset) == EXISTS
}
