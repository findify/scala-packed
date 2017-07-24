package io.findify.scalapacked

import io.findify.scalapacked.pool.MemoryPool

trait Encoder[T] {
  def write(value: T, buffer: MemoryPool): Int
}
