# Originate Wartremovers

A collection of wart removers that can be used with
[Brian McKenna's Wartremover library](https://github.com/puffnfresh/wartremover).

## Usage

* clone repo
* `sbt package` to build a jar
* copy the jar to `/lib` in your project
* update `build.sbt` in your project with:

  ```scala
  wartremoverClasspaths += "file://" + baseDirectory.value + "/../lib/originate-wartremovers_2.11-0.1.0.jar"
  wartremoverErrors in (Compile, compile) += Wart.custom("com.originate.wartremovers.ExplicitStringEncoding")
  ```

## Wart Removers Available

### ExplicitStringEncoding

Enforce that, when using native string to byte conversions, an explicit encoding type is passed in.
While many environemtns default to run in Utf8 mode, it is not guaranteed, and this can become
confusing in production. Ensuring that the encoding is explicit prevents this from ever being a confusing
bug that needs to be hunted down.

```scala
// bad
"some string".getBytes

// good
import java.nio.charset.StandardCharsets.UTF_8
"some string".getBytes(UTF_8)

// bad
new String(someByteArray)

// good
new String(someByteArray, UTF_8)
```
