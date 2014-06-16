import org.scalacheck.Gen._
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

import kwl._
import kwl.TripsCounter._
import GraphGenerator.arbGraph


/**
 * we generate "random" graphs which are fed into different shortest path algorithms for comparing their results
 *
 * Floyd-Warshall is very simple, and shall validate the Dijkstra implementation
 */
object CheckTripsCounter extends Properties("TripsCounter") {
  type T = Char


  val propRecursionMatchesDynProg = forAll { (g: Graph) =>
    val kwl_dp = new KiwilandBase(g) with KwlTripsCounterDynProg
    val kwl_rec = new KiwilandBase(g) with KwlTripsCounterRecursive
    var ok = true
    for (src <- g.nodesList;
         dst <- g.nodesList;
         dist = 10 ) {
      if (!(kwl_dp.countTrips(src, dst, ExactEdgeNumCond(dist)) == kwl_rec.countTrips(src, dst, ExactEdgeNumCond(dist)) &&
            kwl_dp.countTrips(src, dst, MaxDistCond(dist)) == kwl_rec.countTrips(src, dst, MaxDistCond(dist)) &&
            kwl_dp.countTrips(src, dst, MaxEdgeNumCond(dist)) == kwl_rec.countTrips(src, dst, MaxEdgeNumCond(dist))))
        ok = false
    }
    ok
  }
}