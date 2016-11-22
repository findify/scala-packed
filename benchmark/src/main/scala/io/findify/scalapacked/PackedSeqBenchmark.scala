package io.findify.scalapacked

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

/**
  * Created by shutty on 11/22/16.
  */

@Packed class Intp(a:Int)
case class Intc(a:Int)

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class PackedSeqBenchmark {
  var list: List[Intc] = _
  var array: Array[Int] = _
  var pseq: PackedSeq[Intp] = _

  @Setup
  def setup = {
    array = (0 to 1000).toArray
    list = array.map(i => Intc(i)).toList
    pseq = PackedSeq[Intp](array.map(i => Intp(i)).toSeq)
  }

  @Benchmark
  def measureList = {
    var counter = 0
    list.foreach(item => counter += item.a)
    counter
  }

  @Benchmark
  def measureArray = {
    var counter = 0
    var i = 0
    while (i < array.length) {
      counter += array(i)
      i += 1
    }
    counter
  }

  @Benchmark
  def measurePacked = {
    var counter = 0
    pseq.foreach(item => counter += item.a)
    counter
  }
}
