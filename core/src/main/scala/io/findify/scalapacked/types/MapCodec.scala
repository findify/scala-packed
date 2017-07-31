package io.findify.scalapacked.types

import io.findify.scalapacked.pool.MemoryPool

import scala.collection.mutable

class MapCodec[K, V](implicit kc: Codec[K], vc: Codec[V]) extends Codec[scala.collection.immutable.Map[K,V]] {
  override def read(buffer: MemoryPool, offset: Int): Map[K,V] = {
    val count = buffer.readInt(offset + 4)
    var i = 0
    var position = offset + 8
    val buf = new Array[(K,V)](count)
    while (i < count) {
      val keySize = kc.size(buffer, position)
      val key = kc.read(buffer, position)
      val valueSize = vc.size(buffer, position + keySize)
      val value = vc.read(buffer, position + keySize)
      position += keySize + valueSize
      buf(i) = key -> value
      i += 1
    }
    buf.toMap
  }
  override def size(buffer: MemoryPool, offset: Int): Int = buffer.readInt(offset)
  override def size(item: Map[K,V]): Int = {
    var bytes = 8
    item.foreach(kv => {
      val keySize = kc.size(kv._1)
      val valueSize = vc.size(kv._2)
      bytes += keySize + valueSize
    })
    bytes
  }
  override def write(value: Map[K,V], buffer: MemoryPool): Int = {
    val start = buffer.writeInt(0)
    buffer.writeInt(value.size)
    value.foreach( kv => {
      kc.write(kv._1, buffer)
      vc.write(kv._2, buffer)
    })
    val written = buffer.size - start
    buffer.writeInt(written, 0)
    start
  }

}
