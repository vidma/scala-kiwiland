package kwl

import scala.util.{Try, Success, Failure}
import kwl.Graph.NodeId


trait RouteDist extends KiwilandBase {
  val MsgNoRouteFound = "NO SUCH ROUTE"

  protected def reportNoRoute = throw new Exception(MsgNoRouteFound)

  /**
   * get the distance of a specified route or throw an exception
   */
  def routeDist(route: List[NodeId]): Int = route match {
    // fetch the first two nodes on the route if any, and continue recursively
    case from :: to :: rest =>
      g.adjMatrixDist.getOrElse((from, to), reportNoRoute) + routeDist(to :: rest)
    case _ => 0
  }

  def routeDist(s: String): Int = routeDist(s.toList)

  def routeDistOrMsg(s: String): String = Try(routeDist(s)) match {
    case Success(dist) => dist.toString
    case Failure(e) => e.getMessage
  }

}
