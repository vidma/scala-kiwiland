package kwl.tripscounter

import kwl.Graph._
import kwl.{Edge, KiwilandBase}
import kwl.tripscounter.TripsCounterNaive.getGuards
import kwl.tripscounter.TripsCounter._

/**
 *  A Different implementation of even simpler recursive solution
 * */
trait TripsCounterNaive extends KiwilandBase {

  def countTrips(src: NodeId, dest: NodeId, cond: CounterCond): Long = {
    // customize the behaviour according to search conditions
    val (searchGuard, reachedGuard) = getGuards(cond)
    // inner recursive helper
    def getCount(current: NodeId, stops_used: Int, total_dist: Int): Long = {
      var count = 0L
      // Loop over all adjacent edges
      for (Edge(_, edge_to, edge_len) <- g.outEdges(current)) {
        val new_dist = total_dist + edge_len
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

object TripsCounterNaive {
  /** instead of polluting the main CounterCond classes (see TripsCounter.scala),
    * better obtain the search conditions from here (this is just a backup implementation anyway)
    */
  type GuardFunc = (Int, Int) => Boolean
  def getGuards(cond: CounterCond): (GuardFunc, GuardFunc) = {
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