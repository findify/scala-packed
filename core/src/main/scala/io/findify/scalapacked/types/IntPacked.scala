package io.findify.scalapacked.types

import java.nio.ByteBuffer

import io.findify.scalapacked.pool.MemoryPool

/**
  * Created by shutty on 11/22/16.
  */
object IntPacked extends PackedType[Int] {
  override def size(value:Int): Int = 4
  override def write(value:Int, buffer: MemoryPool): Int = buffer.writeInt(value)
  override def read(buffer: MemoryPool, offset: Int): Int = {
    buffer.readInt(offset)
  }
}