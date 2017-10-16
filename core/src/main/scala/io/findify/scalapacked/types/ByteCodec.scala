package io.findify.scalapacked.types

import io.findify.scalapacked.pool.MemoryPool

object ByteCodec extends Codec[Byte] {
  override def read(buffer: MemoryPool, offset: Int): Byte = buffer.readByte(offset)
  override def size(buffer: MemoryPool, offset: Int): Int = 1
  override def size(item: Byte): Int = 1
  override def write(value: Byte, buffer: MemoryPool): Int = buffer.writeByte(value)
}
