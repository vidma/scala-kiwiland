import kwl.Graph.NodeId
import kwl.{Graph, Edge}
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}

/**
 * Generator of Arbitrary Graphs
 */
object GraphGenerator {
  type UnweightedEdge = (NodeId, NodeId)
  val noEdges = Set[UnweightedEdge]()

  /** create a graph from edge definitions and weights */
  def createGraph(edges: List[UnweightedEdge], weights: List[Int]): Graph =
    Graph(edges zip weights map { case ((from, to), dist) => Edge(from, to, dist) } )

  /** generate an arbitrary Graph */
  def genGraph: Gen[Graph] = for {
    edges <- genNonWeightedEdges
    weights <- listOfN(edges.size, choose[Int](1, 1000))
  } yield createGraph(edges.toList, weights) // add a weight to each edge

  /** generates arbitrary unique set of unweighted edges without self-arcs */
  def genNonWeightedEdges: Gen[Set[UnweightedEdge]] = for {
    n1 <- choose[Char]('A', 'Z')
    n2 <- choose[Char]('A', 'Z') if n1 != n2
    g <- oneOf(value(noEdges), genNonWeightedEdges)
  } yield g ++ Set( (n1, n2) )

  // this would allow to generate random graphs implicitly
  implicit lazy val arbGraph: Arbitrary[Graph] = Arbitrary(genGraph)
}
