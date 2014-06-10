import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalacheck.Gen.alphaUpperChar
import org.scalacheck.Arbitrary

import kwl.SimpleUpdatableMinQueue
import kwl.SimpleUpdatableMinQueue.{QueueEntry, T}

/**
 * The scalacheck generates "random" inputs and check that the properties defined below do hold
 */
object CheckUpdatableMinQueue extends Properties("UpdatableMinQueue") {
  type QueueT = List[QueueEntry]
  val ORDERING = Ordering.by( (x: QueueEntry) => x._1 )

  def genSmallPosNum: Gen[Int] = choose[Int](1, 1000)

  /** obtain a sorted list of queue items by repeatedly calling popMin */
  def popminSorted(q: SimpleUpdatableMinQueue[T]): QueueT = {
    val sorted = collection.mutable.MutableList[QueueEntry]()
    while (!q.isEmpty)
      sorted += q.popMin
    sorted.toList
  }

  /**
   * given arbitrary items to be added to the queue,
   * ensure that popMin returns them in sorted order
   * **/
  val propFindMinSorted = forAll { (arb_list: QueueT) =>
    // create the queue
    val q = new SimpleUpdatableMinQueue[T]()
    arb_list foreach (q += _)
    // check that multiple popMin result in a sorted list
    compareQueues(popminSorted(q), arb_list.sorted(ORDERING))
  }


  /**
   * given arbitrary queue items and arbitrary list of updates,
   * check that multiple popMin returns them in sorted order
   **/
  val propUpdateWorks = forAll(genUpdatesQ) {
    (updates: (Array[QueueEntry], List[(Int, Int)])) =>
      val (arb_queue, arb_updates) = updates

      // create the initial queue
      val q = new SimpleUpdatableMinQueue[T]()
      arb_queue foreach (q += _)

      // update some priorities
      for ((index, new_priority) <- arb_updates) {
        val id = arb_queue(index)._2
        q.updatePriority(new_priority, id)
        arb_queue.update(index, (new_priority, id))
      }

      // check that the updated lists still match
      compareQueues(popminSorted(q), arb_queue.sorted(ORDERING).toList)
  }

  def compareQueues(q1: QueueT, q2: QueueT): Boolean = {
    // compare the weights only
    def get_weight(x: QueueEntry) = x._1

    val equals = q1.map(get_weight) == q2.map(get_weight)
    if (!equals) {
      println("Not equal: " + q1 + " and " + q2)
    }
    equals
  }

  /**
   * generator of arbitrary params to propUpdateWorks
   * (initial Queue; indexes to be updated and the new priorities)
   */
  def genUpdatesQ: Gen[(Array[QueueEntry], List[(Int, Int)])] = for {
    queue: QueueT <- genPriorityQ
    indexes: List[Int] <- listOf(choose[Int](0, queue.length - 1)) // index in the queue
    new_weights <- listOfN(indexes.length, genSmallPosNum)
  } yield (queue.toArray, indexes zip new_weights)

  /** generator of an Arbitrary Priority Queue i.e. List[(Char, Int)] */
  def genPriorityQ: Gen[QueueT] = for {
    chars <- listOf(alphaUpperChar) if chars.length > 0
    priorities <- listOfN(chars.length, genSmallPosNum)
  } yield priorities zip chars.distinct

  implicit lazy val arbPriorityQ: Arbitrary[QueueT] = Arbitrary(genPriorityQ)
}