package io.findify.scalapacked.types

import java.nio.ByteBuffer

import io.findify.scalapacked.pool.MemoryPool

/**
  * Created by shutty on 11/22/16.
  */
object FloatPacked extends PackedType[Float] {
  override def size(value:Float): Int = 4
  override def write(value:Float, buffer: MemoryPool): Int = buffer.writeFloat(value)
  override def read(buffer: MemoryPool, offset: Int): Float = buffer.readFloat(offset)
}