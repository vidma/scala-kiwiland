/**
 * The main file
 */
package kwl

import Graph.{NodeId, createGraph}

abstract class Kwl(val g: Graph)

class Kiwiland(override val g: Graph) extends Kwl(g) with RouteDist {
  /**
   * get Distance of the shortest path
   */
  def shortestPath(from: NodeId, to: NodeId) = DijkstraShortestPath.shortestPath(g, from, to)
}

object Kiwiland {
  def apply(str: String): Kiwiland = new Kiwiland(createGraph(str.split(", ")))
}
