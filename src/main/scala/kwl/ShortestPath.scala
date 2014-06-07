package kwl

import scala.math.min
import Graph.NodeId

trait ShortestPathAlg {
  /**
   * @return length of the shortest path or -1 if it don't exist
   */
  def shortestPath(g: Graph, src: NodeId, to: NodeId): Int

  val NO_PATH = -1
}

/**
 * A basic implementation  of Dijkstra's Shortest Path algorithm
 * based on a Priority Queue
 */
object DijkstraShortestPath extends ShortestPathAlg {
  def shortestPath(g: Graph, src: NodeId, to: NodeId): Int = {
    val dists = collection.mutable.Map[NodeId, Int]()
    val visited = collection.mutable.Set[NodeId]()
    val q = new SimpleUpdatableMinQueue[NodeId]()

    // enqueue the edges directly reachable from src
    for (edge <- g.edgesMap(src)) {
      q.enqueue(edge.to, edge.dist)
      dists(edge.to) = edge.dist
    }

    while (!q.isEmpty) {
      val (from, cur_dist) = q.findMin
      visited += from

      for (Edge(_, v, edge_dist) <- g.edgesMap(from) if !visited.contains(v)) {
        // if new dist is better enqueue/update its priority
        val new_dist = cur_dist + edge_dist
        if (!dists.isDefinedAt(v) || new_dist < dists(v)) {
          q.updatePriority(v, new_dist)
          dists(v) = new_dist
        }
      }
    }
    dists.get(to).getOrElse(NO_PATH)
  }
}

/**
 * Even simpler implementation of Floyd-Warshall algorithm
 */
object AllShortestPaths extends ShortestPathAlg {
  val inf: Int = Int.MaxValue / 5
  // use a value low enough to avoid overflows
  type DistsArray = Array[Array[Int]]

  def shortestPath(g: Graph, src: NodeId, to: NodeId): Int =
    extractDist(g, allShortestPaths(g), src, to)

  /**
   * @return array dist(from)(to) containing all shortest paths
   */
  def allShortestPaths(g: Graph): DistsArray = {
    val n = g.nodesList.size
    val dists = Array.fill[Int](n, n)(inf)

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

  def extractDist(g: Graph, dist: DistsArray, src: NodeId, to: NodeId): Int = {
    val d = dist(g.node_id_num(src))(g.node_id_num(to))
    if (d == inf) NO_PATH else d
  }

}