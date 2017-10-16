package io.findify.scalapacked.types

import io.findify.scalapacked.pool.MemoryPool

object LongCodec extends Codec[Long] {
  override def read(buffer: MemoryPool, offset: Int): Long = buffer.readLong(offset)
  override def size(buffer: MemoryPool, offset: Int): Int = 8
  override def size(item: Long): Int = 8
  override def write(value: Long, buffer: MemoryPool): Int = buffer.writeLong(value)
}
