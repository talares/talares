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
package org.talares.api.actors

import akka.actor.Props
import akka.event.LoggingReceive
import org.talares.api.Talares
import org.talares.api.actors.messages.ExecutorMessages
import org.talares.api.actors.messages.FetcherMessages._
import org.talares.api.datatypes.JsonReadable
import org.talares.api.exceptions.UnexpectedResultException
import org.talares.api.queries._
import play.api.libs.json.{JsArray, JsObject}

import scala.reflect.ClassTag

/**
 * Holds all functionality to compose an URL that designates where Json representations of 'T' might be found.
 *
 * Passes these locations to instances of [[Executor]] to receive the Json representations.
 * Is able to parse the Json into an instance of 'T' and send this to it's parent.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
class Fetcher[T](val app: Talares)(implicit jsonReadable: JsonReadable[T], classTag: ClassTag[T]) extends BaseActor {

  import context.parent

  type ID = (String, Any)
  type SearchParam = (String, Any)

  implicit val reads = jsonReadable.reads

  /**
   * Endpoint from which the Json representations of 'T' can be found within the webservice.
   *
   * The plural of the runtime class name of 'T' is used to find the endpoint. For instance a Fetcher[Page] will have
   * endpoint Pages.
   */
  val endpoint = {
    val name = classTag.runtimeClass.getSimpleName
    if (name.endsWith("y")) name.substring(0, name.length - 1) + "ies"
    else name.substring(0, name.length) + "s"
  }

  /** [[Executor]] for use within this [[Fetcher]]. **/
  lazy val executor = context.actorOf(Props(new Executor[T](app)), "executor")

  /**
   * Creates a [[org.talares.api.queries.Query]] consisting of the endpoint for type 'T' and the ID's given.
   *
   * @param IDs the ID's to include in the query
   * @return an instance of [[org.talares.api.queries.Query]]
   */
  def createIDQuery(IDs: Seq[ID]): Query = {
    val formattedIds: Seq[(String, String)] = IDs.foldLeft(Seq[(String, String)]()) {
      case (acc, id) => acc :+(id._1, id._2.toString)
    }
    Query / endpoint % (formattedIds: _*)
  }

  /**
   * Creates a [[org.talares.api.queries.Query]] consisting of the endpoint for type 'T' as well as a specific filter
   * composed of the given search parameters.
   *
   * @param searchParams a arbitrary number of key/value pairs to be 'and'ed into a filter
   * @return an instance of [[org.talares.api.queries.]]
   */
  def createSearchQuery(searchParams: Seq[SearchParam]): Query = {
    val formattedParams = searchParams.foldLeft(Seq[QueryOption]()) {
      case (acc, param) => acc :+ Filter(Operation(param._1, Operator.Eq, param._2))
    }
    Query / endpoint $ (formattedParams: _*)
  }

  /**
   * Composes a complete URL consisting of a webservice URL and a [[org.talares.api.queries.Query]]'s value.
   *
   * @param webserviceLocation the URL on which the webservice can be reached
   * @param query the query to have the webservice execute
   * @return a complete URL in String form
   */
  def createUrl(webserviceLocation: String, query: Query): String = webserviceLocation.toString + query.value

  /**
   * Creates a [[org.talares.api.actors.messages.ExecutorMessages.Execute]] with a complete URL in String form from the 
   * given completeURL.
   *
   * @param url a complete URL
   * @return an [[org.talares.api.actors.messages.ExecutorMessages.Execute]]
   */
  def createURLTask(task: Task[T], url: String): ExecutorMessages.Execute[T] = ExecutorMessages.Execute[T](task, url)

  /**
   * Creates a [[org.talares.api.actors.messages.ExecutorMessages]] with a complete URL in String form by using the
   * createUrl() method to compose said URL from a webservice location and a query.
   *
   * @param task the active task
   * @param webserviceLocation the URL on which the webservice can be reached
   * @param query the query to have the webservice execute
   * @return an [[org.talares.api.actors.messages.ExecutorMessages.Execute]]
   */
  def createQueryTask(task: Task[T], webserviceLocation: String, query: Query): ExecutorMessages.Execute[T] = {
    val url = createUrl(webserviceLocation, query)
    createURLTask(task, url)
  }

  /**
   * Creates a [[org.talares.api.actors.messages.ExecutorMessages.Execute]] with a complete URL in String form by using 
   * the createUrl() method to compose said URL from a webservice location and Query composed of the given ID's.
   *
   * @param task the active task
   * @param webserviceLocation the URL on which the webservice can be reached
   * @param IDs the ID's to include in the query
   * @return an [[org.talares.api.actors.messages.ExecutorMessages.Execute]]
   */
  def createIDTask(task: Task[T], webserviceLocation: String, IDs: ID*): ExecutorMessages.Execute[T] = {
    val query = createIDQuery(IDs)
    createQueryTask(task, webserviceLocation, query)
  }

  /**
   * Creates a [[org.talares.api.actors.messages.ExecutorMessages.Execute]] with a complete URL in String form by using 
   * the createUrl() method to compose said URL from a webservice location and Query consisting of a filter composed of 
   * the given search parameters.
   *
   * @param task the active task
   * @param webserviceLocation the URL on which the webservice can be reached
   * @param searchParams the search parameters with which a filter should be constructed
   * @return an [[org.talares.api.actors.messages.ExecutorMessages.Execute]]
   */
  def createSearchTask(task: Task[T],
                       webserviceLocation: String,
                       searchParams: SearchParam*): ExecutorMessages.Execute[T] = {
    val query = createSearchQuery(searchParams)
    createQueryTask(task, webserviceLocation, query)
  }

  /**
   * Handles messages of the type [[org.talares.api.actors.messages.FetcherMessages.Task]].
   *
   * Depending on the type of task a function is used to turn this task into a complete URL where Json representations 
   * of 'T' might be found. This URL is then wrapped in a [[org.talares.api.actors.messages.ExecutorMessages.Execute]]
   * message and passed to an [[Executor]] for execution.
   *
   * @param task the task to handle
   * @see [[Executor]]
   */
  def handleTask(task: Task[T]): Unit = {
    val message = task match {
      case FetchByID(client, webserviceLocation, ids@_*) =>
        createIDTask(task, webserviceLocation, ids: _*)
      case FetchBySearch(client, webserviceLocation, searchParams) =>
        createSearchTask(task, webserviceLocation, searchParams)
      case FetchByURI(client, uri) =>
        createURLTask(task, uri)
      case FetchByQuery(client, webserviceLocation, query) =>
        createQueryTask(task, webserviceLocation, query)
    }
    executor ! message
  }

  /**
   * Handles messages of the type [[org.talares.api.actors.messages.ExecutorMessages.Result]].
   *
   * This function handles four different situations, all of which result in a
   * [[org.talares.api.actors.messages.FetcherMessages.Result]] sent to the parent.
   *
   * 1. The result contains a JsArray; it is parsed as a Seq of 'T'
   * 2. The result contains a JsObject; it is parsed as a 'T'
   * 3. The result contains an unknown element; a UnexpectedResultException is propagated
   * 4. The result contains an Throwable; the Throwable is propagated
   *
   * @param result the [[org.talares.api.actors.messages.ExecutorMessages.Result]] to handle
   * @see [[Executor]]
   * @see play.api.libs.json.JsValue
   * @see play.api.libs.json.JsArray
   * @see play.api.libs.json.JsObject
   */
  def handleExecutorResult(result: ExecutorMessages.Result[T]): Unit = {
    val message = result match {
      case ExecutorMessages.Success(fetchTask, json: JsArray) => MultiResult(fetchTask, json.as[Seq[T]])
      case ExecutorMessages.Success(fetcherTask, json: JsObject) => SingleResult(fetcherTask, json.as[T])
      case ExecutorMessages.Success(fetcherTask, unknown) =>
        val exception = new UnexpectedResultException(endpoint, Seq(classOf[JsObject], classOf[JsArray]), unknown)
        Failure(fetcherTask, exception)
      case ExecutorMessages.Failure(fetcherTask, throwable) => Failure(fetcherTask, throwable)
    }
    parent ! message
  }

  def receive: Receive = LoggingReceive {
    case task: Task[T] => handleTask(task)
    case result: ExecutorMessages.Result[T] => handleExecutorResult(result)
  }
}