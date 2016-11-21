package io.findify.scalapacked

import java.nio.ByteBuffer

import io.findify.scalapacked.types.PackedType

/**
  * Created by shutty on 11/19/16.
  */
trait PackedProduct {
  var buffer: ByteBuffer
  var offset: Int
  def size: Int
}

object PackedMember {
  def memberSize[T](value: T)(implicit packer: PackedType[T]): Int = packer.size(value)
  def memberWrite[T](value: T, buffer: ByteBuffer, offset: Int)(implicit packer: PackedType[T]): Unit = packer.write(value, buffer, offset)
  def memberRead[T](buffer: ByteBuffer, offset: Int)(implicit packer: PackedType[T]):T = packer.read(buffer, offset)
}