package io.findify.scalapacked.types

import java.nio.ByteBuffer

import io.findify.scalapacked.pool.MemoryPool

/**
  * Created by shutty on 11/19/16.
  */
trait PackedType[@specialized(Int, Long, Float, Double) T] {
  def size(value: T): Int
  def write(value: T, buffer: MemoryPool): Int
  def read(buffer: MemoryPool, offset: Int): T
}

