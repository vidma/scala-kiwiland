package kwl

import kwl.Graph._
import kwl.TripsCounter._

import kwl.utils.Memo
import kwl.utils.Memo.==>


/**
 * At very first sight, it might look that the only feasible solution could be the
 * brute-force enumerating all possible paths. Happily, if cycles are allowed
 * in the paths, dynamic programming comes to help, based on an observation that
 * the # of different sub-routes from s to dest (s -> t) depend only on (s, t, dist)
 * independently of any steps taken earlier. This guarantees the polynomial runtime
 * (at the expense of additional storage).
 *
 * Below is a simple recursive solution covering the Dynamic Programing idea via caching/memento.
 * Its complexity shall be ~ O(|N| * |E| * |Dist|) and uses storage of O(N Dist).
 *
 * TODO: get rid of recursion to support much larger graphs
 *
 * --- Blah Blah talk below ---
 * This might be potentially improved (?) by organizing the computations in some other way (e.g.
 * divide-and-conquer alla FloydWarshall, consider routes between s and t via some intermediary vertex w:
 * c(s->t) = c(s->w) * c(w->t), however with cycles in place counting the # of routes seems much harder...).
 *
 * Note: there are two related problems: first, on acyclic graphs the solution is really simple:
 * count paths while walking in a topological order [3, 4]. On the other hand, finding # of simple
 * paths (i.e. acyclic) in a cyclic graph is P-complete [1-2,5], where, approximations are sometimes
 * preferred.
 *
 * Resources:
 * [1] http://cs.stackexchange.com/questions/423/how-hard-is-counting-the-number-of-simple-paths-between-two-nodes-in-a-directed
 * [2] http://stackoverflow.com/questions/5569256/fast-algorithm-for-counting-the-number-of-acyclic-paths-on-a-directed-graph
 * [3] http://web.eecs.utk.edu/courses/fall2011/cs302/Notes/Topological/  [see very bottom]
 * [4] http://anorwell.com/index.php?id=51
 * [5] http://www.maths.uq.edu.au/~kroese/ps/robkro_rev.pdf*
 */
trait KwlTripsCounterDynProg extends KiwilandBase {

  def countTrips(src: NodeId, dest: NodeId, cond: CounterCond): Long = {
    /** recursive helper: nPaths(s, dist) => Int:
      * recursively calculate # of routes from "s" to "dest" with length exactly of "dist"
      * results are memoized in "Dynamic Programming" cache, i.e. Map[(s, dist)-> Int]
      */
    lazy val nPaths: (NodeId, Long) ==> Long = Memo {
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

    // Note: used only in KwlTripsCounterRecursive (not in KwlTripsCounterDynProg)
    val searchGuard, reachedGuard = (stops: Int, dist: Int) => stops <= max_dist
  }

  case class MaxEdgeNumCond(max_dist: Int) extends CounterCond {
    // when we limit the edge count, we just map distances into 1
    val distToUse = (d: Int) => 1
    // inherit: searchGuard, reachedGuard => stops <= max_dist
  }

  case class ExactEdgeNumCond(max_dist: Int) extends CounterCond {
    // when we limit the edge count, we just map distances into 1
    val distToUse = (d: Int) => 1

    // inherit searchGuard = stops <= max_dist
    override val reachedGuard = (stops: Int, dist: Int) => stops == max_dist
  }

  case class MaxDistCond(max_dist: Int) extends CounterCond {
    // here we limit of the edge weights/distances, so original edge dists are used
    override val distToUse = (d: Int) => d

    override val searchGuard, reachedGuard = (stops: Int, dist: Int) => dist < max_dist
  }

}


/**
 *  A Different implementation of even simpler recursive solution.
 *
 * adding additional CounterCond classes allows to easily
 * change the behavior of this counter.
 * */
trait KwlTripsCounterRecursive extends KiwilandBase {

  def countTrips(src: NodeId, dest: NodeId, cond: CounterCond): Long = {
    // inner recursive helper
    def getCount(current: NodeId, stops_used: Int, total_dist: Int): Long = {
      var count: Long = 0
        // Loop over all adjacent edges
        for (Edge(_, edge_to, edge_dist) <- g.outEdges(current)) {
          val new_dist = total_dist + edge_dist
          // check if the final destination is reached in the required way
          if (dest == edge_to && cond.reachedGuard(stops_used + 1, new_dist)) {
            count += 1
          }
          // any longer paths?
          if (cond.searchGuard(stops_used+1, new_dist)) {
            count += getCount(edge_to, stops_used + 1, new_dist)
          }
        }

      count
    }
    // call the inner recursive function
    getCount(src, 0, 0)
  }

}

