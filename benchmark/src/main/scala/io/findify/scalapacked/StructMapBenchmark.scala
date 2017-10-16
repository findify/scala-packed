package io.findify.scalapacked


import java.util.concurrent.TimeUnit

import io.findify.scalapacked.PackedVector.PackedSeqCanBuildFrom
import io.findify.scalapacked.pool.MemoryPool
import org.openjdk.jmh.annotations._

import scala.util.Random

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class StructMapBenchmark {

  var map: Map[Int, String] = _

  @Param(Array("Map", "PackedMap"))
  var mapType: String = _
  val items = (0 to 1000).map(i => i -> s"v$i")

  @Setup
  def setup = {
    import codec._
    mapType match {
      case "Map" => map = Map(items: _*)
      case "PackedMap" => map = PackedMap(items: _*)
    }
  }

  @Benchmark
  def lookupExisting = {
    val index = Random.nextInt(1000)
    map.get(index)
  }


  @Benchmark
  def lookupNonExisting = {
    val index = 1000 + Random.nextInt(1000)
    map.get(index)
  }

  @Benchmark
  def build1000 = {
    import codec._
    mapType match {
      case "Map" => map = Map(items: _*)
      case "PackedMap" => map = PackedMap(items: _*)
    }
  }

}
