package kwl

import Graph.{NodeId, EdgeTupleIdx}

case class Edge(from: NodeId, to: NodeId, dist: Int) {
  override def toString: String = s"($from->$to: $dist)"
}

case class Node(id: NodeId, edgesOut: List[Edge])

/**
 * A graph structure based on adjacency-lists.
 *
 * it provides access to a couple of different graph representations:
 * - edgeMap, a dictionary of alphabetically named edges by grouped by outgoing node
 * - edgesNumeric, a flat list of edges named with numeric ids
 *     * the numeric id is the Node's index in the nodesList, and may be obtained via node_id_num
 * - adjacency matrix (0 if no edge else edge distance)
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
  lazy val edgesNumeric: List[EdgeTupleIdx] = edgesList map {
    e => (nodeNum(e.from), nodeNum(e.to), e.dist) }

}

object Graph {
  type NodeId = Char
  type Route = List[NodeId]
  type WeightedEdgeTup = (NodeId, NodeId, Int)
  type RouteDistT = Long // TODO: use this constant everywhere?


  /** a tuple representing an edge using numeric node ids */
  type EdgeTupleIdx = (Int, Int, Int)

  /** main Graph "constructor" */
  def apply(edgeList: List[Edge]): Graph = new Graph(edgeList)

  /** create a graph from edge definitions (string like AB7) */
  def createGraph(lines: Array[String]): Graph = {
    val edges = for (List(from, to, dist) <- lines map { _.toList } )
    yield Edge(from, to, Integer.parseInt(dist.toString))
    Graph(edges.toList)
  }

  /** create a graph from edge definitions (tuples) */
  def createGraph(edge_list: List[WeightedEdgeTup]): Graph =
    Graph(edge_list map { case (from, to, dist) => Edge(from, to, dist) } )

}
