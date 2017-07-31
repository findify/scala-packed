package io.findify.scalapacked.complex

import io.findify.scalapacked.StructMap
import io.findify.scalapacked.complex.ProductFeedTest.Prod
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class ProductFeedTest extends FlatSpec with Matchers {
  "product feed" should "load 1000 products" in {
    import io.findify.scalapacked.codec._
    import io.findify.scalapacked.codec.generic._
    val items = (0 to 100).map(i => s"prod$i" -> Prod(s"prod$i", s"title $i", Random.nextDouble(), Set(s"kw$i")))
    val feed = StructMap(items: _*)
    val p75 = feed.get("prod75").get
    p75.id shouldBe "prod75"
    p75.title shouldBe "title 75"
    p75.price should be > 0.0
    p75.keywords.contains("kw75") shouldBe true
  }
}

object ProductFeedTest {
  case class Prod(id: String, title: String, price: Double, keywords: Set[String])
}
