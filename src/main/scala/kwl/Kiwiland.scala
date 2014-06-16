/**
 * The main file
 */
package kwl

import Graph.{NodeId, createGraph}
import kwl.TripsCounter.{MaxDistCond, MaxEdgeNumCond, ExactEdgeNumCond}

abstract class KiwilandBase(val g: Graph)

class Kiwiland(override val g: Graph) extends KiwilandBase(g)
  with RouteDist with KwlTripsCounterDynProg with DijkstraShortestPath {
  /**
   * get Distance of the shortest path
   */
  //def shortestPath(from: NodeId, to: NodeId): Long =
  //  DijkstraShortestPath.shortestPath(g, from, to)

  /*
   * The following methods are inherited from RouteDist:
    def routeDist(route: Route): Int
    def routeDist(s: String): Int
    def routeDistOrMsg(s: String)

   * The following methods are inherited from KwlTripsCounter:
    def countTrips(counter_type: Counter, from: NodeId, to: NodeId, max_dist: Int): Int
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
