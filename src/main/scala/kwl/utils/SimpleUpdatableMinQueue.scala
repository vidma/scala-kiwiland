package kwl.utils

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
class SimpleUpdatableMinQueue[K, V](implicit cmp: Ordering[K])
extends collection.mutable.HashSet[(K, V)] {
  type QItem = (K, V) // (key=priority, e.g. distance; value=entry, e.g. NodeID)

  def popMin(): QItem = {
    val minElm = this.minBy(get_key)
    this -= minElm
    minElm
  }

  def updateKey(new_weight: K, entry: V): Unit = {
    this.retain(_._2 != entry) // remove the element if exists
    this += (new_weight -> entry)
  }

  override def iterator: Iterator[QItem] = new Iterator[QItem] {
    def hasNext: Boolean = !this.isEmpty
    def next(): QItem = popMin()
  }

  def get_key(e: QItem): K = e._1
  def get_value(e: QItem): V = e._2
}

object SimpleUpdatableMinQueue {
  // TODO: do something with this? move to graph?
  type V = Char
  type K = Long
  type QItem = (K, V) // (priority, entry)
}
