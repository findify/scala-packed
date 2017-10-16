package io.findify.scalapacked.types

import io.findify.scalapacked.pool.MemoryPool

object BooleanCodec extends Codec[Boolean] {
  override def read(buffer: MemoryPool, offset: Int): Boolean = buffer.readByte(offset) == 1
  override def size(buffer: MemoryPool, offset: Int): Int = 1
  override def size(item: Boolean): Int = 1
  override def write(value: Boolean, buffer: MemoryPool): Int = if (value) buffer.writeByte(1.toByte) else buffer.writeByte(0.toByte)
}
