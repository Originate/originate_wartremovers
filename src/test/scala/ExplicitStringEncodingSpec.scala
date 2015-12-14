package com.originate.wartremovers

import org.brianmckenna.wartremover.test.WartTestTraverser
import org.scalatest.FreeSpec

import java.nio.charset.StandardCharsets.UTF_8

class Utf8StringSpec extends FreeSpec {

  "obeys SuppressWarnings" in {
    val result = WartTestTraverser(Utf8String) {
      @SuppressWarnings(Array("com.originate.wartremovers.Utf8String"))
      def test = "some string".getBytes
    }
    assertResult(List.empty, "result.errors")(result.errors)
    assertResult(List.empty, "result.warnings")(result.warnings)
  }

  "String#getBytes" - {

    "on literals" - {

      "does not compile" in {
        val result = WartTestTraverser(Utf8String) {
          "some string".getBytes
        }
        assertResult(List(Utf8String.StringGetBytesError), "result.errors")(result.errors)
      }

      "compiles with an explicit flag" in {
        val result = WartTestTraverser(Utf8String) {
          "some string".getBytes(UTF_8)
        }
        assertResult(List.empty, "result.errors")(result.errors)
      }

    }

    "on string variables" - {

      "does not compile" in {
        val result = WartTestTraverser(Utf8String) {
          val x = "some string"
          x.getBytes
        }
        assertResult(List(Utf8String.StringGetBytesError), "result.errors")(result.errors)
      }

      "compiles with an explicit flag" in {
        val result = WartTestTraverser(Utf8String) {
          val x = "some string"
          x.getBytes(UTF_8)
        }
        assertResult(List.empty, "result.errors")(result.errors)
      }

    }

  }

  "String constructor" - {

    "does not compile without an encoding flag" in {
      val result = WartTestTraverser(Utf8String) {
        val bytes = "ok".toCharArray map (_.toByte)
        new String(bytes)
      }
      assertResult(List(Utf8String.StringConstructorWithBytesError), "result.errors")(result.errors)
    }

    "compiles with an explicit encoding flag" in {
      val result = WartTestTraverser(Utf8String) {
        val bytes = "ok".toCharArray map (_.toByte)
        new String(bytes, UTF_8)
      }
      assertResult(List.empty, "result.errors")(result.errors)
      assertResult(List.empty, "result.warnings")(result.warnings)
    }

  }

}
