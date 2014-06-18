package kwl

import scala.math.min
import collection.mutable
import Graph.NodeId
import kwl.utils.SimpleUpdatableMinQueue

trait ShortestPathAlg extends KiwilandBase {
  /**
   * @return length of the shortest path or -1 if it don't exist
   */
  def shortestPath(src: NodeId, to: NodeId): Long

  val NO_PATH = -1
}

/**
 * A basic implementation  of Dijkstra's Shortest Path algorithm
 * based on a Priority Queue
 */
trait DijkstraShortestPath extends ShortestPathAlg {

  def shortestPath(src: NodeId, dest: NodeId): Long = {
    val dists = mutable.Map.empty[NodeId, Long]
    val visited = mutable.Set.empty[NodeId]
    val q = new SimpleUpdatableMinQueue[Long, NodeId]()
    // enqueue the edges directly reachable from src
    for (edge <- g.outEdges(src)) {
      q += ((edge.dist, edge.to))
      dists(edge.to) = edge.dist
    }
    while (!q.isEmpty) {
      val (cur_dist, current) = q.popMin()
      if (current == dest) {
        return cur_dist
      }
      visited += current
      for (Edge(_, v, edge_len) <- g.outEdges(current) if !visited.contains(v);
           new_dist = cur_dist + edge_len) {
        // if new dist is better, enqueue it or update its priority
        if (!dists.isDefinedAt(v) || new_dist < dists(v)) {
          q.updateKey(new_dist, v)
          dists(v) = new_dist
        }
      }
    }
    dists.get(dest) getOrElse NO_PATH
  }
}

object DijkstraShortestPath {
  def apply(g: Graph) = new KiwilandBase(g) with DijkstraShortestPath
}

/**
 * Even simpler implementation of Floyd-Warshall algorithm
 */
trait FloydWarshall extends ShortestPathAlg {
  protected val inf = Long.MaxValue / 4

  /** array dist(from)(to) containing all shortest paths
    * (per graph property: calculated when needed and cached) */
  lazy val allShortestPaths: Array[Array[Long]] = {
    val n = g.nNodes
    val dists = Array.fill[Long](n, n)(inf)
    // init with direct edge distances
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
    if (d == inf) NO_PATH else d
  }

}

object FloydWarshall {
  def apply(g: Graph) = new KiwilandBase(g) with FloydWarshall
}