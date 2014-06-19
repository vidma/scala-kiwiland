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
class SimpleUpdatableMinQueue[K, V](implicit cmp: Ordering[K]) {
  type QItem = (K, V) // (key=priority, e.g. distance; value=entry, e.g. NodeID)
  protected val queue = collection.mutable.HashSet.empty[QItem]

  def popMin(): QItem = {
    val minElm = queue.minBy(get_key)
    queue -= minElm
    minElm
  }

  def updateKey(new_weight: K, entry: V): Unit = {
    queue.retain(_._2 != entry) // remove the element if exists
    queue += (new_weight -> entry)
  }

  def += (elm: QItem): this.type = { queue += elm; this }
  def isEmpty: Boolean = queue.isEmpty

  protected def get_key(e: QItem): K = e._1
  protected def get_value(e: QItem): V = e._2
}
