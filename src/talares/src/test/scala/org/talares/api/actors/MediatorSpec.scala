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

import java.util.concurrent.TimeUnit

import akka.actor.{Status, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.specs2.mock.Mockito
import org.specs2.mutable.SpecificationLike
import org.talares.api.Talares
import org.talares.api.actors.messages.MediatorMessages
import org.talares.api.actors.mock.MockMediator
import org.talares.api.cache.mock.MockCaches
import org.talares.api.datatypes.items.Page
import org.talares.api.datatypes.items.stubs.ItemStubs
import org.talares.api.queries._

import scala.concurrent.duration.FiniteDuration

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
class MediatorSpec extends TestKit(ActorSystem("executor-spec", ConfigFactory.load()))
with ImplicitSender
with MockCaches
with Mockito
with SpecificationLike {

  val app = mock[Talares]
  val cache = mockSimpleCache

  val webserviceLocationStub = "http://www.example.com/cd_webservice/odata.svc"

  val mockMediatorRef = MockMediator.mockMediatorRef(app, cache)

  sequential

  "A Mediator" should {

    "process an URI request" in {

      implicit val jsonReadable = Page.PageJsonReadable

      val message = MediatorMessages.URIRequest[Page](webserviceLocationStub + "/Pages(PublicationId=1,ItemId=2)")
      val expected = ItemStubs.pageStub.as[Page]

      mockMediatorRef ! message

      val result = receiveOne(FiniteDuration(5000, TimeUnit.MILLISECONDS))
      result match {
        case MediatorMessages.Response(value) => value == expected
        case _ => false
      }
    }

    "process an ID request" in {

      implicit val jsonReadable = Page.PageJsonReadable

      val message = MediatorMessages.IDRequest[Page](webserviceLocationStub, "PublicationId" -> 1, "ItemId" -> 2)
      val expected = ItemStubs.pageStub.as[Page]

      mockMediatorRef ! message

      val result = receiveOne(FiniteDuration(5000, TimeUnit.MILLISECONDS))
      result match {
        case MediatorMessages.Response(value) => value == expected
        case _ => false
      }
    }

    "process a search request" in {

      implicit val jsonReadable = Page.PageJsonReadable

      val message = MediatorMessages.SearchRequest[Page](webserviceLocationStub, "Url" -> "/path")
      val expected = ItemStubs.pagesStub.as[Seq[Page]]

      mockMediatorRef ! message

      val result = receiveOne(FiniteDuration(5000, TimeUnit.MILLISECONDS))
      result match {
        case MediatorMessages.Response(value) => value == expected
        case _ => false
      }
    }

    "process a query request" in {

      implicit val jsonReadable = Page.PageJsonReadable

      val query = Query / "Pages" %("PublicationId" -> 1, "ItemId" -> 2)
      val message = MediatorMessages.QueryRequest[Page](webserviceLocationStub, query)
      val expected = ItemStubs.pageStub.as[Page]

      mockMediatorRef ! message

      val result = receiveOne(FiniteDuration(5000, TimeUnit.MILLISECONDS))
      result match {
        case MediatorMessages.Response(value) => value == expected
        case _ => false
      }
    }
  }

  step(shutdown())
}