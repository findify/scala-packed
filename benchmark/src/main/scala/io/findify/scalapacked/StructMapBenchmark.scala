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
  var smap: PackedMap[Int, String] = _
  @Setup
  def setup = {
    import codec._
    val items = (0 to 1000).map(i => i -> s"v$i")
    map = Map(items: _*)
    smap = PackedMap(items: _*)
  }

  @Benchmark
  def measureMap = {
    val index = Random.nextInt(1000)
    map.get(index)
  }

  @Benchmark
  def measureStructMap = {
    val index = Random.nextInt(1000)
    smap.get(index)
  }
}
