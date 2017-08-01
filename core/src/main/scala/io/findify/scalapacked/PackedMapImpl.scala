package io.findify.scalapacked

import java.util

import com.typesafe.scalalogging.LazyLogging
import io.findify.scalapacked.pool.{HeapPool, MemoryPool}
import io.findify.scalapacked.types.Codec

class PackedMapImpl[A, B](var bucketCount: Int, var pool: MemoryPool = new HeapPool(1024))(implicit kc: Codec[A], vc: Codec[B]) extends LazyLogging {
  var buckets = new Array[Int](bucketCount)
  var usedBuckets = new util.BitSet(bucketCount)
  var count = 0

  private def findBucket(key: A): Int = {
    var i = math.abs(key.hashCode() % bucketCount)
    while (usedBuckets.get(i) && (key != kc.read(pool, buckets(i)))) {
      i = (i + 1) % bucketCount
    }
    i
  }

  def put(key: A, value: B): Unit = {
    if (count > (bucketCount / 2)) rebuild
    val offset = kc.write(key, pool)
    vc.write(value, pool)
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
    val larger = new PackedMapImpl[A,B](bucketCount * 2)
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
}
