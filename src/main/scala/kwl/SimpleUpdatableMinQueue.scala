package kwl

import scala.collection.mutable


/**
 * A basic Priority Queue with Updatable Priorities.
 *
 * this is a min-queue, i.e. dequueing returns the min-weight element
 *
 * Note: a more efficient queue implementation with better decreaseKey is possible,
 * but surely not needed for this task (at least until nodes are named as letters!)
 */
abstract class AbstractUpdatableMiniQueue[T] {

  case class QueueEntry(entry: T, weight: Int) {
    def toTuple: (T, Int) = (entry, weight)
  }

  def isEmpty: Boolean

  def enqueue(entry: T, priority: Int): Unit

  def findMin: (T, Int)

  def findMinAll: Seq[(T, Int)]

  def updatePriority(entry: T, new_weight: Int): Unit
}

class SimpleUpdatableMinQueue[T] extends AbstractUpdatableMiniQueue[T] {
  var q = mutable.PriorityQueue[QueueEntry]()(Ordering.by(-_.weight))

  def isEmpty: Boolean = q.isEmpty

  def enqueue(entry: T, priority: Int): Unit = q.enqueue(QueueEntry(entry, priority))

  def findMin: (T, Int) = q.dequeue().toTuple

  def findMinAll: Seq[(T, Int)] = q.dequeueAll map (_.toTuple)

  def updatePriority(entry: T, new_weight: Int): Unit = {
    // TODO: this is rather slow!
    // remove the element if exists
    if (q.exists(x => x.entry == entry)) {
      q = q.filter(_.entry != entry)
    }
    // add to queue with the new_weight
    q.enqueue(QueueEntry(entry, new_weight))
  }
}

/*
 Note: a faster but more complex Priority Queue implementing DecreaseWeight would require:
  - a reverse index of where physically each item is stored in the head (to find it quickly) O(1)
      * e.g. Array for Heap for Char or other types
  - once found, modify it and shiftUp/down O(log n)
*/
