// src/test/scala/progscala3/implicits/CustomStringInterpolatorSuite.scala
package progscala3.implicits

import munit._

class CustomStringInterpolatorSuite extends FunSuite:

  object JSONToMapInterpolator:
    implicit class mapForStringContext(val sc: StringContext):   // <1>
      def map(values: String*): Map[String, String] =            // <2>
        val keyRE = """^[\s{,]*(\S+):\s*""".r                    // <3>
        val keys = sc.parts map {                                // <4>
          case keyRE(key) => key
          case str => str
        }
        val kvs = keys zip values                                // <5>
        kvs.toMap                                                // <6>

  import JSONToMapInterpolator._

  val name = "Dean Wampler"
  val book = "Programming Scala, Third Edition"

  test("""A custom interpolator is invoked with name"" """) {
    val map1 = map"{name: $name, book: $book}"                     // <7>
    assert(map1 == Map(
      "name" -> "Dean Wampler",
      "book" -> "Programming Scala, Third Edition"))
  }

  test("A custom interpolator can use variable interpolation.") {
    val publisher = "O'Reilly"
    val map2 = map"{name: $name, book: $book, publisher: $publisher}"
    assert(map2 == Map(
      "name" -> "Dean Wampler",
      "book" -> "Programming Scala, Third Edition",
      "publisher" -> "O'Reilly"))
  }
