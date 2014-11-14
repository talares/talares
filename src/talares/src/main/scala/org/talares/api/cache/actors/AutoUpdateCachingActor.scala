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

import org.talares.api.Talares
import org.talares.api.actors.messages.FetcherMessages
import org.talares.api.cache.actors.messages.CachingActorMessages.{Found, NotFound}
import org.talares.api.cache.{Cache, CacheItem}

/**
 * A [[CachingActor]] which, after caching an item once, will always return a cached instance of this item.
 *
 * To do this it keeps the item updated asynchronously by sending a
 * [[org.talares.api.cache.actors.messages.CachingActorMessages.NotFound]] message to it's parent after a specified
 * amount of accesses to the cached item.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
class AutoUpdateCachingActor(val app: Talares, cache: Cache) extends CachingActor {

  import context.parent

  /**
   * The amount of times a [[CacheItem]] should be accessed before an update action is triggered.
   */
  val timesAccessedTrigger = Settings.cacheRefreshRatio

  override def handleFetch(fetcherTask: FetcherMessages.Task[_]): Unit = {

    val cacheKey = fetcherTask.cacheKey

    cache.get(cacheKey) match {
      case Some(cacheItem: CacheItem) =>

        parent ! Found(fetcherTask, cacheItem.value)

        val copy = cacheItem.copy(timesAccessed = cacheItem.timesAccessed + 1)
        cache.put(cacheKey, copy)

        if (cacheItem.timesAccessed >= timesAccessedTrigger) parent ! NotFound(fetcherTask)

      case _ => parent ! NotFound(fetcherTask)
    }
  }

  override def handleStore(fetcherTask: FetcherMessages.Task[_], value: Any): Unit =
    cache.put(fetcherTask.cacheKey, CacheItem(value))
}