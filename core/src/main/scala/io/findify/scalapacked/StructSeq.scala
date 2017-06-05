package io.findify.scalapacked

import io.findify.scalapacked.StructSeq.StructBuilder
import io.findify.scalapacked.pool.{HeapPool, MemoryPool}

import scala.collection.{TraversableLike, mutable}
import scala.collection.generic.CanBuildFrom

class StructSeq[A <: Struct](pool: MemoryPool = new HeapPool(20))(implicit encoder: Encoder[A], decoder: Decoder[A]) extends Traversable[A] with TraversableLike[A, StructSeq[A]] {
  override protected[this] def newBuilder = new StructBuilder[A]()

  def foreach[U](f: A => U): Unit = {
    val poolSize = pool.size
    var offset = 0
    while (offset < poolSize) {
      val size = decoder.size(pool, offset)
      val item: A = decoder.read(pool, offset)
      f(item)
      offset += size
    }
  }
}

object StructSeq {
  class StructBuilder[A <: Struct](implicit encoder: Encoder[A], decoder: Decoder[A]) extends mutable.Builder[A, StructSeq[A]] {
    private var pool = new HeapPool(20)
    private var size = 0
    override def +=(elem: A) = {
      encoder.write(elem, pool)
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

  class StructCanBuildFrom[A <: Struct](implicit encoder: Encoder[A], decoder: Decoder[A]) extends CanBuildFrom[Any, A, StructSeq[A]] {
    def apply = new StructBuilder[A]()
    def apply(from: Any) = new StructBuilder[A]()
  }
  
}