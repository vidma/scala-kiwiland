import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalacheck.Gen.alphaUpperChar
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._

import kwl.SimpleUpdatableMinQueue

/**
 * The scalacheck generates "random" inputs and check that the properties defined below do hold
 */
object CheckUpdatableMinQueue extends Properties("UpdatableMinQueue") {
  type T = Char
  type QT = List[(T, Int)]
  val ORDERING = Ordering.by((x: (T, Int)) => x._2)

  def genSmallPosNum = choose[Int](1, 1000)

  def getSmallPosNum = genSmallPosNum.sample.getOrElse(1)

  val propFindMinSorted = forAll { (list: QT) =>
    // create the queue
    val q = new SimpleUpdatableMinQueue[T]()
    for ((entry, priority) <- list) {
      q.enqueue(entry, priority)
    }
    // check that multiple findMin result in a sorted list  
    compareQueues(q.findMinAll.toList, list.sorted(ORDERING))
  }

  val propUpdateWorks = forAll(genPriorityQ, listOf(genSmallPosNum)) { (l: QT, updates: List[Int]) =>
    val list = Array.empty ++ (Set.empty ++ l)
    if (list.length == 0) true
    else {
      // create the initial queue
      val q = new SimpleUpdatableMinQueue[T]()
      for ((entry, priority) <- list)
        q.enqueue(entry, priority)

      //update some priorities
      for (index <- updates map (_ % list.length)) {
        val update_with = getSmallPosNum
        val (id, _) = list(index)
        q.updatePriority(id, update_with)
        list.update(index, (id, update_with))
      }
      // make sure the updated lists still match
      compareQueues(q.findMinAll.toList, list.sorted(ORDERING).toList)

    }
  }


  // generator of an Arbitrary List of (Char, Int)
  // it must consist of unique letters
  def genPriorityQ: Gen[QT] = for (chars <- listOf(alphaUpperChar))
  yield chars.distinct map (x => (x, getSmallPosNum))

  implicit lazy val arbPriorityQ: Arbitrary[QT] = Arbitrary(genPriorityQ)

  // compare the weights only
  def compareQueues(q1: QT, q2: QT): Boolean = {
    def get_weight(x: (T, Int)) = x._2
    val equals = q1.map(get_weight) == q2.map(get_weight)
    if (!equals) {
      println("Not equal: " + q1 + " and " + q2)
    }
    equals
  }

}