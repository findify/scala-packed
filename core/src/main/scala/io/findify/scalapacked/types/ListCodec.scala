package io.findify.scalapacked.types

import io.findify.scalapacked.pool.MemoryPool

import scala.collection.mutable

class ListCodec[T](implicit cdc: Codec[T]) extends Codec[List[T]] {
  override def read(buffer: MemoryPool, offset: Int): List[T] = SeqCodec.read(buffer, offset).toList
  override def size(buffer: MemoryPool, offset: Int): Int = buffer.readInt(offset)
  override def size(item: List[T]): Int = SeqCodec.size(item)
  override def write(value: List[T], buffer: MemoryPool): Int = SeqCodec.write(value, buffer)
}