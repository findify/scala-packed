package io.findify.scalapacked.codecs

import io.findify.scalapacked.pool.HeapPool
import io.findify.scalapacked.types.Codec

trait CodecTest {
  def roundtrip[T](value: T)(implicit cdc: Codec[T]): T = {
    val buf = new HeapPool()
    val offset = cdc.write(value, buf)
    cdc.read(buf, offset)
  }

  def size[T](value: T)(implicit cdc: Codec[T]): Int = {
    cdc.size(value)
  }

  def sizeSerialized[T](value: T)(implicit cdc: Codec[T]): Int = {
    val buf = new HeapPool()
    val offset = cdc.write(value, buf)
    cdc.size(buf, offset)
  }
}
