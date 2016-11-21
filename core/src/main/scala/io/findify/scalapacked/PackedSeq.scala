package io.findify.scalapacked

import java.nio.ByteBuffer

import scala.reflect.ClassTag

/**
  * Created by shutty on 11/20/16.
  */
class PackedSeq[T <: PackedProduct : ClassTag](buffer: ByteBuffer, size: Int) {
  import scala.reflect._
  def map[U <: PackedProduct : ClassTag]( f: T => U ) = {
    var offset = 0
    var count = 0
    val instanceT = classTag[T].runtimeClass.newInstance().asInstanceOf[T]
    var instanceU = classTag[U].runtimeClass.newInstance().asInstanceOf[U]
    var output = ByteBuffer.allocate(1024)
    while (count < size) {
      instanceT.buffer = buffer
      instanceT.offset = offset
      offset += instanceT.size
      instanceU = f(instanceT)
      count += 1
      if (output.remaining() > instanceU.size) {
        output.put(instanceU.buffer)
      } else {
        val largerOutput = ByteBuffer.allocate(output.capacity() * 2)
        largerOutput.put(output)
        largerOutput.put(instanceU.buffer)
        output = largerOutput
      }
    }
    new PackedSeq[U](output, size)
  }

  def foreach(f: T => Unit) = {
    var offset = 0
    var count = 0
    val instanceT = classTag[T].runtimeClass.newInstance().asInstanceOf[T]
    while (count < size) {
      instanceT.buffer = buffer
      instanceT.offset = offset
      offset += instanceT.size
      f(instanceT)
      count += 1
    }
  }
}

object PackedSeq {
  def apply[T <: PackedProduct : ClassTag](items: Seq[T]) = {
    val initial = ByteBuffer.allocate(1024)
    val buffer = items.foldLeft(initial)( (buffer, item) => {
      if (buffer.remaining() > item.size) {
        buffer.put(item.buffer)
        buffer
      } else {
        val largerBuffer = ByteBuffer.allocate(buffer.capacity() * 2)
        largerBuffer.put(buffer)
        largerBuffer.put(item.buffer)
      }
    })
    new PackedSeq[T](buffer, items.length)
  }
}
