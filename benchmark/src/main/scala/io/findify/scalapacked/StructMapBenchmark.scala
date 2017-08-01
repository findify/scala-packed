package io.findify.scalapacked


import java.util.concurrent.TimeUnit

import io.findify.scalapacked.PackedSeq.PackedSeqCanBuildFrom
import io.findify.scalapacked.pool.MemoryPool
import org.openjdk.jmh.annotations._

import scala.util.Random

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class StructMapBenchmark {

  var map: Map[Int, String] = _

  @Param(Array("Map", "PackedMap"))
  val mapType: String = null

  @Setup
  def setup = {
    import codec._
    val items = (0 to 1000).map(i => i -> s"v$i")
    mapType match {
      case "Map" => map = Map(items: _*)
      case "PackedMap" => map = PackedMap(items: _*)
    }
    map = Map(items: _*)
  }

  @Benchmark
  def measureLookup = {
    val index = Random.nextInt(1000)
    map.get(index)
  }

}
