package kwl

import scala.collection.TraversableOnce


/**
 * A basic Priority Queue with Updatable Priorities.
 *
 * this is a min-queue, i.e. de-queueing returns the min-weight element
 *
 * Note: a more efficient queue implementation with better decreaseKey is possible,
 * but surely not needed for this task (at least until nodes are named as letters!)
 *
 * Note: a faster but more complex Priority Queue implementing DecreaseWeight would require:
 * - a reverse index of where physically each item is stored in the head (to find it quickly) O(1)
 * * e.g. Array for Heap for Char or other types
 * - once found, modify it and shiftUp/down O(log n)
 */
class SimpleUpdatableMinQueue[T] extends collection.mutable.HashSet[(Int, T)] {
  type QueueEntry = (Int, T) // (priority, entry)

  override def iterator: Iterator[QueueEntry] = new Iterator[QueueEntry] {
    def hasNext: Boolean = !this.isEmpty

    def next(): QueueEntry = popMin()
  }

  def popMin(): QueueEntry = {
    val minElm = this.minBy(_._1)
    this -= minElm
    minElm
  }

  def updatePriority(new_weight: Int, entry: T): Unit = {
    this.retain(_._2 != entry) // remove the element if exists, TODO: this lookup could be faster
    this += ((new_weight, entry))
  }
}

object SimpleUpdatableMinQueue {
  type T = Char
  type QueueEntry = (Int, T) // (priority, entry)
}
