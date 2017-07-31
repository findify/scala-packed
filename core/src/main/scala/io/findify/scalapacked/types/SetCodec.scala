package io.findify.scalapacked.types

import io.findify.scalapacked.pool.MemoryPool

import scala.collection.mutable

class SetCodec[K](implicit kc: Codec[K]) extends Codec[scala.collection.immutable.Set[K]] {
  override def read(buffer: MemoryPool, offset: Int): Set[K] = {
    val count = buffer.readInt(offset + 4)
    var i = 0
    var position = offset + 8
    val buf = new mutable.ArrayBuffer[K]()
    while (i < count) {
      val keySize = kc.size(buffer, position)
      val key = kc.read(buffer, position)
      position += keySize
      buf.append(key)
      i += 1
    }
    buf.toSet
  }
  override def size(buffer: MemoryPool, offset: Int): Int = buffer.readInt(offset)
  override def size(item: Set[K]): Int = {
    var bytes = 8
    item.foreach(k => {
      val keySize = kc.size(k)
      bytes += keySize
    })
    bytes
  }
  override def write(value: Set[K], buffer: MemoryPool): Int = {
    val start = buffer.writeInt(0)
    buffer.writeInt(value.size)
    var count = 0
    value.foreach( key => {
      kc.write(key, buffer)
      count += 1
    })
    buffer.writeInt(count, start)
    start
  }

}
