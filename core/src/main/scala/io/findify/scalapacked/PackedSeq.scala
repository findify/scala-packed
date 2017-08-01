package io.findify.scalapacked

import io.findify.scalapacked.PackedSeq.PackedSeqBuilder
import io.findify.scalapacked.pool.{HeapPool, MemoryPool}
import io.findify.scalapacked.types.Codec

import scala.collection.{TraversableLike, mutable}
import scala.collection.generic.CanBuildFrom

class PackedSeq[A](pool: MemoryPool = new HeapPool(20))(implicit codec: Codec[A]) extends Traversable[A] with TraversableLike[A, PackedSeq[A]] {
  override protected[this] def newBuilder = new PackedSeqBuilder[A]()

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

object PackedSeq {
  class PackedSeqBuilder[A](implicit codec: Codec[A]) extends mutable.Builder[A, PackedSeq[A]] {
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
    override def result(): PackedSeq[A] = {
      new PackedSeq[A](pool)
    }
  }

  class PackedSeqCanBuildFrom[A](implicit codec: Codec[A]) extends CanBuildFrom[Any, A, PackedSeq[A]] {
    def apply = new PackedSeqBuilder[A]()
    def apply(from: Any) = new PackedSeqBuilder[A]()
  }

  def apply[A](items: A*)(implicit codec: Codec[A]): PackedSeq[A] = {
    val builder = new PackedSeqBuilder[A]()
    builder ++= items
    builder.result()
  }

}