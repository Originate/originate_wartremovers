package com.originate.wartremovers

import org.brianmckenna.wartremover.{WartTraverser, WartUniverse}

object ExplicitStringEncoding extends WartTraverser {

  val StringGetBytesError =
    """Use explicit encoding flag, ex: "string".getBytes(UTF_8)"""
  val StringConstructorWithBytesError =
    """Use explicit encoding flag when constructing strings, ex: new String(bytes, UTF_8)"""

  def apply(u: WartUniverse): u.Traverser = {
    import u.universe._

    val GetBytesMethodName = TermName("getBytes")

    new u.Traverser {
      override def traverse(tree: Tree): Unit = {
        tree match {
          case t if hasWartAnnotation(u)(t) =>
          case Apply(Select(left, GetBytesMethodName), args) if left.tpe <:< typeOf[String] && args.isEmpty =>
            u.error(tree.pos, StringGetBytesError)
            super.traverse(tree)
          case q"new scala.this.Predef.String(..$args)" if args.size <= 1 =>
            u.error(tree.pos, StringConstructorWithBytesError)
            super.traverse(tree)
          case q"new java.lang.String(..$args)" if args.size <= 1 =>
            u.error(tree.pos, StringConstructorWithBytesError)
            super.traverse(tree)
          case _ =>
            super.traverse(tree)
        }
      }
    }
  }

}
