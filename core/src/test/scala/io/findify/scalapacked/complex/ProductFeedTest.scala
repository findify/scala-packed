package io.findify.scalapacked.complex

import org.scalatest.{FlatSpec, Matchers}

class ProductFeedTest extends FlatSpec with Matchers {

}

object ProductFeedTest {
  case class Prod(id: String, title: String, price: Double, keywords: Set[String])
}
