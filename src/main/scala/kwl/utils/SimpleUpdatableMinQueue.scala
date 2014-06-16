package kwl.utils

import SimpleUpdatableMinQueue._

/**
 * A basic Priority Queue with Updatable Priorities.
 *
 * this is a min-queue, i.e. de-queueing returns the min-weight element
 *
 * Note: a more efficient queue implementation with better decreaseKey is possible,
 * but surely not needed for this task (at least until nodes are named as letters!)
 *
 * Note: a faster but more complex Priority Queue implementing DecreaseWeight would require:
 * - a reverse index of where physically each entry is stored in the heap (to find it quickly)
 *     * e.g. Array for low number of elements, or a HashTable for more fancy types  O(1)
 * - once found the required heap entry, modify it and shiftUp/down O(log n)
 */
class SimpleUpdatableMinQueue[T] extends collection.mutable.HashSet[(WeightT, T)] {
  type QueueEntry = (WeightT, T) // (priority, entry)

  override def iterator: Iterator[QueueEntry] = new Iterator[QueueEntry] {
    def hasNext: Boolean = !this.isEmpty

    def next(): QueueEntry = popMin()
  }

  def popMin(): QueueEntry = {
    val minElm = this.minBy(_._1)
    this -= minElm
    minElm
  }

  def updatePriority(new_weight: WeightT, entry: T): Unit = {
    this.retain(_._2 != entry) // remove the element if exists, TODO: this lookup could be faster
    this += ((new_weight, entry))
  }
}

object SimpleUpdatableMinQueue {
  type T = Char
  type WeightT = Int
  type QueueEntry = (WeightT, T) // (priority, entry)
}
