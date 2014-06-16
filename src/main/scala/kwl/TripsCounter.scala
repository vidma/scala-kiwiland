package kwl

import kwl.Graph._
import kwl.TripsCounter._

import kwl.utils.Memo
import kwl.utils.Memo._


/**
 * If cycles are allowed, dynamic programming may be useful [1]
 *
 * "If we allow paths to reuse vertices then there is a dynamic programming solution to find the number of paths from s to t with n edges."
 *
 * Related Variations on the problem:
 * - on cyclic graph finding # of acyclic paths (i.e. simple paths) is P-complete [1-2,5]
 * - on acyclic graph - basically walking in topological order and counting paths [3, 4]
 *
 * Related resources:
 * [1] http://cs.stackexchange.com/questions/423/how-hard-is-counting-the-number-of-simple-paths-between-two-nodes-in-a-directed
 * [2] http://stackoverflow.com/questions/5569256/fast-algorithm-for-counting-the-number-of-acyclic-paths-on-a-directed-graph
 * [3] http://web.eecs.utk.edu/courses/fall2011/cs302/Notes/Topological/  [see very bottom]
 * [4] http://anorwell.com/index.php?id=51
 * [5] http://www.maths.uq.edu.au/~kroese/ps/robkro_rev.pdf
 */


/**
 * The following solution is based on an observation that
 * if cycles are allowed, the count of different sub-routes continuing at s
 * depend only on (s, dest, dist) independently of any steps taken earlier.
 *
 * Thus, the complexity shall be ~ O(|N| * |E| * |Dist|) and uses storage of O(N Dist).
 * This might be theoretically improved by organizing the computations in other way?
 *
 * TODO: get rid of recursion to support much larger graphs
 */
trait KwlTripsCounterDynProg extends KiwilandBase {

  def countTrips(src: NodeId, dest: NodeId, cond: CounterCond): Int = {
    /** recursive helper: nPaths(s, dist) => Int:
      * recursively calculate # of routes from "s" to "dest" with length exactly of "dist"
      * results are memoized in "Dynamic Programming" cache, i.e. Map[(s, dist)-> Int]
      */
    lazy val nPaths: (NodeId, Int) ==> Int = Memo {
      // reached destination?
      case (`dest`, 0) => 1
      // otherwise, count # of distinct paths to "dest" via adjacent vertices
      case (s, dist) =>
        // get either weighted or unweighted outgoing edges depending on search condition
        val outgoingEdges = g.outEdges(s).map { case Edge(_, to, d) => (to, cond.distToUse(d)) }
        // return the sum of counts over the admissible out-edges
        ( for { (to, d) <- outgoingEdges if d <= dist }
          yield nPaths(to, dist - d) ).sum
    }

    /** sum the counts over the provided distance range */
    def sumCountsOverDists(dists: Range) = dists.map(d => nPaths(src, d)).sum

    // --- entry point ---
    cond match {
      case ExactEdgeNumCond(max_dist) => nPaths(src, max_dist)
      case MaxEdgeNumCond(edge_num) => sumCountsOverDists(1 to edge_num)
      case MaxDistCond(max_dist) => sumCountsOverDists(1 until max_dist)
    }
  }
}


object TripsCounter {

  /* The classes below represent various filtering conditions */

  abstract class CounterCond {
    // do we use edge weights (d => d), or just count # of them ( d => 1 )?
    val distToUse: Int => Int
    // this stores either max # of edges or max distance
    val max_dist: Int

    // used in recursive (on DP) solution only
    def searchGuard(stops_used: Int, dist: Int, destReached: Boolean): Boolean
  }

  case class MaxEdgeNumCond(max_dist: Int) extends CounterCond {
    // when we limit the edge count, we just map distances into 1
    val distToUse = (d: Int) => 1

    def searchGuard(stops_used: Int, dist: Int, destReached: Boolean): Boolean =
      stops_used <= max_dist
  }

  case class ExactEdgeNumCond(max_dist: Int) extends CounterCond {
    // when we limit the edge count, we just map distances into 1
    val distToUse = (d: Int) => 1

    def searchGuard(stops_used: Int, dist: Int, destReached: Boolean): Boolean =
      if (destReached) { stops_used == max_dist }
      else { stops_used <= max_dist }
  }

  case class MaxDistCond(max_dist: Int) extends CounterCond {
    // here we limit of the edge weights/distances, so original edge dists are used
    override val distToUse = (d: Int) => d

    def searchGuard(stops_used: Int, dist: Int, destReached: Boolean): Boolean =
      dist < max_dist
  }

}


/**
 *  Different implementation of even simpler recursive solution.
 *
 * adding additional CounterCond classes allows to easily
 * change the behavior of this counter.
 * */
 trait KwlTripsCounterRecursive extends KiwilandBase {

  def countTrips(src: NodeId, dest: NodeId, cond: CounterCond): Int = {
    val searchGuard = cond.searchGuard _

    // inner recursive helper
    def getCount(current: NodeId, stops_used: Int, total_dist: Int): Int = {
      var count = 0
      if (searchGuard(stops_used, total_dist, false)) {
        // Loop over all adjacent edges
        for (Edge(_, edge_to, edge_dist) <- g.outEdges(current)) {
          val new_dist = total_dist + edge_dist
          // check if the final destination is reached in the required way
          if (dest == edge_to && searchGuard(stops_used + 1, new_dist, true)) {
            count += 1
          }
          // any longer paths?
          count += getCount(edge_to, stops_used + 1, new_dist)
        }
      }
      count
    }

    // call the inner recursive function
    getCount(src, 0, 0)
  }

}

