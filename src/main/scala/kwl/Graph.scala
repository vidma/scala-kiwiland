package kwl

import Graph.NodeId

case class Edge(from: NodeId, to: NodeId, dist: Int) {
  override def toString: String = s"$from$to$dist"
}

case class Node(id: NodeId, edgesOut: List[Edge])

/**
 * A graph structure based on adjacency-lists.
 *
 * based on edgeList, initial flat list of edges (e.g. A B 7), this class provides
 * a couple of graph representations (calculated lazily on demand):
 * - edgesNumeric, a flat list of edges named with numeric ids
 *    * the numeric id is the Node's index in the nodesList, and may be obtained via nodeNum
 * - edgeMap, a dictionary of alphabetically named edges by grouped by outgoing node
 * - adjMatrixDist, an adjacency matrix representation
 * - nodesList - list of all node ids (e.g. A B C D ..)
 */
class Graph(val edgesList: List[Edge]) {
  /** map of outgoing edges: a dictionary of alphabetically named edges, grouped by outgoing node */
  lazy val outEdges: Map[NodeId, List[Edge]] =
    Map.empty.withDefaultValue(List[Edge]()) ++ edgesList.groupBy(_.from)

  /** set of all nodes */
  lazy val nodesList: List[NodeId] =
    (Set.empty ++ edgesList.map(_.from) ++ edgesList.map(_.to)).toList

  /** # of nodes in the graph */
  val nNodes: Int = nodesList.size

  /** a map allowing to rename nodeName into its index */
  lazy val nodeNum = nodesList.zipWithIndex.toMap

  /** edgeList using numeric node ids */
  lazy val edgesNumeric: List[(Int, Int, Int)] = edgesList map {
    e => (nodeNum(e.from), nodeNum(e.to), e.dist) }

  /** Adjacency Matrix: Map (from, to) => dist */
  lazy val adjMatrixDist: Map[(NodeId, NodeId), Int] =
    Map.empty ++ (edgesList map { case Edge(from, to, dist) => (from, to) -> dist })

  override def toString: String = {
    val edges = edgesList mkString ","
    s"Graph($edges)"
  }

}

object Graph {
  type NodeId = Char
  type DistT = Int // TODO: shall I use this instead?
  type RouteDistT = Long // TODO: use this constant everywhere?

  /** main Graph "constructor" */
  def apply(edgeList: List[Edge]): Graph = new Graph(edgeList)

  /** create a graph from edge definitions (string like AB7) */
  def createGraph(lines: Array[String]): Graph = {
    val edges = for (List(from, to, dist) <- lines map { _.toList } )
    yield Edge(from, to, Integer.parseInt(dist.toString))
    Graph(edges.toList)
  }

}
