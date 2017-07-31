package io.findify.scalapacked

import java.time.{LocalDateTime, ZoneOffset}

import io.findify.scalapacked.ShapelessDeriveTest._
import io.findify.scalapacked.pool.MemoryPool
import io.findify.scalapacked.types.{Codec, FloatCodec, IntCodec, StringCodec}
import org.scalatest.{FlatSpec, Matchers}
import shapeless._

class ShapelessDeriveTest extends FlatSpec with Matchers {

  implicit def size[T](a: T)(implicit codec: Codec[T]):Int = codec.size(a)


  it should "create derivation for plain case classes" in {
    import codec.generic._
    size(One(1, 1.0f, "foo")) shouldBe 15
  }

  it should "create derivation for nested case classes" in {
    import codec.generic._
    size(Wrapped("foo", Nested(1))) shouldBe 11
  }

  it should "derive classes with custom types" in {
    import codec.generic._
    implicit val dtCodec = new Codec[SuperDate] {
      override def read(buffer: MemoryPool, offset: Int): SuperDate = SuperDate(
        year = buffer.readInt(offset),
        month = buffer.readInt(offset + 4),
        day = buffer.readInt(offset + 8)
      )
      override def size(buffer: MemoryPool, offset: Int): Int = 12
      override def size(item: SuperDate): Int = 12
      override def write(value: SuperDate, buffer: MemoryPool): Int = {
        val offset = buffer.writeInt(value.year)
        buffer.writeInt(value.month)
        buffer.writeInt(value.day)
        offset
      }
    }
    size(CustomOne(1, SuperDate(1970, 1, 1))) shouldBe 16
  }
}

object ShapelessDeriveTest {
  case class One(i: Int, f: Float, s: String)
  case class Nested(i: Int)
  case class Wrapped(s: String, n: Nested)

  case class CustomOne(i: Int, d: SuperDate)

  case class SuperDate(year: Int, month: Int, day: Int)
}