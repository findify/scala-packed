package io.findify.scalapacked

import io.findify.scalapacked.pool.MemoryPool
import io.findify.scalapacked.types.{IntPacked, StringPacked}
import org.github.jamm.MemoryMeter

/**
  * Created by shutty on 11/22/16.
  */

/*case class PackedInt(a:Int, b: String) extends Struct
case class WrappedInt(a:Int, b: String)

object MemoryFootprint {
  def main(args: Array[String]): Unit = {
    implicit val encoder = new Encoder[PackedInt] {
      override def write(value: PackedInt, buffer: MemoryPool): Int = {
        IntPacked.write(4 + IntPacked.size(value.a) + StringPacked.size(value.b), buffer)
        IntPacked.write(value.a, buffer)
        StringPacked.write(value.b, buffer)
      }
    }

    implicit val decoder = new Decoder[PackedInt] {
      override def read(buffer: MemoryPool, offset: Int): PackedInt = {
        val a = IntPacked.read(buffer, offset + 4)
        val s = StringPacked.read(buffer, offset + 8)
        PackedInt(a, s)
      }
      override def size(buffer: MemoryPool, offset: Int): Int = IntPacked.read(buffer, offset)
    }
    implicit def cbf = new StructCanBuildFrom[PackedInt]()
    val arrayInts = (0 to 100000).toArray
    val listInts = arrayInts.map(i => WrappedInt(i, i.toString)).toList
    val listPackedInts: StructBuffer[PackedInt] = arrayInts.toList.map(i => PackedInt(i, i.toString))


    val meter = new MemoryMeter()
    println(s"list of ints wrapped in case class = ${meter.measureDeep(listInts)}")
    println(s"packed seq = ${meter.measureDeep(listPackedInts)}")
  }
}
*/