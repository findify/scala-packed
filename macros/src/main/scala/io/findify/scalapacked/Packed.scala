package io.findify.scalapacked

/**
  * Created by shutty on 11/21/16.
  */

import scala.meta._

class Packed extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case cls @ q"class $tpname (...$paramss) { ..$stats }" =>
        def sizeRec(params: Seq[Term.Param]):Term = params match {
          case head :: Nil => q"PackedMember.memberSize(${Term.Name(head.name.toString())})"
          case head :: tail => q"PackedMember.memberSize(${Term.Name(head.name.toString())}) + ${sizeRec(tail)}"
        }
        val size = q"def size:Int = ${sizeRec(paramss.flatten)}"
        val arguments = paramss.flatten
        def gettersRec(prevOffset: Term, params: Seq[Term.Param]):List[Defn] = {
          def getter(field: Term.Param) = q"def ${Term.Name(field.name.toString())}:${Type.Name(field.decltpe.get.toString())} = PackedMember.memberRead[${Type.Name(field.decltpe.get.toString())}](buffer, offset + $prevOffset)"
          params match {
            case field :: Nil => List(getter(field))
            case field :: tail => getter(field) +: gettersRec(q"$prevOffset + PackedMember.memberSize(${Term.Name(field.name.toString())})", tail)
          }
        }
        val getters = gettersRec(q"0", arguments)
        val empty = q"def this() = this(java.nio.ByteBuffer.allocate(0), 0)"
        val implicits = q"import io.findify.scalapacked.types.DefaultTypes._"
        val clazz = q"class $tpname(var buffer: java.nio.ByteBuffer, var offset: Int) extends io.findify.scalapacked.PackedProduct { $implicits; $empty; $size; ..$getters }"


        val impStmt = q"import io.findify.scalapacked.types.DefaultTypes._"
        val bufferStmt = q"val buffer = java.nio.ByteBuffer.allocate(${sizeRec(arguments)})"
        def writeRec(prevOffset: Term, params: Seq[Term.Param]):List[Stat] = {
          def writeMember(field: Term.Param, offset: Term) = q"PackedMember.memberWrite(${Term.Name(field.name.toString())}, buffer, $offset)"
          params match {
            case field :: Nil => List(writeMember(field, prevOffset))
            case field :: tail => writeMember(field, prevOffset) +: writeRec(q"$prevOffset + PackedMember.memberSize(${Term.Name(field.name.toString())})", tail)
          }
        }
        val writeStmt:List[Stat] = arguments.map(arg => q"PackedMember.memberWrite(${Term.Name(arg.name.toString())}, buffer)").toList
        val newStmt = q"new ${Ctor.Primary(List(), Ctor.Name(tpname.value), paramss).name}(buffer, 0)"
        val apply = q"def apply(...$paramss) = { $impStmt; $bufferStmt; ..$writeStmt; $newStmt }"
        val companion = q"object ${Term.Name(tpname.value)} { $apply }"

        Term.Block(List(clazz, companion))
      case _ =>
        abort("cannot match")
    }
  }
}
