/**
 * The main file
 */
package kwl

import Graph.{NodeId, createGraph}
import kwl.TripsCounter.CounterTypeEnum.{MAX_DIST, MAX_STOPS, EXACT_STOPS}

abstract class KwlBase(val g: Graph)

class Kiwiland(override val g: Graph) extends KwlBase(g) with RouteDist with KwlTripsCounter {
  /**
   * get Distance of the shortest path
   */
  def shortestPath(from: NodeId, to: NodeId): Int =
    DijkstraShortestPath.shortestPath(g, from, to)

  /*
   * The following methods are inherited from RouteDist:
    def routeDist(route: Route): Int
    def routeDist(s: String): Int
    def routeDistOrMsg(s: String)

   * The following methods are inherited from KwlTripsCounter:
    def countTrips(counter_type: CType, from: NodeId, to: NodeId, max_stops: Int, max_dist: Int = 0): Int
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
      kwl.countTrips(MAX_STOPS, 'C', 'C', 3),
      kwl.countTrips(EXACT_STOPS, 'A', 'C', 4),
      kwl.shortestPath('A', 'C'),
      kwl.shortestPath('B', 'B'),
      kwl.countTrips(MAX_DIST, 'C', 'C', 0, 30)
    )

    println(results mkString "\n")
  }
}
