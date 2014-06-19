package kwl.tripscounter

import kwl.Graph._
import kwl.utils.Memo
import kwl.utils.Memo.==>
import kwl.{Edge, KiwilandBase}
import kwl.tripscounter.TripsCounter._

import scala.collection.mutable

/**
 * At very first sight, it might look that the only feasible solution could be the
 * brute-force enumerating all possible paths. Happily, if cycles are allowed
 * in the paths, dynamic programming comes to help, based on an observation that
 * the # of different sub-routes from s to dest (s -> t) depend only on (s, t, dist)
 * independently of any steps taken earlier. This guarantees the polynomial runtime
 * (at the expense of additional storage).
 *
 * Below is a simple recursive solution covering the Dynamic Programing idea via caching/memento.
 * Its complexity shall be ~ O(|N| * |E| * |Dist|) and uses storage of O(N*Dist). Might be not
 * miraculous for very large distances (esp. storage), but it's at least not exponensial.
 *
 * --- below are just unimportant details and speculations ---
 * This might be potentially improved (?) by organizing the computations in some other way (e.g.
 * divide-and-conquer alla Floyd-Warshall, consider routes between s and t via some intermediary vertex w:
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
trait TripsCounter extends KiwilandBase {

  def countTrips(src: NodeId, dest: NodeId, cond: CounterCond): Long = {
    // do we use edge weights (d => d), or just count # of them ( d => 1 )?
    val distToUse = cond match {
      case MaxDistCond(_) => (d: Int) => d
      case _ => (d: Int) => 1
    }
    // recursive helper: nPaths(s, dist) => Int
    // recursively calculates # of routes from "s" to "dest" with length exactly of "dist"
    // results are memoized in "Dynamic Programming" cache, i.e. Map[(s, dist)-> Int]
    // TODO: get rid of non-tail recursion to support much larger graphs
    lazy val nPaths: (NodeId, Long) ==> Long = Memo {
      case (`dest`, 0) => 1  // reached destination?
      case (s, dist) =>      // otherwise, count # of distinct paths to "dest" via adjacent vertices
        // get either weighted or unweighted outgoing edges depending on search condition
        val outgoingEdges = g.outEdges(s).map { case Edge(_, to, d) => (to, distToUse(d)) }
        // return the sum of counts over the admissible out-edges
        ( for { (to, d) <- outgoingEdges if d <= dist }
          yield nPaths(to, dist - d) ).sum
    }
    // helper to sum the counts over the provided distance range
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
    // this stores either the max # of edges or the max distance
    val max_dist: Int
  }

  case class MaxEdgeNumCond(max_dist: Int) extends CounterCond

  case class ExactEdgeNumCond(max_dist: Int) extends CounterCond

  case class MaxDistCond(max_dist: Int) extends CounterCond

}
