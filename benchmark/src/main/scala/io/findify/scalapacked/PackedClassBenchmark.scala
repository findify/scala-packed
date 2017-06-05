package io.findify.scalapacked

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

/**
  * Created by shutty on 11/22/16.
  */

/*@Packed class Foo(a: Int, b: Long)
case class Bar(a: Int, b: Long)

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class PackedClassBenchmark {

  var foo: Foo = _
  var bar: Bar = _
  @Setup
  def setup = {
    foo = Foo(1, 2L)
    bar = Bar(1, 2L)
  }

  @Benchmark
  def measurePacked = {
    foo.a + foo.b
  }

  @Benchmark
  def measureNormal = {
    bar.a + bar.b
  }
}
*/