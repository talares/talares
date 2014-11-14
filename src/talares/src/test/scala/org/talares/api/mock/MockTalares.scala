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
package org.talares.api.mock

import akka.actor.Props
import org.talares.api.Talares
import org.talares.api.actors.mock.MockMediator
import org.talares.api.cache.{Cache, NoCache}

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
class MockTalares(override val cache: Cache = NoCache()) extends Talares(cache) {

  override lazy val mediator = system.actorOf(Props(new MockMediator(this, cache)), "mock-mediator")
}

object MockTalares {

  def apply(cache: Cache = NoCache()): MockTalares = new MockTalares(cache)
}