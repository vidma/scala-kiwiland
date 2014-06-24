package kwl


import scala.annotation.tailrec
import scala.util.{Try, Success, Failure}
import kwl.Graph.NodeId


trait RouteDist extends KiwilandBase {
  val MsgNoRouteFound = "NO SUCH ROUTE"

  protected def reportNoRoute = throw new Exception(MsgNoRouteFound)

  /**
   * get the distance of a specified route or throw an exception
   */
  // TODO: It doesn't seem scala compiler could figure out tailrec without using an accumulator?
  @tailrec final def routeDist(route: List[NodeId], acc: Int = 0): Int = route match {
    // fetch the first two nodes on the route if any, and continue recursively
    case from :: to :: rest =>
      val d = g.adjMatrixDist.getOrElse((from, to), reportNoRoute)
      routeDist(to :: rest, acc + d)
    case _ => acc
  }

  def routeDist(s: String): Int = routeDist(s.toList)

  def routeDistOrMsg(s: String): String = Try(routeDist(s)) match {
    case Success(dist) => dist.toString
    case Failure(e) => e.getMessage
  }

}
