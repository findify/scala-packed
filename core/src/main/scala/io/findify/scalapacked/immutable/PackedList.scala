package io.findify.scalapacked.immutable

import io.findify.scalapacked.immutable
import io.findify.scalapacked.immutable.PackedList.PackedListBuilder
import io.findify.scalapacked.pool.{HeapPool, MemoryPool}
import io.findify.scalapacked.types.Codec

import scala.collection.{TraversableLike, mutable}
import scala.collection.generic.CanBuildFrom

class PackedList[A](val pool: MemoryPool = new HeapPool(20))(implicit codec: Codec[A]) extends Traversable[A] with TraversableLike[A, PackedList[A]] {
  override protected[this] def newBuilder = new PackedListBuilder[A]()

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

  def ++(other: PackedList[A]): PackedList[A] = {
    val unionPool = new HeapPool()
    unionPool.copy(pool)
    unionPool.copy(other.pool)
    new PackedList(unionPool)
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

  def compact: PackedList[A] = {
    new PackedList(pool.compact())
  }
}

object PackedList {
  class PackedListBuilder[A](implicit codec: Codec[A]) extends mutable.Builder[A, PackedList[A]] {
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
    override def result(): PackedList[A] = {
      new PackedList[A](pool)
    }
  }

  class PackedSeqCanBuildFrom[A](implicit codec: Codec[A]) extends CanBuildFrom[Any, A, PackedList[A]] {
    def apply = new PackedListBuilder[A]()
    def apply(from: Any) = new PackedListBuilder[A]()
  }

  implicit def canBuildFrom[A](implicit codec: Codec[A]) = new PackedSeqCanBuildFrom[A]()

  def apply[A](items: A*)(implicit codec: Codec[A]): PackedList[A] = {
    val builder = new PackedListBuilder[A]()
    builder ++= items
    builder.result()
  }

}