package kwl.shortestpath

import kwl.KiwilandBase
import kwl.Graph._

trait ShortestPathBase extends KiwilandBase {
  /**
   * @return length of the shortest path or -1 if it don't exist
   */
  def shortestPath(src: NodeId, to: NodeId): Long

  val NO_PATH_EXISTS = -1
}
