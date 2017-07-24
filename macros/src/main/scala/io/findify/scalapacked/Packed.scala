package io.findify.scalapacked

/**
  * Created by shutty on 11/21/16.
  */

import scala.meta._
/*
class Packed extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case cls @ q"class $tpname (...$paramss) { ..$stats }" =>
        def sizeRec(params: Seq[Term.Param]):Term = params match {
          case head :: Nil => q"${Term.Name("io.findify.scalapacked.types."+head.decltpe.get.toString() + "Packed.size")}(${Term.Name(head.name.toString())})"
          case head :: tail => q"${Term.Name("io.findify.scalapacked.types."+head.decltpe.get.toString() + "Packed.size")}(${Term.Name(head.name.toString())}) + ${sizeRec(tail)}"
        }
        val size = q"def size:Int = ${sizeRec(paramss.flatten)}"
        val arguments = paramss.flatten
        def gettersRec(prevOffset: Term, params: Seq[Term.Param]):List[Defn] = {
          def getter(field: Term.Param) = q"def ${Term.Name(field.name.toString())}:${Type.Name(field.decltpe.get.toString())} = ${Term.Name("io.findify.scalapacked.types."+field.decltpe.get.toString() + "Packed.read")}(buffer, offset + $prevOffset)"
          params match {
            case field :: Nil => List(getter(field))
            case field :: tail => getter(field) +: gettersRec(q"$prevOffset + ${Term.Name("io.findify.scalapacked.types."+field.decltpe.get.toString() + "Packed.size")}(${Term.Name(field.name.toString())})", tail)
          }
        }
        val getters = gettersRec(q"0", arguments)
        val empty = q"def this() = this(java.nio.ByteBuffer.allocate(0), 0)"
        val clazz = q"class $tpname(var buffer: java.nio.ByteBuffer, var offset: Int) extends io.findify.scalapacked.PackedProduct {  $empty; $size; ..$getters }"


        val bufferStmt = q"val buffer = java.nio.ByteBuffer.allocate(${sizeRec(arguments)})"
        val writeStmt:List[Stat] = arguments.map(arg => q"${Term.Name("io.findify.scalapacked.types."+arg.decltpe.get.toString() + "Packed.write")}(${Term.Name(arg.name.toString())}, buffer)").toList
        val newStmt = q"new ${Ctor.Primary(List(), Ctor.Name(tpname.value), paramss).name}(buffer, 0)"
        val apply = q"def apply(...$paramss) = { $bufferStmt; ..$writeStmt; $newStmt }"
        val companion = q"object ${Term.Name(tpname.value)} { $apply }"

        Term.Block(List(clazz, companion))
      case _ =>
        abort("cannot match")
    }
  }
}
*/