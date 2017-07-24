package io.findify.scalapacked

trait Codec[T] {
  def build(offset: Int): T
}
