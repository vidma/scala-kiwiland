package kwl.utils

/** *
 * Generic way to create memoized functions (even recursive and multiple-arg ones)
 *
 * @param f the function to memoize
 * @tparam I input to f
 * @tparam K the keys we should use in cache instead of I
 * @tparam O output of f
 *
 * Based on [1], which is much more clean than other more straight-forward approaches, e.g. [2].
 * [1] https://github.com/pathikrit/scalgos/blob/master/src/main/scala/com/github/pathikrit/scalgos/Memo.scala
 * [2] http://stackoverflow.com/questions/16257378/is-there-a-generic-way-to-memoize-in-scala
 */
case class Memo[I <% K, K, O](f: I => O) extends (I => O) {
  import collection.mutable.{Map => Dict}
  val cache = Dict.empty[K, O]
  override def apply(x: I) = cache getOrElseUpdate (x, f(x))
}

object Memo {
  /**
   * Type of a simple memoized function e.g. when I = K
   */
  type ==>[I, O] = Memo[I, I, O]
}
