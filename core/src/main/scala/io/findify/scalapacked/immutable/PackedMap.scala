package io.findify.scalapacked.immutable

import java.util

import io.findify.scalapacked.immutable
import io.findify.scalapacked.immutable.PackedMap.StructMapIterator
import io.findify.scalapacked.types.Codec

import scala.collection.generic.{CanBuildFrom, ImmutableMapFactory}
import scala.collection.mutable


class PackedMap[A, +B](val map: PackedMapImpl[A,B])(implicit kc: Codec[A], vc: Codec[B])
  extends scala.collection.immutable.Map[A, B] with scala.collection.immutable.MapLike[A, B, PackedMap[A, B]] {

  override def size: Int = map.count
  override def empty: PackedMap[A, B] = PackedMap.empty
  override def iterator: Iterator[(A, B)] = new StructMapIterator(this)
  override def -(key: A): PackedMap[A, B] = ???
  override def get(key: A): Option[B] = {
    map.get(key)
  }
  override def +[V1 >: B](kv: (A, V1)): PackedMap.this.type = {
    ???
  }

  def compact: PackedMap[A, B] = {
    new PackedMap(map.compact)
  }
}

object PackedMap extends ImmutableMapFactory[PackedMap] {

  class StructMapIterator[A,B](parent: PackedMap[A,B])(implicit kc: Codec[A], vc: Codec[B]) extends Iterator[(A,B)] {
    private var position = 0
    private val positions = {
      val pos = new Array[Int](parent.map.bucketCount)
      var i = 0
      var ins = 0
      while (i < parent.map.bucketCount) {
        if (parent.map.usedBuckets.get(i)) {
          pos(ins) = parent.map.buckets(i)
          ins += 1
        }
        i += 1
      }
      util.Arrays.copyOfRange(pos, 0, ins)
    }
    override def hasNext: Boolean = position < positions.length
    override def next(): (A, B) = {
      val offset = positions(position)
      val keySize = kc.size(parent.map.pool, offset)
      val key = kc.read(parent.map.pool, offset)
      val value = vc.read(parent.map.pool, offset + keySize)
      position += 1
      key -> value
    }
  }

  class PackedMapBuilder[A,B](implicit kc: Codec[A], vc: Codec[B]) extends mutable.Builder[(A,B), PackedMap[A,B]] {
    private var map = PackedMapImpl()(kc, vc)
    override def +=(elem: (A, B)) = {
      map.put(elem._1, elem._2)
      this
    }
    override def clear(): Unit = {
      map = PackedMapImpl()(kc, vc)
    }
    override def result(): PackedMap[A,B] = {
      new PackedMap[A,B](map)
    }

  }


  override def empty[A, B]: PackedMap[A, B] = {
    ???
  }

  override def newBuilder[A, B]: mutable.Builder[(A, B), PackedMap[A, B]] = {
    ???
  }

  def newStructBuilder[A, B](implicit kc: Codec[A], vc: Codec[B]): mutable.Builder[(A, B), PackedMap[A, B]] = new PackedMapBuilder[A,B]()
  implicit def canBuildFrom[A, B](implicit kc: Codec[A], vc: Codec[B]): CanBuildFrom[Coll, (A, B), PackedMap[A, B]] = new PackedMapCanBuildFrom[A, B]

  class PackedMapCanBuildFrom[A, B](implicit kc: Codec[A], vc: Codec[B]) extends CanBuildFrom[Coll, (A,B), PackedMap[A,B]] {
    def apply(from: Coll) = newStructBuilder[A, B]
    def apply() = newStructBuilder[A,B]
  }

  def apply[A,B](pairs: (A, B)*)(implicit kc: Codec[A], vc: Codec[B]): PackedMap[A,B] = {
    val builder = newStructBuilder[A,B]
    builder ++= pairs
    builder.result()
  }

  def apply[A,B](poolSize: Int = 32, bucketCount: Int = 16)(implicit kc: Codec[A], vc: Codec[B]): PackedMap[A, B] = {
    new PackedMap(
      map = PackedMapImpl[A,B](bucketCount, poolSize)
    )
  }

/*  def apply[A, B](pair: (A, B), pairs: (A,B)*)(implicit kc: Codec[A], vc: Codec[B]): StructMap[A,B] = {
    val builder = newStructBuilder[A,B]
    builder += pair
    builder ++= pairs
    builder.result()
  }*/
}