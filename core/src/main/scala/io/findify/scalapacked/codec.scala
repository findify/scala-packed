package io.findify.scalapacked
import io.findify.scalapacked.pool.MemoryPool
import io.findify.scalapacked.types._
import shapeless.{:+:, ::, CNil, Coproduct, HList, HNil, LabelledTypeClass, LabelledTypeClassCompanion}

object codec {
  implicit val stringCodec: Codec[String] = StringCodec
  implicit val intCodec: Codec[Int] = IntCodec
  implicit val floatCodec: Codec[Float] = FloatCodec
  implicit val longCodec: Codec[Long] = LongCodec

  object generic extends LabelledTypeClassCompanion[Codec] {
    implicit def intCodec: Codec[Int] = IntCodec
    implicit def floatCodec: Codec[Float] = FloatCodec
    implicit def stringCodec: Codec[String] = StringCodec

    object typeClass extends LabelledTypeClass[Codec] {
      override def emptyProduct: Codec[HNil] = new Codec[HNil] {
        override def read(buffer: MemoryPool, offset: Int): HNil = HNil
        override def size(buffer: MemoryPool, offset: Int): Int = 0
        override def size(item: HNil): Int = 0
        override def write(value: HNil, buffer: MemoryPool): Int = 0
      }

      override def product[H, T <: HList](name: String, ch: Codec[H], ct: Codec[T]): Codec[::[H, T]] = new Codec[::[H, T]] {
        override def read(buffer: MemoryPool, offset: Int): ::[H, T] = ???
        override def size(buffer: MemoryPool, offset: Int): Int = ???
        override def size(item: ::[H, T]): Int = {
          val head = ch.size(item.head)
          val tail = ct.size(item.tail)
          head + tail
        }
        override def write(value: ::[H, T], buffer: MemoryPool): Int = ???
      }

      override def emptyCoproduct: Codec[CNil] = new Codec[CNil] {
        override def read(buffer: MemoryPool, offset: Int): CNil = ???
        override def size(buffer: MemoryPool, offset: Int): Int = ???
        override def size(item: CNil): Int = ???
        override def write(value: CNil, buffer: MemoryPool): Int = ???
      }

      override def coproduct[L, R <: Coproduct](name: String, cl: => Codec[L], cr: => Codec[R]): Codec[:+:[L, R]] = new Codec[:+:[L, R]] {
        override def read(buffer: MemoryPool, offset: Int): :+:[L, R] = ???
        override def size(buffer: MemoryPool, offset: Int): Int = ???
        override def size(item: :+:[L, R]): Int = ???
        override def write(value: :+:[L, R], buffer: MemoryPool): Int = ???
      }

      override def project[F, G](instance: => Codec[G], to: (F) => G, from: (G) => F): Codec[F] = new Codec[F] {
        override def read(buffer: MemoryPool, offset: Int): F = from(instance.read(buffer, offset))
        override def size(buffer: MemoryPool, offset: Int): Int = instance.size(buffer, offset)
        override def size(item: F): Int = instance.size(to(item))
        override def write(value: F, buffer: MemoryPool): Int = instance.write(to(value), buffer)
      }
    }
  }

}
