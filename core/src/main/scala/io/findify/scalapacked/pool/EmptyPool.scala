package io.findify.scalapacked.pool

import sun.reflect.generics.reflectiveObjects.NotImplementedException

object EmptyPool extends MemoryPool {
  def fail = throw new NotImplementedException()
  override def capacity: Int = 0
  override def size: Int = 0
  override def readByte(position: Int): Byte = fail
  override def writeByte(byte: Byte): Int = fail
  override def writeBytes(bytes: Array[Byte]): Int = fail
  override def readBytes(offset: Int, length: Int): Array[Byte] = fail
  override def writeInt(value: Int): Int = fail
  override def writeInt(value: Int, offset: Int): Int = fail
  override def readInt(offset: Int): Int = fail
  override def writeLong(value: Long): Int = fail
  override def readLong(offset: Int): Long = fail
  override def writeFloat(value: Float): Int = fail
  override def readFloat(offset: Int): Float = fail
  override def writeDouble(value: Double): Int = fail
  override def readDouble(offset: Int): Double = fail
  override def copy(other: MemoryPool): Unit = fail

}
