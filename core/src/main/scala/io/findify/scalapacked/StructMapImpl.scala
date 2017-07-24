package io.findify.scalapacked

import io.findify.scalapacked.pool.{HeapPool, MemoryPool}

class StructMapImpl[A, B](val bucketCount: Int, pool: MemoryPool = new HeapPool(1024))(implicit ke: Encoder[A], kd: Decoder[A], ve: Encoder[B], vd: Decoder[B]) {
  val buckets = new Array[Int](bucketCount)
  val usedBuckets = new Array[Byte](bucketCount)
  var count = 0

  private def findBucket(key: A): Int = {
    var i = key.hashCode() % bucketCount
    while ((usedBuckets(i) == 1) && (key != kd.read(pool, buckets(i)))) {
      i = (i + 1) % bucketCount
    }
    i
  }

  def put(key: A, value: B): Unit = {
    val offset = ke.write(key, pool)
    ve.write(value, pool)
    val bucket = findBucket(key)
    if (usedBuckets(bucket) == 1) {
      buckets(bucket) = offset
    } else {
      buckets(bucket) = offset
      usedBuckets(bucket) = 1
      count += 1
    }
  }

  def get(key: A): Option[B] = {
    val bucket = findBucket(key)
    if (usedBuckets(bucket) == 1) {
      val keySize = kd.size(pool, buckets(bucket))
      Some(vd.read(pool, buckets(bucket) + keySize))
    } else {
      None
    }

  }
}
