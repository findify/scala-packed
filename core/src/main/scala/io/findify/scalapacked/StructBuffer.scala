package io.findify.scalapacked

import io.findify.scalapacked.pool.{HeapPool, MemoryPool}
import io.findify.scalapacked.types.IntPacked

class StructBuffer[A](pool: MemoryPool)(implicit codec: Codec[A]) {

  def foreach[U](f: A => U): Unit = {
    val poolSize = pool.size
    var offset = 0
    while (offset < poolSize) {
      val item: A = codec.build(offset)
      val size = itemSize(offset)
      f(item)
      offset += size
    }
  }
  def map[B](f: (A, ) => B): StructBuffer[B] = {
    val bpool = new HeapPool(20)
    val poolSize = pool.size
    var offset = 0
    while (offset < poolSize) {
      val item: A = codec.build(offset)
      val size = itemSize(offset)
      val b = f(item)
      offset += size
    }
  }
  def size: Int = {
    val poolSize = pool.size
    var offset = 0
    var count = 0
    while (offset < poolSize) {
      //val item: A = codec.build(offset)
      val size = itemSize(offset)
      count += 1
      offset += size
    }
    count
  }

  private def itemSize(offset: Int) = IntPacked.read(pool, offset)
}

