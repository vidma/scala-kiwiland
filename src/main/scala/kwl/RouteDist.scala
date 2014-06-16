package kwl

import scala.util.{Try, Success, Failure}

import kwl.Graph.{Route, NodeId}


trait RouteDist extends KiwilandBase {
  /**
   * get the distance of a specified route or throw an exception
   */
  val MsgNoRouteFound = "NO SUCH ROUTE"

  protected def reportNoRoute = throw new Exception(MsgNoRouteFound)

  def routeDist(route: Route): Int = route match {
    // fetch the first two nodes on the route if any, and continue recursively
    case from :: to :: rest =>
      edgeDist(from, to).getOrElse(reportNoRoute) + routeDist(to :: rest)
    case _ => 0
  }

  protected def edgeDist(from: NodeId, to: NodeId): Option[Int] = for {
    from_edges <- g.outEdges.get(from)
    edge <- from_edges.find(_.to == to)
  } yield edge.dist

  def routeDist(s: String): Int = routeDist(s.toList)

  def routeDistOrMsg(s: String): String = Try(routeDist(s)) match {
    case Success(dist) => dist.toString
    case Failure(e) => e.getMessage
  }

}
