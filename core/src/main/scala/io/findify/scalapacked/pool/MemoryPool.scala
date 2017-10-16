package io.findify.scalapacked.pool

/**
  * Created by shutty on 1/22/17.
  */
trait MemoryPool {
  def capacity: Int
  def size: Int
  def writeByte(byte: Byte): Int
  def readByte(position: Int): Byte
  def writeBytes(bytes: Array[Byte]): Int
  def readBytes(offset: Int, length: Int): Array[Byte]
  def writeInt(value: Int): Int
  def writeInt(value: Int, offset: Int): Int
  def readInt(offset: Int): Int
  def writeLong(value: Long): Int
  def readLong(offset: Int): Long
  def writeFloat(value: Float): Int
  def readFloat(offset: Int): Float
  def writeDouble(value: Double): Int
  def readDouble(offset: Int): Double
  def copy(other: MemoryPool): Unit
  def compact(): MemoryPool
}
