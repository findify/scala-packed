package io.findify.scalapacked.types

import java.nio.ByteBuffer

/**
  * Created by shutty on 11/19/16.
  */
trait PackedType[@specialized(Int, Long, Float, Double) T] {
  def size(value: T): Int
  def write(value: T, buffer: ByteBuffer): Unit
  def read(buffer: ByteBuffer, offset: Int): T
}

