package io.findify.scalapacked

import org.github.jamm.MemoryMeter

/**
  * Created by shutty on 11/22/16.
  */

@Packed class PackedInt(a:Int)
case class WrappedInt(a:Int)

@Packed class PackedIS(a:Int, s:String)
case class WrappedIS(a:Int, s:String)

object MemoryFootprint {
  def main(args: Array[String]): Unit = {
    val arrayInts = (0 to 100000).toArray
    val listInts = arrayInts.map(i => WrappedInt(i)).toList
    val listPackedInts = PackedSeq[PackedInt](arrayInts.map(i => PackedInt(i)).toSeq)

    val listIS = arrayInts.map(i => WrappedIS(i, i.toString))
    val listPackedIS = PackedSeq[PackedIS](arrayInts.map(i => PackedIS(i, i.toString)).toSeq)

    val meter = new MemoryMeter()
    println(s"array of ints = ${meter.measureDeep(arrayInts)}")
    println(s"list of ints wrapped in case class = ${meter.measureDeep(listInts)}")
    println(s"packed seq = ${meter.measureDeep(listPackedInts)}")
    println(s"list of I+S wrapped in case class = ${meter.measureDeep(listIS)}")
    println(s"packed I+S seq = ${meter.measureDeep(listPackedIS)}")

    println(s"sizeof case IS = ${meter.measureDeep(WrappedIS(1, "foo"))}")
    println(s"sizeof packed IS = ${meter.measureDeep(PackedIS(1, "foo"))}")
  }
}
