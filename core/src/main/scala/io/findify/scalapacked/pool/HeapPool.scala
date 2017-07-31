package io.findify.scalapacked.pool

import java.nio.ByteBuffer

/**
  * Created by shutty on 1/22/17.
  */
class HeapPool(preallocate: Int = 1024, growthFactor: Float = 1.5f) extends MemoryPool {
  private var bufferLength = 0
  var buffer = new Array[Byte](preallocate)

  override def capacity = buffer.length

  override def size = bufferLength

  override def copy(other: MemoryPool) = {
    grow(other.size)
    var i = 0
    while (i < other.size) {
      buffer(size + i) = other.readByte(i)
      i += 1
    }
    bufferLength += other.size
  }

  override def readByte(position: Int) = buffer(position)

  override def writeByte(byte: Byte): Int = {
    grow(1)
    val offset = bufferLength
    buffer(bufferLength) = byte
    bufferLength += 1
    offset
  }

  override def writeBytes(bytes: Array[Byte]) = {
    grow(bytes.length)
    var i = 0
    while (i < bytes.length) {
      buffer(bufferLength + i) = bytes(i)
      i += 1
    }
    val offset = bufferLength
    bufferLength += bytes.length
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
    val offset = bufferLength
    buffer(bufferLength) = (value >> 24).toByte
    buffer(bufferLength + 1) = (value >> 16).toByte
    buffer(bufferLength + 2) = (value >> 8).toByte
    buffer(bufferLength + 3) = value.toByte
    bufferLength += 4
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
    val offset = bufferLength
    buffer(bufferLength + 0) = (value >> 56).toByte
    buffer(bufferLength + 1) = (value >> 48).toByte
    buffer(bufferLength + 2) = (value >> 40).toByte
    buffer(bufferLength + 3) = (value >> 32).toByte
    buffer(bufferLength + 4) = (value >> 24).toByte
    buffer(bufferLength + 5) = (value >> 16).toByte
    buffer(bufferLength + 6) = (value >> 8).toByte
    buffer(bufferLength + 7) = value.toByte
    bufferLength += 8
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

  private def grow(bytesToAdd: Int) = if (bytesToAdd > (buffer.length - bufferLength)) {
    val capacity = math.round(buffer.length * growthFactor)
    val grown = new Array[Byte](capacity)
    var i = 0
    while (i < bufferLength) {
      grown(i) = buffer(i)
      i += 1
    }
    buffer = grown
  }
}
