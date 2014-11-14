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
import org.talares.api.cache.actors.messages.CachingActorMessages.NotFound

/**
 * A [[CachingActor]] which does not apply any caching. It simply returns a
 * [[org.talares.api.cache.actors.messages.CachingActorMessages.NotFound]] on all
 * [[org.talares.api.cache.actors.messages.CachingActorMessages.RetrieveFromCache]] messages.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
class NoCachingActor(val app: Talares) extends CachingActor {

  import context.parent

  override def handleFetch(fetcherTask: FetcherMessages.Task[_]): Unit = parent ! NotFound(fetcherTask)

  override def handleStore(fetcherTask: FetcherMessages.Task[_], value: Any): Unit = {}
}