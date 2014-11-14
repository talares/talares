/*
 * Copyright 2014 Dennis Vis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.talares.api.queries

/**
 * A Scala representation of an OData query URL.
 *
 * Example:
 * {{{
 *   import org.talares.api.queries._
 *   val q = Query / "Pages" % ("ItemId" -> 123, "PublicationId" -> 1) $ ("Title" ==| "A title") && ("Url" !=| "/path")
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class Query(name: String, args: Seq[(String, String)] = Seq())
                (parent: Option[Query] = None)
                (options: Seq[QueryOption] = Seq()) {

  /** Holds a formatted String containing all main arguments of the query (ID's most likely). **/
  lazy private val argsValue: String =
    if (args.isEmpty) ""
    else args.map(x => x._1 + '=' + x._2).mkString("(", ",", ")")

  /** Holds a formatted String containing all additional query options like filters. **/
  lazy private val optionsValue: String =
    if (options.isEmpty) ""
    else options.map(_.toString).mkString("?", "&", "")

  /** Holds the complete query in String form taking into account it's parent, children, arguments and options. **/
  lazy val value: String = {
    val trailingValue = '/' + name + argsValue + optionsValue
    parent match {
      case Some(parentQuery) => parentQuery.value + trailingValue
      case _ => trailingValue
    }
  }

  /**
   * Appends the given set of new arguments to the existing set of arguments.
   *
   * @param newArgs the new set of arguments to process
   * @return a new instance of [[Query]] holding the new arguments
   */
  def %(newArgs: (String, Any)*): Query =
    new Query(name, args ++ newArgs.map(arg => arg._1 -> arg._2.toString))(parent)(options)

  /**
   * Adds the given [[Query]] as a child of this query.
   *
   * @param newChild the child this [[Query]] will be a parent of
   * @return a new instance of [[Query]] holding a reference to the child [[Query]]
   */
  def /(newChild: Query): Query = new Query(newChild.name, newChild.args)(Some(this))(options)

  /**
   * Appends the given set of new options to the existing set of options.
   *
   * @param newOptions the new set of options to process
   * @return a new instance of [[Query]] holding the new options
   */
  def $(newOptions: QueryOption*): Query = new Query(name, args)(parent)(options ++ newOptions)

  /**
   * @return the [[Query#value]] field.
   */
  override def toString: String = value
}

object Query {

  private def emptyQueryWithName(name: String): Query = Query(name, Seq())(None)(Seq())

  def /(name: String): Query = emptyQueryWithName(name)

  implicit def createQuery(name: String): Query = emptyQueryWithName(name)

}