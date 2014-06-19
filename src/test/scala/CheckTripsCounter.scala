import kwl.tripscounter.{TripsCounterNaive, TripsCounter}
import org.scalacheck.Gen._
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

import kwl._
import TripsCounter._
import GraphGenerator.arbGraph


/**
 * we generate "random" graphs which are fed into different shortest
 * path algorithms for comparing their results
 *
 * Floyd-Warshall is very simple, and shall validate the Dijkstra implementation
 */
object CheckTripsCounter extends Properties("TripsCounter") {

  property("RecursionMatchesDynProg") = forAll { (g: Graph) =>
    val kwl_dp = new KiwilandBase(g) with TripsCounter
    val kwl_rec = new KiwilandBase(g) with TripsCounterNaive
    var ok = true
    for (src <- g.nodesList;
         dst <- g.nodesList;
         dist <- choose[Int](1, 30).sample;
         cond <- List(ExactEdgeNumCond(dist), MaxDistCond(dist), MaxEdgeNumCond(dist))) {
      //println(src, dst, cond)
      // compare the two implementations
      if (kwl_dp.countTrips(src, dst, cond) != kwl_rec.countTrips(src, dst, cond))
        ok = false
    }

    ok
  }
}