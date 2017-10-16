package io.findify.scalapacked.immutable

import io.findify.scalapacked.immutable
import io.findify.scalapacked.immutable.PackedVector.PackedVectorBuilder
import io.findify.scalapacked.pool.{HeapPool, MemoryPool}
import io.findify.scalapacked.types.Codec

import scala.collection.{TraversableLike, mutable}
import scala.collection.generic.CanBuildFrom

class PackedVector[A](pool: MemoryPool = new HeapPool(20))(implicit codec: Codec[A]) extends Traversable[A] with TraversableLike[A, PackedVector[A]] {
  override protected[this] def newBuilder = new PackedVectorBuilder[A]()

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

  def compact: PackedVector[A] = {
    new PackedVector(pool.compact())
  }
}

object PackedVector {
  class PackedVectorBuilder[A](implicit codec: Codec[A]) extends mutable.Builder[A, PackedVector[A]] {
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
    override def result(): PackedVector[A] = {
      new PackedVector[A](pool)
    }
  }

  class PackedSeqCanBuildFrom[A](implicit codec: Codec[A]) extends CanBuildFrom[Any, A, PackedVector[A]] {
    def apply = new PackedVectorBuilder[A]()
    def apply(from: Any) = new PackedVectorBuilder[A]()
  }

  def apply[A](items: A*)(implicit codec: Codec[A]): PackedVector[A] = {
    val builder = new PackedVectorBuilder[A]()
    builder ++= items
    builder.result()
  }

}