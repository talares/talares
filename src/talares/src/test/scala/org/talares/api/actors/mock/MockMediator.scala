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
package org.talares.api.actors.mock

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.TestActorRef
import org.talares.api.Talares
import org.talares.api.actors.Mediator
import org.talares.api.cache.Cache
import org.talares.api.datatypes.JsonReadable

import scala.reflect.ClassTag

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
class MockMediator(app: Talares, cache: Cache, shouldFail: Boolean = false) extends Mediator(app, cache) {

  override def fetcher[T](implicit jsonReadable: JsonReadable[T], classTag: ClassTag[T]): ActorRef = {
    val selfRef = self
    if (shouldFail) MockFetcher.failingFetcherRef[T](app, selfRef)
    else MockFetcher.mockFetcherRef[T](app, selfRef)
  }
}

object MockMediator {

  def mockMediatorRef(app: Talares, cache: Cache)(implicit system: ActorSystem): TestActorRef[Mediator] =
    TestActorRef[Mediator](new MockMediator(app, cache))
}