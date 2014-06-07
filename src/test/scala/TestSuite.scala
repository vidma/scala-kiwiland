import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.lang.Exception
import kwl._
import TripsCounter.CType._


@RunWith(classOf[JUnitRunner])
class TestSuite extends FunSuite with Checkers {
  val testInput = "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7"
  val kwl = Kiwiland(testInput)

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

  test("test 6") {
    assertResult(2) {
      TripsCounter(MAX_STOPS, kwl.g, 'C', 'C', 3).count
    }
  }

  test("test 7") {
    assertResult(3) {
      TripsCounter(EXACT_STOPS, kwl.g, 'A', 'C', 4).count
    }
  }

  test("test 8") {
    assert(kwl.shortestPath('A', 'C') == 9)
  }

  test("test 9") {
    assert(kwl.shortestPath('B', 'B') == 9)
  }

  test("test 10") {
    assertResult(7) {
      TripsCounter(MAX_DIST, kwl.g, 'C', 'C', 0, 30).count
    }
  }

  /*
  var scalachecks = List(
    CheckUpdatableMinQueue.propFindMinSorted, CheckUpdatableMinQueue.propUpdateWorks,
    CheckShortestPath.propDijkstraFloydMatches
  )
  for (scheck <- scalachecks) {
    test("scalacheck: " + scheck){
      check(scheck)
    }
  }
*/

  test("run ScalaCheck: propFindMinSorted") {
    // TODO: how to make it verbose?
    check(CheckUpdatableMinQueue.propFindMinSorted)
  }

  test("run ScalaCheck: propUpdateWorks") {
    check(CheckUpdatableMinQueue.propUpdateWorks)
  }

  test("run ScalaCheck: Dijkstra and Floyd-Warshall Matches?") {
    check(CheckShortestPath.propDijkstraFloydMatches)
  }

  test("test 8: floyd Warshall") {
    assertResult(9) {
      AllShortestPaths.shortestPath(kwl.g, 'A', 'C')
    }
  }

  test("test 9: floyd Warshall") {
    assertResult(9) {
      AllShortestPaths.shortestPath(kwl.g, 'B', 'B')
    }
  }

}