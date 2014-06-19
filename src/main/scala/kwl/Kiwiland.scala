/**
 * The main file
 */
package kwl

import Graph.createGraph
import kwl.shortestpath.DijkstraShortestPath
import kwl.TripsCounter.{MaxDistCond, MaxEdgeNumCond, ExactEdgeNumCond}

/** The base class which provides the graph "g" to all its subclassess/traits */
abstract class KiwilandBase(val g: Graph)

/** This class mixes in the functionality of all the components */
class Kiwiland(override val g: Graph)
extends KiwilandBase(g)
  with RouteDist
  with KwlTripsCounterDynProg
  with DijkstraShortestPath {

  /*
   * The following methods are inherited from RouteDist:
    def routeDist(route: Route): Int
    def routeDist(s: String): Int
    def routeDistOrMsg(s: String)

   * The following methods are inherited from KwlTripsCounterDynProg:
    def countTrips(from: NodeId, to: NodeId, counter_condition: CounterCond): Long

   * The following is inherited from DijkstraShortestPath:
    def shortestPath(from: NodeId, to: NodeId): Long
  */

}

object Kiwiland {
  val defaultInput = "AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7"

  // factory method
  def apply(str: String): Kiwiland = new Kiwiland(createGraph(str split ", "))

  def main(args: Array[String]): Unit = {
    val msg = "Enter the graph edges separated by ',' (or just press Enter for default):\n"
    // get the edges or use default
    val edges = Option(readLine(msg)) filter{ ! _.isEmpty } getOrElse defaultInput
    val kwl = Kiwiland(edges)

    val results = List(
      kwl.routeDist("ABC"),
      kwl.routeDist("AD"),
      kwl.routeDist("ADC"),
      kwl.routeDist("AEBCD"),
      kwl.routeDistOrMsg("AED"),
      kwl.countTrips('C', 'C', MaxEdgeNumCond(3)),
      kwl.countTrips('A', 'C', ExactEdgeNumCond(4)),
      kwl.shortestPath('A', 'C'),
      kwl.shortestPath('B', 'B'),
      kwl.countTrips('C', 'C', MaxDistCond(30))
    )

    println(results mkString "\n")
  }
}
