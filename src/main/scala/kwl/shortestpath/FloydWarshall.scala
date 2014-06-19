package kwl.shortestpath

import math.min
import kwl.KiwilandBase
import kwl.Graph
import kwl.Graph._

/**
 * Even simpler implementation of Floyd-Warshall algorithm
 */
trait FloydWarshall extends ShortestPathBase {
  protected val inf = Long.MaxValue / 4

  /** array dist(from)(to) containing all shortest paths
    * (per graph property: calculated when needed and cached) */
  lazy val allShortestPaths: Array[Array[Long]] = {
    val n = g.nNodes
    val dists = Array.fill[Long](n, n)(inf)
    for ((i, j, d) <- g.edgesNumeric) {
      dists(i)(j) = d
    }
    // update dists based on the possibility of going via some intermediary node k
    for (k <- 0 until n; i <- 0 until n; j <- 0 until n) {
      dists(i)(j) = min(dists(i)(j), dists(i)(k) + dists(k)(j))
    }
    dists
  }

  def shortestPath(src: NodeId, to: NodeId): Long = {
    val d = allShortestPaths(g.nodeNum(src))(g.nodeNum(to))
    if (d == inf) NO_PATH_EXISTS else d
  }

}

object FloydWarshall {
  def apply(g: Graph) = new KiwilandBase(g) with FloydWarshall
}
