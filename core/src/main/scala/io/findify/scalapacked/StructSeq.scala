package io.findify.scalapacked

import io.findify.scalapacked.StructSeq.StructBuilder
import io.findify.scalapacked.pool.{HeapPool, MemoryPool}
import io.findify.scalapacked.types.Codec

import scala.collection.{TraversableLike, mutable}
import scala.collection.generic.CanBuildFrom

class StructSeq[A](pool: MemoryPool = new HeapPool(20))(implicit codec: Codec[A]) extends Traversable[A] with TraversableLike[A, StructSeq[A]] {
  override protected[this] def newBuilder = new StructBuilder[A]()

  def foreach[U](f: A => U): Unit = {
    val poolSize = pool.size
    var offset = 0
    while (offset < poolSize) {
      val size = codec.size(pool, offset)
      val item: A = codec.read(pool, offset)
      f(item)
      offset += size
    }
  }

  override def size: Int = {
    val poolSize = pool.size
    var offset = 0
    var count = 0
    while (offset < poolSize) {
      val size = codec.size(pool, offset)
      count += 1
      offset += size
    }
    count
  }
}

object StructSeq {
  class StructBuilder[A](implicit codec: Codec[A]) extends mutable.Builder[A, StructSeq[A]] {
    private var pool = new HeapPool(20)
    private var size = 0
    override def +=(elem: A) = {
      codec.write(elem, pool)
      size += 1
      this
    }
    override def clear(): Unit = {
      pool = new HeapPool(20)
      size = 0
    }
    override def result(): StructSeq[A] = {
      new StructSeq[A](pool)
    }
  }

  class StructCanBuildFrom[A](implicit codec: Codec[A]) extends CanBuildFrom[Any, A, StructSeq[A]] {
    def apply = new StructBuilder[A]()
    def apply(from: Any) = new StructBuilder[A]()
  }

  def apply[A](item: A, items: A*)(implicit codec: Codec[A]): StructSeq[A] = {
    val builder = new StructBuilder[A]()
    builder += item
    builder ++= items
    builder.result()
  }

}