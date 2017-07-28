package io.findify.scalapacked

import io.findify.scalapacked.pool.MemoryPool

trait Codec[T] {
  def read(buffer: MemoryPool, offset: Int): T
  def size(buffer: MemoryPool, offset: Int): Int
  def size(item: T): Int
  def write(value: T, buffer: MemoryPool): Int
}
