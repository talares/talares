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

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.TestActorRef
import org.talares.api.Talares
import org.talares.api.actors.Fetcher
import org.talares.api.datatypes.JsonReadable

import scala.reflect.ClassTag

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
class MockFetcher[T](app: Talares, shouldFail: Boolean = false)
                    (implicit jsonReadable: JsonReadable[T], classTag: ClassTag[T]) extends Fetcher[T](app) {

  override lazy val executor = {
    val selfRef = self
    if (shouldFail) MockExecutor.failingExecutorRef(app, selfRef)
    else MockExecutor.mockExecutorRef(app, selfRef)
  }
}

object MockFetcher {

  def mockFetcherRef[T](app: Talares, supervisor: ActorRef)
                       (implicit system: ActorSystem, jsonReadable: JsonReadable[T], classTag: ClassTag[T]): TestActorRef[Fetcher[T]] =
    TestActorRef[Fetcher[T]](Props(new MockFetcher[T](app)), supervisor, s"mock-fetcher-${UUID.randomUUID}")

  def failingFetcherRef[T](app: Talares, supervisor: ActorRef)
                          (implicit system: ActorSystem, jsonReadable: JsonReadable[T], classTag: ClassTag[T]): TestActorRef[Fetcher[T]] =
    TestActorRef[Fetcher[T]](
      Props(new MockFetcher[T](app, shouldFail = true)), supervisor, s"failing-fetcher-${UUID.randomUUID}"
    )

}