package kwl

import Graph.NodeId
import TripsCounter.CType.{MAX_DIST, EXACT_STOPS, CType}


/**
 * Counting the number of different paths in a graph without cycles
 * TODO: could be solved efficiently (e.g. by multiplying the number of paths in a [topological order?]),
 *
 * however if the cycles are allowed there doesn't seem to be a much
 * better solution than just an enumeration of all possibilities.
 */
class TripsCounter(g: Graph, from: NodeId, to: NodeId, val max_stops: Int, val max_dist: Int = 0) {
  // overriding these two methods allows to easily change the behavior of this counter
  protected def searchGuard(stops_used: Int, dist: Int): Boolean = stops_used < max_stops

  protected def destReachedGuard(stops_used: Int, dist: Int): Boolean = true

  protected def countTrips(current: NodeId, stops_used: Int, total_dist: Int): Int = {
    var trips = 0
    if (searchGuard(stops_used, total_dist)) {
      // Loop over all adjacent edges
      for (Edge(_, edge_to, edge_dist) <- g.edgesMap(current)) {
        val new_dist = total_dist + edge_dist
        // check if the final destination is reached in the required way
        if (to == edge_to && destReachedGuard(stops_used + 1, new_dist)) {
          trips += 1
        }
        // any longer paths?
        trips += countTrips(edge_to, stops_used + 1, new_dist)
      }
    }
    trips
  }

  def count(): Int = countTrips(from, 0, 0)
}

trait ExactDistCounter extends TripsCounter {
  override def searchGuard(stops_used: Int, dist: Int): Boolean = stops_used < max_stops

  override def destReachedGuard(stops_used: Int, dist: Int): Boolean = stops_used == max_stops
}

trait MaxDistCounter extends TripsCounter {
  override def destReachedGuard(stops_used: Int, dist: Int): Boolean = dist < max_dist

  override def searchGuard(stops_used: Int, dist: Int): Boolean = dist < max_dist
}

/**
 * Trips counter factory
 */
object TripsCounter {

  object CType extends Enumeration {
    type CType = Value
    val MAX_DIST, EXACT_STOPS, MAX_STOPS = Value
  }

  def apply(t: CType, g: Graph, from: NodeId, to: NodeId, max_stops: Int, max_dist: Int = 0): TripsCounter = t match {
    case MAX_DIST => new TripsCounter(g, from, to, max_stops, max_dist) with MaxDistCounter
    case EXACT_STOPS => new TripsCounter(g, from, to, max_stops, max_dist) with ExactDistCounter
    case _ => new TripsCounter(g, from, to, max_stops, max_dist)
  }
}
