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
import org.talares.api.Talares
import org.talares.api.actors.messages.FetcherMessages
import org.talares.api.cache.actors.messages.CachingActorMessages.{Found, NotFound}
import org.talares.api.cache.{Cache, CacheItem}

/**
 * A [[CachingActor]] which uses the supplied cache implementation.
 * It will honor the configuration of the cache implementation as it will not add any cache strategy of it's own.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
class SimpleCachingActor(val app: Talares, cache: Cache) extends CachingActor with ActorLogging {

  import context.parent

  override def handleFetch(fetcherTask: FetcherMessages.Task[_]): Unit = {
    cache.get(fetcherTask.cacheKey) match {
      case Some(cacheItem: CacheItem) => parent ! Found(fetcherTask, cacheItem.value)
      case _ => parent ! NotFound(fetcherTask)
    }
  }

  override def handleStore(fetcherTask: FetcherMessages.Task[_], value: Any): Unit =
    cache.put(fetcherTask.cacheKey, CacheItem(value))
}