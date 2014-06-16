import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop._

import kwl.{FloydWarshall, DijkstraShortestPath, Graph}
import GraphGenerator.arbGraph


/**
 * we generate "random" graphs which are fed into different shortest path algorithms for comparing their results
 *
 * Floyd-Warshall is very simple, and shall validate the Dijkstra implementation
 */
object CheckShortestPath extends Properties("ShortestPath") {
  type T = Char
  val ORDERING = Ordering.by((x: (T, Int)) => x._2)

  val propDijkstraFloydMatches = forAll { (g: Graph) =>
    var ok = true

    val floyd = FloydWarshall(g)
    val dijkstra = DijkstraShortestPath(g)
    val dists = floyd.allShortestPaths() // by Floyd-Warshal

    for (src <- g.nodesList; to <- g.nodesList) {
      val fail = floyd.extractDist(dists, src, to) != dijkstra.shortestPath(src, to)
      if (fail) {
        println("Dijkstra do not match Floyd-Warshal", src, to, g)
        ok = false
      }
    }
    ok
  }
}