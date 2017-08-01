package io.findify.scalapacked.types

import io.findify.scalapacked.pool.MemoryPool

import scala.collection.mutable

class SeqCodec[T](implicit cdc: Codec[T]) extends Codec[Seq[T]] {
  override def read(buffer: MemoryPool, offset: Int): Seq[T] = SeqCodec.read(buffer, offset)
  override def size(buffer: MemoryPool, offset: Int): Int = buffer.readInt(offset)
  override def size(item: Seq[T]): Int = SeqCodec.size(item)
  override def write(value: Seq[T], buffer: MemoryPool): Int = SeqCodec.write(value, buffer)
}

object SeqCodec {
  def read[T](buffer: MemoryPool, offset: Int)(implicit cdc: Codec[T]): Seq[T] = {
    val count = buffer.readInt(offset + 4)
    var i = 0
    var position = offset + 8
    val buf = new mutable.ArrayBuffer[T]()
    while (i < count) {
      val keySize = cdc.size(buffer, position)
      val key = cdc.read(buffer, position)
      position += keySize
      buf.append(key)
      i += 1
    }
    buf
  }
  def size[T](item: Seq[T])(implicit cdc: Codec[T]): Int = {
    var bytes = 8
    item.foreach(k => {
      val keySize = cdc.size(k)
      bytes += keySize
    })
    bytes
  }
  def write[T](value: Seq[T], buffer: MemoryPool)(implicit cdc: Codec[T]): Int = {
    val start = buffer.writeInt(0)
    buffer.writeInt(value.size)
    var count = 0
    value.foreach( key => {
      cdc.write(key, buffer)
      count += 1
    })
    buffer.writeInt(buffer.size - start, start)
    start
  }


}