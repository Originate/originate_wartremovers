package com.originate.wartremovers

import org.brianmckenna.wartremover.test.WartTestTraverser
import org.scalatest.FreeSpec

import java.nio.charset.StandardCharsets.UTF_8

class ExplicitStringEncodingSpec extends FreeSpec {

  "obeys SuppressWarnings" in {
    val result = WartTestTraverser(ExplicitStringEncoding) {
      @SuppressWarnings(Array("com.originate.wartremovers.ExplicitStringEncoding"))
      def test = "some string".getBytes
    }
    assertResult(List.empty, "result.errors")(result.errors)
    assertResult(List.empty, "result.warnings")(result.warnings)
  }

  "String#getBytes" - {

    "on literals" - {

      "does not compile" in {
        val result = WartTestTraverser(ExplicitStringEncoding) {
          "some string".getBytes
        }
        assertResult(List(ExplicitStringEncoding.StringGetBytesError), "result.errors")(result.errors)
      }

      "compiles with an explicit flag" in {
        val result = WartTestTraverser(ExplicitStringEncoding) {
          "some string".getBytes(UTF_8)
        }
        assertResult(List.empty, "result.errors")(result.errors)
      }

    }

    "on string variables" - {

      "does not compile" in {
        val result = WartTestTraverser(ExplicitStringEncoding) {
          val x = "some string"
          x.getBytes
        }
        assertResult(List(ExplicitStringEncoding.StringGetBytesError), "result.errors")(result.errors)
      }

      "compiles with an explicit flag" in {
        val result = WartTestTraverser(ExplicitStringEncoding) {
          val x = "some string"
          x.getBytes(UTF_8)
        }
        assertResult(List.empty, "result.errors")(result.errors)
      }

    }

  }

  "String constructor" - {

    "does not compile without an encoding flag" in {
      val result = WartTestTraverser(ExplicitStringEncoding) {
        val bytes = "ok".toCharArray map (_.toByte)
        new String(bytes)
      }
      assertResult(List(ExplicitStringEncoding.StringConstructorWithBytesError), "result.errors")(result.errors)
    }

    "does not compile without an encoding flag if using native java String" in {
      val result = WartTestTraverser(ExplicitStringEncoding) {
        val bytes = "ok".toCharArray map (_.toByte)
        new java.lang.String(bytes)
      }
      assertResult(List(ExplicitStringEncoding.StringConstructorWithBytesError), "result.errors")(result.errors)
    }

    "compiles with an explicit encoding flag" in {
      val result = WartTestTraverser(ExplicitStringEncoding) {
        val bytes = "ok".toCharArray map (_.toByte)
        new String(bytes, UTF_8)
      }
      assertResult(List.empty, "result.errors")(result.errors)
      assertResult(List.empty, "result.warnings")(result.warnings)
    }

  }

}
