package io.findify.scalapacked

import io.findify.scalapacked.pool.{HeapPool, MemoryPool}
import io.findify.scalapacked.types.Codec

import scala.collection.generic.{CanBuildFrom, ImmutableMapFactory, MutableMapFactory}
import scala.collection.mutable


class StructMap[A, B](pool: MemoryPool = new HeapPool(20))(implicit kc: Codec[A], vc: Codec[B])
  extends scala.collection.mutable.Map[A, B] with scala.collection.mutable.MapLike[A, B, StructMap[A, B]] {
  val map = new StructMapImpl[A,B](16)

  override def size: Int = map.count
  override def empty: StructMap[A, B] = ???
  override def iterator: Iterator[(A, B)] = ???
  override def -(key: A): StructMap[A, B] = ???
  override def get(key: A): Option[B] = {
    map.get(key)
  }
  override def +=(kv: (A, B)): StructMap.this.type = {
    map.put(kv._1, kv._2)
    this
  }
  override def -=(key: A): StructMap.this.type = ???
}

object StructMap extends MutableMapFactory[StructMap] {

  class StructMapBuilder[A,B](implicit kc: Codec[A], vc: Codec[B]) extends mutable.Builder[(A,B), StructMap[A,B]] {
    private var map = new StructMap[A,B]()
    override def +=(elem: (A, B)) = {
      map += elem
      this
    }
    override def clear(): Unit = {
      map = new StructMap[A,B]()
    }
    override def result(): StructMap[A,B] = {
      map
    }

  }

  override def empty[A, B]: StructMap[A, B] = ???

  def newStructBuilder[A, B](implicit kc: Codec[A], vc: Codec[B]): mutable.Builder[(A, B), StructMap[A, B]] = new StructMapBuilder[A,B]()
  implicit def canBuildFrom[A, B](implicit kc: Codec[A], vc: Codec[B]): CanBuildFrom[Coll, (A, B), StructMap[A, B]] = new StructMapCanBuildFrom[A, B]

  class StructMapCanBuildFrom[A, B](implicit kc: Codec[A], vc: Codec[B]) extends CanBuildFrom[Coll, (A,B), StructMap[A,B]] {
    def apply(from: Coll) = newStructBuilder[A, B]
    def apply() = newStructBuilder[A,B]
  }

  def apply[A, B](pair: (A, B), pairs: (A,B)*)(implicit kc: Codec[A], vc: Codec[B]): StructMap[A,B] = {
    val builder = newStructBuilder[A,B]
    builder += pair
    builder ++= pairs
    builder.result()
  }
}