package io.findify.scalapacked

import java.util.concurrent.TimeUnit

import io.findify.scalapacked.immutable.PackedVector
import org.openjdk.jmh.annotations._

/**
  * Created by shutty on 11/22/16.
  */

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class StructSeqBenchmark {
  var list: Traversable[Int] = _
  val items = (0 to 1000).toArray
  @Param(Array("List", "PackedSeq"))
  var listType: String = _

  @Setup
  def setup = {
    import codec._
    listType match {
      case "List" => list = items.toList
      case "PackedSeq" => list = PackedVector(items.toList: _*)
    }
  }

  @Benchmark
  def foreach = {
    var counter = 0L
    list.foreach(item => counter += item)
    counter
  }

  @Benchmark
  def filter = {
    list.count(_ % 10 == 0)
  }

  @Benchmark
  def head = {
    list.headOption
  }

}

