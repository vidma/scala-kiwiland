import kwl.Graph.{WeightedEdgeTup, NodeId, createGraph}
import kwl.Graph
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}

/**
 * Generator of Arbitrary Graphs
 */
object GraphGenerator {
  // type of edgeID
  type EdgeT = (NodeId, NodeId)
  val noEdges = Set[EdgeT]()

  def getSmallPosNum = choose[Int](1, 1000).sample getOrElse 1

  /** make a weighted edge from unweighted by adding a random distance */
  def makeWeightedEdge(e: EdgeT): WeightedEdgeTup = (e._1, e._2, getSmallPosNum)

  /** generate an arbitrary Graph */
  def genGraph: Gen[Graph] = for {
    edges_simple <- genNonWeightedEdges
  } yield createGraph(edges_simple.toList map makeWeightedEdge)

  /** generates arbitrary unique set of unweighted edges without self-arcs */
  def genNonWeightedEdges: Gen[Set[EdgeT]] = for {
    n1 <- choose[Char]('A', 'Z')
    n2 <- choose[Char]('A', 'Z') if n1 != n2
    g <- oneOf(value(noEdges), genNonWeightedEdges)
  } yield g ++ Set( (n1, n2) )

  // this would allow to generate random graphs implicitly
  implicit lazy val arbGraph: Arbitrary[Graph] = Arbitrary(genGraph)
}
