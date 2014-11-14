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

import akka.actor.{ActorRef, Props, Status}
import akka.event.LoggingReceive
import org.talares.api.Talares
import org.talares.api.actors.messages.FetcherMessages
import org.talares.api.actors.messages.MediatorMessages._
import org.talares.api.cache.Cache
import org.talares.api.cache.actors.messages.CachingActorMessages
import org.talares.api.datatypes.JsonReadable
import org.talares.api.datatypes.items.Item

import scala.collection.mutable
import scala.reflect.ClassTag

/**
 * Functions as a conduit between the public API and the other actors needed to turn a
 * [[org.talares.api.actors.messages.MediatorMessages.Request]] into a
 * [[org.talares.api.actors.messages.MediatorMessages.Response]].
 *
 * Takes care of spawning the correct [[org.talares.api.actors.Fetcher]]'s corresponding to the type of
 * [[org.talares.api.actors.messages.MediatorMessages.Request]]. Also leverages the chosen
 * [[org.talares.api.cache.actors.CachingActor]].
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
class Mediator(val app: Talares, cache: Cache) extends BaseActor {

  val cachingActor = cache.toCachingActor(app, context)

  /** Cache which prevents duplicate creation of [[org.talares.api.actors.Fetcher]]'s of a specific type **/
  val fetcherCache = mutable.Map[String, ActorRef]()

  /**
   * Either creates a [[org.talares.api.actors.Fetcher]] of the correct type or fetches it from the fetcherCache if one
   * is already present.
   *
   * @param jsonReadable implicit proof that the specified type can be read from a Json representation.
   * @param classTag the class tag of the specified type injected by the compiler, used for naming the fetcher
   * @tparam T the specific item type for which to create a [[org.talares.api.actors.Fetcher]]
   * @return a [[org.talares.api.actors.Fetcher]] of the given type
   */
  def fetcher[T](implicit jsonReadable: JsonReadable[T], classTag: ClassTag[T]): ActorRef = {

    val fetcherName = s"${classTag.runtimeClass.getSimpleName.toLowerCase}-fetcher"

    fetcherCache.get(fetcherName).fold {

      val newFetcher = context.actorOf(Props(new Fetcher[T](app)), fetcherName)
      fetcherCache.put(fetcherName, newFetcher)
      newFetcher

    }(aFetcher => aFetcher)
  }

  /**
   * Takes a [[org.talares.api.actors.messages.MediatorMessages.Request]] and morphs it into the correct
   * [[org.talares.api.actors.messages.FetcherMessages.Task]].
   * Then sends a [[org.talares.api.cache.actors.messages.CachingActorMessages.RetrieveFromCache]] to a
   * [[org.talares.api.cache.actors.CachingActor]] holding  a reference to the created message.
   *
   * @param request a [[org.talares.api.actors.messages.MediatorMessages.Request]] holding the information needed to
   *                create a proper [[org.talares.api.actors.messages.FetcherMessages.Task]]
   */
  def handleRequest[T <: Item](request: Request[T]): Unit = {

    implicit val jsonReadable = request.jsonReadable
    implicit val classTag = request.classTag

    val client = sender()

    val message = request match {
      case request: URIRequest[T] =>
        FetcherMessages.FetchByURI(client, request.uri)
      case request: IDRequest[T] =>
        FetcherMessages.FetchByID(client, request.webserviceLocation, request.IDs: _*)
      case request: SearchRequest[T] =>
        FetcherMessages.FetchBySearch(client, request.webserviceLocation, request.searchParams: _*)
      case request: QueryRequest[T] =>
        FetcherMessages.FetchByQuery(client, request.webserviceLocation, request.query)
    }

    cachingActor ! CachingActorMessages.RetrieveFromCache(message)
  }

  /**
   * Takes a [[org.talares.api.actors.messages.FetcherMessages.Result]] and sends a
   * [[org.talares.api.actors.messages.MediatorMessages.Response]] back to the client.
   *
   * @param result the response originating from a [[org.talares.api.actors.Fetcher]] and passed on by a
   *               [[org.talares.api.cache.actors.CachingActor]]
   */
  def handleResult(result: FetcherMessages.Result[_]): Unit = {

    val client = result.task.client

    result match {
      case FetcherMessages.Success(request, value) =>
        client ! Response(value)
        cachingActor ! CachingActorMessages.StoreInCache(request, value)
      case FetcherMessages.Failure(request, throwable) =>
        client ! Status.Failure(throwable)
        if (Settings.cacheOnFailure) cachingActor ! CachingActorMessages.StoreInCache(request, throwable)
    }
  }

  override def receive: Receive = LoggingReceive {
    case request: Request[_] => handleRequest(request)
    case result: FetcherMessages.Result[_] => handleResult(result)
    case CachingActorMessages.Found(request, value) => value match {
      case throwable: Throwable => request.client ! Status.Failure(throwable)
      case _ => request.client ! Response(value)
    }
    case CachingActorMessages.NotFound(request: FetcherMessages.Task[_]) =>

      implicit val jsonReadable = request.jsonReadable
      implicit val classTag = request.classTag

      fetcher ! request
  }
}