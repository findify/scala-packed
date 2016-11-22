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

