package io.findify.scalapacked

import java.util.concurrent.TimeUnit

import io.findify.scalapacked.StructSeq.StructCanBuildFrom
import io.findify.scalapacked.pool.MemoryPool
import io.findify.scalapacked.types.IntPacked
import org.openjdk.jmh.annotations._

/**
  * Created by shutty on 11/22/16.
  */

case class Intp(a:Int) extends Struct
case class Intc(a:Int)

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class PackedSeqBenchmark {
  var list: List[Intc] = _
  var array: Array[Int] = _
  var pseq: StructSeq[Intp] = _

  implicit val encoder = new Encoder[Intp] {
    override def write(value: Intp, buffer: MemoryPool): Int = {
      IntPacked.write(8, buffer)
      IntPacked.write(value.a, buffer)
    }
  }

  implicit val decoder = new Decoder[Intp] {
    override def read(buffer: MemoryPool, offset: Int): Intp = Intp(IntPacked.read(buffer, offset + 4))
    override def size(buffer: MemoryPool, offset: Int): Int = 8
  }

  @Setup
  def setup = {
    implicit def cbf = new StructCanBuildFrom[Intp]()
    array = (0 to 1000).toArray
    list = array.map(i => Intc(i)).toList
    pseq = array.map(i => Intp(i))
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
