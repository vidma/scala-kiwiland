import kwl.Graph.NodeId
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop._

import kwl.shortestpath.{FloydWarshall, DijkstraShortestPath}
import kwl.Graph
import GraphGenerator.arbGraph


/**
 * we generate "random" graphs which are fed into different shortest path algorithms for comparing their results
 *
 * Floyd-Warshall is very simple, and shall validate the Dijkstra implementation
 */
object CheckShortestPath extends Properties("ShortestPath") {
  val ORDERING = Ordering.by((x: (NodeId, Int)) => x._2)

  property("DijkstraMatchesFloyd") = forAll { (g: Graph) =>
    var ok = true
    val floyd = FloydWarshall(g)
    val dijkstra = DijkstraShortestPath(g)

    for (src <- g.nodesList; to <- g.nodesList) {
      val fail = floyd.shortestPath(src, to) != dijkstra.shortestPath(src, to)
      if (fail) {
        println("Dijkstra do not match Floyd-Warshal", src, to, g)
        ok = false
      }
    }

    ok
  }
}