package io.findify.scalapacked

import io.findify.scalapacked.PackedSeq.PackedSeqCanBuildFrom
import io.findify.scalapacked.pool.MemoryPool
import org.github.jamm.MemoryMeter

import scala.util.Random

/**
  * Created by shutty on 11/22/16.
  */

object MemoryFootprint {
  val meter = new MemoryMeter()
  val count = 1024*10
  def main(args: Array[String]): Unit = {
    import codec._

    val listInts = (0 to count).toList
    val listPackedInts: PackedSeq[Int] = PackedSeq(listInts: _*).compact
    measure("list of ints", listInts, listPackedInts)

    val listStrings = (0 to count).map(_.toString).toList
    val listPackedStrings = PackedSeq(listStrings: _*).compact
    measure("list of strings", listStrings, listPackedStrings)

    val mapStrings = (0 to count).map(i => i.toString -> (i + 1).toString).toMap
    val mapPackedStrings = PackedMap(mapStrings.toList: _*).compact
    measure("map of strings", mapStrings, mapPackedStrings)

    import codec.generic._
    case class NestedFoo(x: String)
    case class ComplexFoo(i: Int, s: String, n: NestedFoo)
    val mapCase = (0 to count).map(i => Random.nextInt() -> ComplexFoo(i, i.toString, NestedFoo((i + 1).toString))).toMap
    val mapPackedCase = PackedMap(mapCase.toList: _*).compact
    val cdc = deriveCodec[ComplexFoo]
    println(s"obj size = ${cdc.size(mapPackedCase.head._2)}")
    measure("map of case classes", mapCase, mapPackedCase)
  }

  def measure(name: String, baseline: AnyRef, packed: AnyRef) = {
    val baselineBytes = meter.measureDeep(baseline)
    val packedBytes = meter.measureDeep(packed)
    println(s"$name: baseline = ${baselineBytes / count} byte/item")
    println(s"$name: packed = ${packedBytes / count} byte/item (${100.0 * (packedBytes / baselineBytes.toDouble)}% of original)")

  }
}
