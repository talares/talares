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

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.typesafe.config.ConfigFactory
import org.specs2.mock.Mockito
import org.specs2.mutable.SpecificationLike
import org.talares.api.Talares
import org.talares.api.actors.messages.{ExecutorMessages, FetcherMessages}
import org.talares.api.actors.mock.MockExecutor
import org.talares.api.datatypes.items.Page
import org.talares.api.datatypes.items.stubs.ItemStubs
import org.talares.api.datatypes.items.stubs.ItemStubs._
import play.api.libs.json.Json

import scala.concurrent.duration.FiniteDuration
import scala.reflect.ClassTag

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
class ExecutorSpec extends TestKit(ActorSystem("executor-spec", ConfigFactory.load()))
with Mockito
with SpecificationLike {

  implicit val app = mock[Talares]

  val webserviceLocationStub = "http://www.example.com/cd_webservice/odata.svc"
  val locationStub = webserviceLocationStub + "/Pages(PublicationId=1,ItemId=2)"

  implicit val jsonReadable = Page.PageJsonReadable
  val fetcherTaskStub = FetcherMessages.FetchByID(
    testActor, webserviceLocationStub, "PublicationId" -> 1, "ItemId" -> 2
  )

  val singleResult = Json.obj("d" -> pageStub)
  val multiResult = Json.obj("d" -> Json.obj("results" -> pagesStub))

  def mockExecutorRef[T](implicit classTag: ClassTag[T]) = MockExecutor.mockExecutorRef[T](app, testActor)

  def mockExecutor[T](implicit classTag: ClassTag[T]) = mockExecutorRef[T].underlyingActor

  def failingExecutorRef[T](implicit classTag: ClassTag[T]) = MockExecutor.failingExecutorRef[T](app, testActor)

  "An Executor" should {

    "add Json param" in {
      val urlWithParam = "http://www.google.com?$format=json"
      val urlWithoutParam = "http://www.google.com"
      mockExecutor[Page].addJsonParam(urlWithoutParam) == urlWithParam
    }

    "parse single Json result" in {
      mockExecutor[Page].parseJsonResult(singleResult) == pageStub
    }

    "parse multi Json result" in {
      mockExecutor[Page].parseJsonResult(multiResult) == pagesStub
    }

    "handle task" in {

      val message = ExecutorMessages.Execute[Page](fetcherTaskStub, locationStub)
      val expected = ExecutorMessages.Success[Page](fetcherTaskStub, ItemStubs.pageStub)

      mockExecutorRef[Page] ! message
      expected == receiveOne(FiniteDuration(5000, TimeUnit.MILLISECONDS))
    }

    "handle failure" in {

      failingExecutorRef[Page] ! ExecutorMessages.Execute[Page](fetcherTaskStub, locationStub)
      receiveOne(FiniteDuration(5000, TimeUnit.MILLISECONDS)) match {
        case ExecutorMessages.Failure(task, throwable) => task == fetcherTaskStub
        case _ => false
      }
    }
  }

  step(shutdown())
}