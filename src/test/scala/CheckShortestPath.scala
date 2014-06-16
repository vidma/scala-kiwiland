import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop._

import kwl.FloydWarshall.{extractDist, allShortestPaths}
import kwl.DijkstraShortestPath
import kwl.Graph
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
    val dists = allShortestPaths(g) // by Floyd-Warshal
    for (src <- g.nodesList; to <- g.nodesList) {
      val fail = extractDist(g, dists, src, to) != DijkstraShortestPath.shortestPath(g, src, to)
      if (fail) {
        println("Dijkstra do not match Floyd-Warshal", src, to, g)
        ok = false
      }
    }
    ok
  }
}