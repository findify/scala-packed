package io.findify.scalapacked.pool

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by shutty on 1/22/17.
  */
class HeapPoolTest extends FlatSpec with Matchers {
  "heap pool" should "allocate 1k of ram" in {
    val pool = new HeapPool()
    pool.buffer.length shouldBe 1024
  }
  it should "write bytes" in {
    val pool = new HeapPool()
    val bytes = "hello".getBytes
    val offset = pool.writeBytes(bytes)
    pool.size shouldBe bytes.length
    val read = pool.readBytes(offset, bytes.length)
    read shouldBe bytes
  }
  it should "write ints" in {
    val pool = new HeapPool()
    val offset = pool.writeInt(31337)
    pool.size shouldBe 4
    pool.readInt(offset) shouldBe 31337
  }
  it should "write floats" in {
    val pool = new HeapPool()
    val offset = pool.writeFloat(3133.7f)
    pool.size shouldBe 4
    pool.readFloat(offset) shouldBe 3133.7f
  }
  it should "write longs" in {
    val pool = new HeapPool()
    val offset = pool.writeLong(4728057454353868390L)
    pool.size shouldBe 8
    pool.readLong(offset) shouldBe 4728057454353868390L
  }
  it should "write doubles" in {
    val in = java.lang.Double.doubleToLongBits(123456789.1)
    val out = java.lang.Double.longBitsToDouble(in)
    val pool = new HeapPool()
    val offset = pool.writeDouble(123456789.1)
    pool.size shouldBe 8
    pool.readDouble(offset) shouldBe 123456789.1
  }
  it should "grow the pool with ints" in {
    val pool = new HeapPool()
    val ints = (0 to 1000).toList
    val offsets = ints.map(i => pool.writeInt(i))
    val read = offsets.map(pool.readInt)
    read shouldBe ints
    pool.buffer.length should be > 1024
  }
  it should "copy from other pool" in {
    val p1 = new HeapPool()
    p1.writeInt(1)
    val p2 = new HeapPool()
    p2.writeInt(2)
    val merged = new HeapPool()
    merged.copy(p1)
    merged.copy(p2)
    merged.readInt(0) shouldBe 1
    merged.readInt(4) shouldBe 2
  }
}
