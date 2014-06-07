package kwl

import kwl.Graph._
import scala.Some


trait RouteDist extends Kwl {
  /**
   * get the distance of a specified route or throw an exception
   */
  def routeDist(route: Route): Int = route match {
    case from :: to :: rest =>
      val dist = findEdge(from, to) match {
        case Some(edgeOut: Edge) => edgeOut.dist
        case _ => throw new Exception("NO SUCH ROUTE")
      }
      routeDist(to :: rest) + dist
    case _ => 0
  }

  protected def findEdge(from: NodeId, to: NodeId): Option[Edge] = for {
    from_edges <- g.edgesMap.get(from)
    edge <- from_edges.find(_.to == to)
  } yield edge

  def routeDist(s: String): Int = routeDist(s.toList)

  /**
   * given a route as a string, print its distance or an error message
   */
  def routeDistPrint(s: String): Unit = {
    try {
      println(routeDist(s.toList))
    }
    catch {
      case e: Exception => println(e.getMessage)
    }
  }

}