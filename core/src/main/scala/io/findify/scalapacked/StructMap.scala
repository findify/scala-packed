package io.findify.scalapacked

import io.findify.scalapacked.pool.{HeapPool, MemoryPool}

import scala.collection.generic.{CanBuildFrom, ImmutableMapFactory}


class StructMap[K <: Struct, V <: Struct]
(
  pool: MemoryPool = new HeapPool(20)
)
(
  implicit keyEncoder: Encoder[K],
  keyDecoder: Decoder[K],
  valueEncoder: Encoder[K],
  valueDecoder: Decoder[K]
) extends scala.collection.immutable.Map[K, V] with scala.collection.immutable.MapLike[K, V, StructMap[K, V]] {
  override def empty: StructMap[K, V] = ???
  override def iterator: Iterator[(K, V)] = ???
  override def +[V1 >: V] (kv: (K, V1)): StructMap[K, V1] = ???
  override def -(key: K): StructMap[K, V] = ???
  override def get(key: K): Option[V] = ???
}

object StructMap extends ImmutableMapFactory[StructMap] {
  override def empty[A, B]: StructMap[A, B] = ???
  implicit def canBuildFrom[K, V]: CanBuildFrom[Coll, (K, V), StructMap[K, V]] = new MapCanBuildFrom[K, V]
}