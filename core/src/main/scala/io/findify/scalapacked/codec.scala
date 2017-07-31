package io.findify.scalapacked
import io.findify.scalapacked.pool.MemoryPool
import io.findify.scalapacked.types._
import shapeless.{:+:, ::, CNil, Coproduct, HList, HNil, LabelledTypeClass, LabelledTypeClassCompanion, Lazy}

object codec {
  implicit val stringCodec: Codec[String] = StringCodec
  implicit val intCodec: Codec[Int] = IntCodec
  implicit val floatCodec: Codec[Float] = FloatCodec
  implicit val longCodec: Codec[Long] = LongCodec
  implicit val doubleCodec: Codec[Double] = DoubleCodec
  implicit val noneCodec = OptionCodec.NoneCodec
  implicit def someCodec[T](implicit cdc: Codec[T]) = new OptionCodec.SomeCodec[T]()(cdc)
  implicit def mapCodec[K,V](implicit kc: Codec[K], vc: Codec[V]) = new MapCodec[K,V]()(kc, vc)
  implicit def setCodec[K](implicit kc: Codec[K]) = new SetCodec[K]()(kc)

  def deriveCodec[T](implicit codec: Lazy[Codec[T]]) = codec.value

  object generic extends LabelledTypeClassCompanion[Codec] {

    object typeClass extends LabelledTypeClass[Codec] {
      override def emptyProduct: Codec[HNil] = new Codec[HNil] {
        override def read(buffer: MemoryPool, offset: Int): HNil = HNil
        override def size(buffer: MemoryPool, offset: Int): Int = 0
        override def size(item: HNil): Int = 0
        override def write(value: HNil, buffer: MemoryPool): Int = buffer.size
      }

      override def product[H, T <: HList](name: String, ch: Codec[H], ct: Codec[T]): Codec[::[H, T]] = new Codec[::[H, T]] {
        override def read(buffer: MemoryPool, offset: Int): ::[H, T] = {
          val headSize = ch.size(buffer, offset)
          val head = ch.read(buffer, offset)
          val tailSize = ct.size(buffer, offset + headSize)
          val tail = ct.read(buffer, offset + headSize)
          head :: tail
        }
        override def size(buffer: MemoryPool, offset: Int): Int = size(read(buffer, offset)) // super bad
        override def size(item: ::[H, T]): Int = {
          val head = ch.size(item.head)
          val tail = ct.size(item.tail)
          head + tail
        }
        override def write(value: ::[H, T], buffer: MemoryPool): Int = {
          val head = ch.write(value.head, buffer)
          val tail = ct.write(value.tail, buffer)
          head
        }
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
        override def read(buffer: MemoryPool, offset: Int): F = {
          from(instance.read(buffer, offset + 4))
        }
        override def size(buffer: MemoryPool, offset: Int): Int = {
          buffer.readInt(offset)
        }
        override def size(item: F): Int = instance.size(to(item))
        override def write(value: F, buffer: MemoryPool): Int = {
          val start = buffer.size
          buffer.writeInt(0)
          instance.write(to(value), buffer)
          val end = buffer.size
          buffer.writeInt(end - start, start)
          start
        }
      }
    }
  }

}
