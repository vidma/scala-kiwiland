import kwl.Graph.{EdgeTuple, createGraph}
import kwl.Graph
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}

/**
 * Generator of Arbitrary Graphs
 */
object GraphGenerator {
  type T = Char
  // type of edgeID
  type EdgeT = (T, T)
  type EdgeSetT = Set[(T, T)] // type of weightless edge

  val noEdges = Set[EdgeT]()

  def getSmallPosNum = choose[Int](1, 1000).sample.getOrElse(1)

  /** make a weighted edge from unweighted by adding a random distance */
  def appendDist(e: EdgeT): EdgeTuple = (e._1, e._2, getSmallPosNum)

  /** generate an arbitrary Graph */
  def genGraph: Gen[Graph] =
    for (edges_simple <- genUnweightedEdges)
    yield createGraph(edges_simple.toList map appendDist)

  /** generates an Arbitrary and unique set of unweighted edges without self-arcs */
  // TODO: constraint the AVERAGE arity?
  def genUnweightedEdges: Gen[EdgeSetT] = for {
    n1 <- choose[Char]('A', 'Z')
    n2 <- choose[Char]('A', 'Z') if n1 != n2
    g <- oneOf(value(noEdges), genUnweightedEdges)
  } yield g ++ Set((n1, n2))

  // this would allow to generate random graphs implicitly
  implicit lazy val arbGraph: Arbitrary[Graph] = Arbitrary(genGraph)
}
