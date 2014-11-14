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
package org.talares.api.cache

import akka.actor.{ActorContext, ActorRef, Props}
import org.talares.api.Talares
import org.talares.api.cache.actors.{NoCachingActor, SimpleCachingActor, AutoUpdateCachingActor}

/**
 * Trait whose instances allow the library to make use of a given cache implementation. This leaves the user free
 * to supply a cache solution of his own choosing.
 *
 * An instance of one of the implementing types should be provided at library instantiation. When no cache instance is
 * provided the [[NoCache]] strategy is assumed.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
trait Cache {

  def get: Any => Option[Any]

  def put: (Any, Any) => Unit

  def toCachingActor(app: Talares, context: ActorContext): ActorRef
}

/**
 * Does not apply any caching.
 */
case class NoCache() extends Cache {

  lazy val get: Any => Option[Any] = throw new UnsupportedOperationException

  lazy val put: (Any, Any) => Unit = throw new UnsupportedOperationException

  override def toCachingActor(app: Talares, context: ActorContext): ActorRef =
    context.actorOf(Props(new NoCachingActor(app)), "no-cache")
}

/**
 * Leverages the given functions get and set to either retrieve or store objects from and into the cache respectively.
 *
 * Does not add any additional caching logic and as such honors the given cache's configuration.
 *
 * @param get function which can be used to retrieve object from the cache with a given key
 * @param put function which can be used to store objects in the cache with a given key
 */
case class SimpleCache(get: Any => Option[Any], put: (Any, Any) => Unit) extends Cache {

  override def toCachingActor(app: Talares, context: ActorContext): ActorRef =
    context.actorOf(Props(new SimpleCachingActor(app, this)), "simple-cache")
}

/**
 * Leverages the given functions get and set to either retrieve or store objects from and into the cache respectively.
 *
 * Always returns a cached instance of a specific item once it has entered the cache once.
 *
 * A configurable amount of times will be accepted for accessing a specific item in the cache, before this strategy
 * issues an update of said item.
 *
 * @param get function which can be used to retrieve object from the cache with a given key
 * @param put function which can be used to store objects in the cache with a given key
 */
case class AutoUpdateCache(get: Any => Option[Any], put: (Any, Any) => Unit) extends Cache {

  override def toCachingActor(app: Talares, context: ActorContext): ActorRef =
    context.actorOf(Props(new AutoUpdateCachingActor(app, this)), "auto-update-cache")
}