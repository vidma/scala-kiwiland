package kwl.shortestpath

import collection.mutable
import kwl.Graph
import Graph.NodeId
import kwl.utils.SimpleUpdatableMinQueue
import kwl.KiwilandBase
import kwl.{Graph,Edge}

/**
 * A basic implementation  of Dijkstra's Shortest Path algorithm
 * based on a Priority Queue
 */
trait DijkstraShortestPath extends ShortestPathBase {

  def shortestPath(src: NodeId, dest: NodeId): Long = {
    val dists = mutable.Map.empty[NodeId, Long].withDefaultValue(Long.MaxValue)
    val visited = mutable.Set.empty[NodeId]
    val q = new SimpleUpdatableMinQueue[Long, NodeId]()
    // enqueue the edges directly reachable from src
    for (edge <- g.outEdges(src)) {
      q +=(edge.dist, edge.to)
      dists(edge.to) = edge.dist
    }
    while (!q.isEmpty) {
      // pop the closest vertex, s
      val (_, s) = q.popMin()
      if (s == dest) {
        return dists(s)
      }
      visited += s
      // if some edge, s->v, improves dist of vertex v, update v's priority in the queue
      for (Edge(_, v, edge_len) <- g.outEdges(s) if !visited.contains(v);
           better_dist = dists(s) + edge_len if better_dist < dists(v)) {
        q.updateKey(better_dist, v)
        dists(v) = better_dist
      }
    }
    dists.get(dest) getOrElse NO_PATH_EXISTS
  }
}

object DijkstraShortestPath {
  def apply(g: Graph) = new KiwilandBase(g) with DijkstraShortestPath
}
