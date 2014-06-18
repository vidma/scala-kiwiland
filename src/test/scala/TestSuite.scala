import org.scalacheck.Properties
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.lang.Exception
import kwl._
import TripsCounter._


@RunWith(classOf[JUnitRunner])
class TestSuite extends FunSuite with Checkers {
  val testInput = "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7"
  val kwl = Kiwiland(testInput)

  /**
   * run scalacheck for provided properties class - this provide
   * much better & more detailed report than just running check(p.propertyName)
    */
  def runScalaCheck(p: Properties) = {
    val args = List( "-minSuccessfulTests", "500").toArray
    assertResult(0) {
      p.mainRunner(args)
    }
  }


  test("test 1-4") {
    assert(kwl.routeDist("ABC") == 9)
    assert(kwl.routeDist("AD") == 5)
    assert(kwl.routeDist("ADC") == 13)
    assert(kwl.routeDist("AEBCD") == 22)
  }

  test("test 5") {
    intercept[Exception] {
      kwl.routeDist("AED")
    }
  }

  test("test 5: error msg") {
    assertResult(kwl.MsgNoRouteFound) {
      kwl.routeDistOrMsg("AED")
    }
  }

  test("test 6") {
    assertResult(2) {
      kwl.countTrips('C', 'C', MaxEdgeNumCond(3))

    }
  }

  test("test 7") {
    assertResult(3) {
      kwl.countTrips('A', 'C', ExactEdgeNumCond(4))
    }
  }

  test("test 8") {
    assertResult(9) {
      kwl.shortestPath('A', 'C')
    }
  }

  test("test 9") {
    assertResult(9) {
      kwl.shortestPath('B', 'B')
    }
  }

  test("test 10") {
    assertResult(7) {
      kwl.countTrips('C', 'C', MaxDistCond(30))
    }
  }

  test("test 8: with floyd Warshall") {
    assertResult(9) {
      FloydWarshall(kwl.g).shortestPath('A', 'C')
    }
  }

  test("test 9: with floyd Warshall") {
    assertResult(9) {
      FloydWarshall(kwl.g).shortestPath('B', 'B')
    }
  }

  test("ScalaCheck: DP vs complete recursion") {
    runScalaCheck(CheckTripsCounter)
  }

  test("ScalaCheck: CheckUpdatableMinQueue") {
    runScalaCheck(CheckUpdatableMinQueue)
  }

  test("ScalaCheck: Dijkstra and Floyd-Warshall Matches?") {
    runScalaCheck(CheckShortestPath)
  }

}