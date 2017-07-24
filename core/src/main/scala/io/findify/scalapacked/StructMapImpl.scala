package io.findify.scalapacked

class StructMapImpl(val bucketCount: Int) {
  val keyHashes = new Array[Int](bucketCount)
  val valueOffsets = new Array[Int](bucketCount)

  def put(key: Array[Byte], value: Array[Byte]): Unit = ???
}
