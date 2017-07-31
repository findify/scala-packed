package io.findify.scalapacked

import java.util.concurrent.TimeUnit

import io.findify.scalapacked.StructSeq.StructCanBuildFrom
import io.findify.scalapacked.pool.MemoryPool
import org.openjdk.jmh.annotations._

/**
  * Created by shutty on 11/22/16.
  */

case class Intp(a:Int)
case class Intc(a:Int)

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class StructSeqBenchmark {
  var list: List[Intc] = _
  var array: Array[Int] = _
  var pseq: StructSeq[Intp] = _


  @Setup
  def setup = {
    import codec._
    import codec.generic._
    implicit def cbf = new StructCanBuildFrom[Intp]()
    array = (0 to 1000).toArray
    list = array.map(i => Intc(i)).toList
    pseq = array.map(i => Intp(i))
  }

  @Benchmark
  def measureList = {
    var counter = 0L
    list.foreach(item => counter += item.a)
    counter
  }

  @Benchmark
  def measureArray = {
    var counter = 0L
    var i = 0
    while (i < array.length) {
      counter += array(i)
      i += 1
    }
    counter
  }

  @Benchmark
  def measurePacked = {
    var counter = 0L
    pseq.foreach(item => counter += item.a)
    counter
  }
}
