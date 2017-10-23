package io.findify.scalapacked.immutable

import java.util

import com.typesafe.scalalogging.LazyLogging
import io.findify.scalapacked.pool.{HeapPool, MemoryPool}
import io.findify.scalapacked.types.Codec

class PackedMapImpl[A, +B]
(
  var bucketCount: Int,
  var pool: MemoryPool,
  var buckets: Array[Int],
  var usedBuckets: util.BitSet,
  var count: Int
)(implicit kc: Codec[A], vc: Codec[B]) extends LazyLogging {

  private def findBucket(key: A): Int = {
    var i = math.abs(key.hashCode() % bucketCount)
    while (usedBuckets.get(i) && (key != kc.read(pool, buckets(i)))) {
      i = (i + 1) % bucketCount
    }
    i
  }

  def put[T >: B](key: A, value: T): Unit = {
    if (count > (bucketCount / 2)) rebuild
    val offset = kc.write(key, pool)
    vc.write(value.asInstanceOf[B], pool)
    //logger.debug(s"put: k=$key, v=$value, offset=$offset, size=${pool.size}")

    val bucket = findBucket(key)
    if (usedBuckets.get(bucket)) {
      buckets(bucket) = offset
    } else {
      buckets(bucket) = offset
      usedBuckets.set(bucket)
      count += 1
    }
  }

  def get(key: A): Option[B] = {
    val bucket = findBucket(key)
    if (usedBuckets.get(bucket)) {
      val keySize = kc.size(pool, buckets(bucket))
      Some(vc.read(pool, buckets(bucket) + keySize))
    } else {
      None
    }
  }

  def rebuild = {
    val larger = PackedMapImpl[A,B](bucketCount * 2)
    //logger.debug(s"rebuild: size = ${larger.bucketCount}")
    var i = 0
    while (i < bucketCount) {
      if (usedBuckets.get(i)) {
        //logger.debug(s"reading key at ${buckets(i)}")
        val key = kc.read(pool, buckets(i))
        val keySize = kc.size(pool, buckets(i))
        val value = vc.read(pool, buckets(i) + keySize)
        larger.put(key, value)
      }
      i += 1
    }
    bucketCount = larger.bucketCount
    buckets = larger.buckets
    usedBuckets = larger.usedBuckets
    count = larger.count
    pool = larger.pool
    //logger.debug("rebuild done")
  }

  def compact: PackedMapImpl[A, B] = {
    new PackedMapImpl[A, B](
      bucketCount  = bucketCount,
      pool = pool.compact(),
      buckets = buckets,
      usedBuckets = usedBuckets,
      count = count
    )
  }
}

object PackedMapImpl {
  def apply[A, B](bucketCount: Int = 16, poolSize: Int = 32)(implicit kc: Codec[A], vc: Codec[B]): PackedMapImpl[A, B] = {
    new PackedMapImpl[A, B](
      bucketCount = bucketCount,
      pool = new HeapPool(),
      buckets = new Array[Int](bucketCount),
      usedBuckets = new util.BitSet(bucketCount),
      count = 0
    )
  }
}