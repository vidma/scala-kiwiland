package kwl.tripscounter

import kwl.Graph._
import kwl.{Edge, KiwilandBase}
import kwl.tripscounter.TripsCounterRecursive.getGuards
import kwl.tripscounter.TripsCounter._

/**
 *  A Different implementation of even simpler recursive solution.
 * */
trait TripsCounterRecursive extends KiwilandBase {

  def countTrips(src: NodeId, dest: NodeId, cond: CounterCond): Long = {
    // customize the behaviour according to search conditions
    val (searchGuard, reachedGuard) = getGuards(cond)
    // inner recursive helper
    def getCount(current: NodeId, stops_used: Int, total_dist: Int): Long = {
      var count: Long = 0
      // Loop over all adjacent edges
      for (Edge(_, edge_to, edge_dist) <- g.outEdges(current)) {
        val new_dist = total_dist + edge_dist
        // check if the final destination is reached in the required way
        if (dest == edge_to && reachedGuard(stops_used + 1, new_dist)) {
          count += 1
        }
        // any longer paths?
        if (searchGuard(stops_used+1, new_dist)) {
          count += getCount(edge_to, stops_used + 1, new_dist)
        }
      }

      count
    }
    // call the inner recursive function
    getCount(src, 0, 0)
  }

}

object TripsCounterRecursive {
  // instead of polluting the CounterCond classes, store the search conditions here
  // (this is just a backup implementation)
  def getGuards(cond: CounterCond): ((Int, Int) => Boolean, (Int, Int) => Boolean) = {
    // default guards
    var searchGuard, reachedGuard = (stops: Int, dist: Int) => stops <= cond.max_dist
    cond match {
      case ExactEdgeNumCond(max_dist) =>
        reachedGuard = (stops: Int, dist: Int) => stops == max_dist
      case MaxDistCond(max_dist) =>
        searchGuard = (stops: Int, dist: Int) => dist < max_dist
        reachedGuard = searchGuard
      case MaxEdgeNumCond(max_dist) => // default
    }
    (searchGuard, reachedGuard)
  }
}