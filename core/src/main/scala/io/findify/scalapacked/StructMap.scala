package io.findify.scalapacked

import io.findify.scalapacked.pool.{HeapPool, MemoryPool}

import scala.collection.generic.{CanBuildFrom, ImmutableMapFactory}
import scala.collection.mutable


class StructMap[A, +B](pool: MemoryPool = new HeapPool(20))(implicit ke: Encoder[A], kd: Decoder[A], ve: Encoder[B], vd: Decoder[B])
  extends scala.collection.immutable.Map[A, B] with scala.collection.immutable.MapLike[A, B, StructMap[A, B]] {
  override def empty: StructMap[A, B] = ???
  override def iterator: Iterator[(A, B)] = ???
  override def +[V1 >: B] (kv: (A, V1)): StructMap[A, V1] = ???
  override def -(key: A): StructMap[A, B] = ???
  override def get(key: A): Option[B] = ???
}

object StructMap extends ImmutableMapFactory[StructMap] {

  class StructMapBuilder[A,B](implicit ke: Encoder[A], kd: Decoder[A], ve: Encoder[B], vd: Decoder[B]) extends mutable.Builder[(A,B), StructMap[A,B]] {
    private var map = new StructMap[A,B]()
    override def +=(elem: (A, B)) = {
      map = map + elem
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

  def newStructBuilder[A, B](implicit ke: Encoder[A], kd: Decoder[A], ve: Encoder[B], vd: Decoder[B]): mutable.Builder[(A, B), StructMap[A, B]] = new StructMapBuilder[A,B]()
  implicit def canBuildFrom[A, B](implicit ke: Encoder[A], kd: Decoder[A], ve: Encoder[B], vd: Decoder[B]): CanBuildFrom[Coll, (A, B), StructMap[A, B]] = new StructMapCanBuildFrom[A, B]

  class StructMapCanBuildFrom[A, B](implicit ke: Encoder[A], kd: Decoder[A], ve: Encoder[B], vd: Decoder[B]) extends CanBuildFrom[Coll, (A,B), StructMap[A,B]] {
    def apply(from: Coll) = newStructBuilder[A, B]
    def apply() = newStructBuilder[A,B]
  }

  def apply[A, B](pair: (A, B), pairs: (A,B)*)(implicit ke: Encoder[A], kd: Decoder[A], ve: Encoder[B], vd: Decoder[B]): StructMap[A,B] = {
    val builder = newStructBuilder[A,B]
    builder += pair
    builder ++= pairs
    builder.result()
  }
}