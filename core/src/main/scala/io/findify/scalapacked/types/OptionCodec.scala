package io.findify.scalapacked.types

import io.findify.scalapacked.pool.MemoryPool

object OptionCodec {
  object NoneCodec extends Codec[None.type] {
    override def read(buffer: MemoryPool, offset: Int) = None
    override def size(buffer: MemoryPool, offset: Int): Int = 1
    override def size(item: None.type): Int = 1
    override def write(value: None.type, buffer: MemoryPool): Int = buffer.writeByte(0.toByte)
  }
  class SomeCodec[T](implicit cdc: Codec[T]) extends Codec[Some[T]] {
    override def read(buffer: MemoryPool, offset: Int) = Some(cdc.read(buffer, offset + 1))
    override def size(buffer: MemoryPool, offset: Int): Int = 1 + cdc.size(buffer, offset + 1)
    override def size(item: Some[T]): Int = 1 + cdc.size(item.get) //yep, get
    override def write(value: Some[T], buffer: MemoryPool): Int = {
      val offset = buffer.writeByte(1.toByte)
      cdc.write(value.get, buffer)
      offset
    }
  }
}