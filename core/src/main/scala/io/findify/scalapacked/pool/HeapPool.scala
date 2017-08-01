package io.findify.scalapacked.pool

import java.nio.ByteBuffer
import java.util

/**
  * Created by shutty on 1/22/17.
  */
class HeapPool(growthFactor: Float = 1.5f, var buffer: Array[Byte] = new Array[Byte](1024), var bufferBytesUsed: Int = 0) extends MemoryPool {
  override def capacity = buffer.length

  override def size = bufferBytesUsed

  override def copy(other: MemoryPool) = {
    grow(other.size)
    var i = 0
    while (i < other.size) {
      buffer(size + i) = other.readByte(i)
      i += 1
    }
    bufferBytesUsed += other.size
  }

  override def readByte(position: Int) = buffer(position)

  override def writeByte(byte: Byte): Int = {
    grow(1)
    val offset = bufferBytesUsed
    buffer(bufferBytesUsed) = byte
    bufferBytesUsed += 1
    offset
  }

  override def writeBytes(bytes: Array[Byte]) = {
    grow(bytes.length)
    var i = 0
    while (i < bytes.length) {
      buffer(bufferBytesUsed + i) = bytes(i)
      i += 1
    }
    val offset = bufferBytesUsed
    bufferBytesUsed += bytes.length
    offset
  }
  override def readBytes(offset: Int, length: Int): Array[Byte] = {
    if (offset + length > buffer.length) throw new IndexOutOfBoundsException()
    val bytes = new Array[Byte](length)
    var i = 0
    while (i < length) {
      bytes(i) = buffer(offset + i)
      i += 1
    }
    bytes
  }
  override def writeInt(value: Int) = {
    grow(4)
    val offset = bufferBytesUsed
    buffer(bufferBytesUsed) = (value >> 24).toByte
    buffer(bufferBytesUsed + 1) = (value >> 16).toByte
    buffer(bufferBytesUsed + 2) = (value >> 8).toByte
    buffer(bufferBytesUsed + 3) = value.toByte
    bufferBytesUsed += 4
    offset
  }
  def writeInt(value: Int, offset: Int): Int = {
    buffer(offset) = (value >> 24).toByte
    buffer(offset + 1) = (value >> 16).toByte
    buffer(offset + 2) = (value >> 8).toByte
    buffer(offset + 3) = value.toByte
    offset
  }
  override def readInt(offset: Int): Int = {
    if (offset + 4 > buffer.length) throw new IndexOutOfBoundsException()
    val i1 = buffer(offset) & 0xFF
    val i2 = buffer(offset + 1) & 0xFF
    val i3 = buffer(offset + 2) & 0xFF
    val i4 = buffer(offset + 3) & 0xFF
    (i1 << 24) | (i2 << 16) | (i3 << 8) | (i4 << 0)
  }
  override def writeFloat(value: Float) = {
    writeInt(java.lang.Float.floatToIntBits(value))
  }
  override def readFloat(offset: Int): Float = {
    java.lang.Float.intBitsToFloat(readInt(offset))
  }

  override def writeLong(value: Long) = {
    grow(8)
    val offset = bufferBytesUsed
    buffer(bufferBytesUsed + 0) = (value >> 56).toByte
    buffer(bufferBytesUsed + 1) = (value >> 48).toByte
    buffer(bufferBytesUsed + 2) = (value >> 40).toByte
    buffer(bufferBytesUsed + 3) = (value >> 32).toByte
    buffer(bufferBytesUsed + 4) = (value >> 24).toByte
    buffer(bufferBytesUsed + 5) = (value >> 16).toByte
    buffer(bufferBytesUsed + 6) = (value >> 8).toByte
    buffer(bufferBytesUsed + 7) = value.toByte
    bufferBytesUsed += 8
    offset
  }

  override def readLong(offset: Int) = {
    if (offset + 8 > buffer.length) throw new IndexOutOfBoundsException()
    val i1 = (buffer(offset) & 0xFF).toLong
    val i2 = (buffer(offset + 1) & 0xFF).toLong
    val i3 = (buffer(offset + 2) & 0xFF).toLong
    val i4 = (buffer(offset + 3) & 0xFF).toLong
    val i5 = (buffer(offset + 4) & 0xFF).toLong
    val i6 = (buffer(offset + 5) & 0xFF).toLong
    val i7 = (buffer(offset + 6) & 0xFF).toLong
    val i8 = (buffer(offset + 7) & 0xFF).toLong
    (i1 << 56) | (i2 << 48) | (i3 << 40) | (i4 << 32) | (i5 << 24) | (i6 << 16) | (i7 << 8) | (i8 << 0)
  }

  override def writeDouble(value: Double) = writeLong(java.lang.Double.doubleToLongBits(value))

  override def readDouble(offset: Int) = java.lang.Double.longBitsToDouble(readLong(offset))

  private def grow(bytesToAdd: Int) = if (bytesToAdd > (capacity - size)) {
    val newCapacity = math.round(buffer.length * growthFactor)
    val grown = util.Arrays.copyOf(buffer, newCapacity)
    buffer = grown
  }

  override def compact(): MemoryPool = {
    val other = util.Arrays.copyOf(buffer, size)
    new HeapPool(buffer = other, bufferBytesUsed = bufferBytesUsed)
  }
}

object HeapPool {
  def apply(preallocate: Int = 1024): HeapPool = new HeapPool(
    buffer = new Array[Byte](preallocate)
  )
}