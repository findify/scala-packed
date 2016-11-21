package io.findify.scalapacked

/**
  * Created by shutty on 11/21/16.
  */

import scala.meta._

class Packed extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case q"class $tpname (...$paramss) { ..$stats }" => q"class $tpname (...$paramss) {..$stats; def size: Int = 1}; object $tpname { def hello: Int = 1 }"
      case _ => abort("cannot match class")
    }
  }
}
