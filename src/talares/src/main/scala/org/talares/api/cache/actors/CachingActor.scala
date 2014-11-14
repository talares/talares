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
package org.talares.api.cache.actors

import akka.actor.ActorLogging
import akka.event.LoggingReceive
import org.talares.api.actors.BaseActor
import org.talares.api.actors.messages.FetcherMessages
import org.talares.api.cache.actors.messages.CachingActorMessages.{RetrieveFromCache, StoreInCache}

/**
 * Blueprint for caching actors.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
trait CachingActor extends BaseActor with ActorLogging {

  /**
   * Handles messages of the type [[org.talares.api.cache.actors.messages.CachingActorMessages.RetrieveFromCache]].
   *
   * These messages indicate a request for fetching a specific item from the cache using a predefined key.
   *
   * @param fetcherTask the [[org.talares.api.actors.messages.FetcherMessages.Task]] for reference when the parent is
   *                    contacted
   */
  def handleFetch(fetcherTask: FetcherMessages.Task[_]): Unit

  /**
   * Handles messages of the type [[org.talares.api.cache.actors.messages.CachingActorMessages.StoreInCache]].
   *
   * These messages indicate a request for storing a specific item in the cache using a predefined key.
   *
   * @param fetcherTask the [[org.talares.api.actors.messages.FetcherMessages.Task]] for reference when the parent is
   *                    contacted
   */
  def handleStore(fetcherTask: FetcherMessages.Task[_], value: Any): Unit

  override final def receive: Receive = LoggingReceive {
    case RetrieveFromCache(request) => handleFetch(request)
    case StoreInCache(request, value) => handleStore(request, value)
  }
}