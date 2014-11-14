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
import org.talares.api.actors.Executor
import org.talares.api.datatypes.items.stubs.ItemStubs._
import org.talares.api.exceptions.ServiceErrorException
import play.api.libs.json.JsValue

import scala.concurrent.Future
import scala.reflect.ClassTag

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
class MockExecutor[T](app: Talares, shouldFail: Boolean = false) extends Executor[T](app) {

  override def callService(location: String): Future[Either[Throwable, JsValue]] = {
    if (shouldFail) fail(location)
    else success(location)
  }

  private def success(location: String): Future[Either[Throwable, JsValue]] = Future.successful {

    val filter = location.contains("$filter")
    val hostStripped = location.split(".*odata.svc/")(1)
    val uri = hostStripped.split("\\?")(0)
    val parts = uri.split("/")
    val lastPart = parts(parts.length - 1)
    val idRequest = lastPart.contains("(")
    val endpoint = lastPart.replaceAll("\\(.*\\)", "")

    val multi = filter || !idRequest

    Right(
      endpoint match {
        case "Binaries" if multi => binariesStub
        case "Binaries" => binaryStub
        case "BinaryContents" if multi => binaryContentsStub
        case "BinaryContents" => binaryContentStub
        case "BinaryVariants" if multi => binaryVariantsStub
        case "BinaryVariants" => binaryVariantStub
        case "Components" if multi => componentsStub
        case "Components" => componentStub
        case "ComponentPresentations" if multi => componentPresentationsStub
        case "ComponentPresentations" => componentPresentationStub
        case "CustomMetas" if multi => customMetasStub
        case "CustomMetas" => customMetaStub
        case "Keywords" if multi => keywordsStub
        case "Keywords" => keywordStub
        case "Pages" if multi => pagesStub
        case "Pages" => pageStub
        case "PageContents" if multi => pageContentsStub
        case "PageContents" => pageContentStub
        case "Publications" if multi => publicationsStub
        case "Publications" => publicationStub
        case "Schemas" if multi => schemasStub
        case "Schemas" => schemaStub
        case "StructureGroups" if multi => structureGroupsStub
        case "StructureGroups" => structureGroupStub
        case "Templates" if multi => templatesStub
        case "Templates" => templateStub
        case _ => throw new UnsupportedOperationException(s"Unknown endpoint: $endpoint")
      }
    )
  }

    def fail(location: String): Future[Either[Throwable, JsValue]] = Future.successful {
      Left(ServiceErrorException(location, new java.util.concurrent.TimeoutException))
    }
  }

  object MockExecutor {

    def mockExecutorRef[T](app: Talares, supervisor: ActorRef)
                          (implicit system: ActorSystem, classTag: ClassTag[T]): TestActorRef[Executor[T]] =
      TestActorRef[Executor[T]](Props(new MockExecutor[T](app)), supervisor, s"mock-executor-${UUID.randomUUID}")

    def failingExecutorRef[T](app: Talares, supervisor: ActorRef)
                             (implicit system: ActorSystem, classTag: ClassTag[T]): TestActorRef[Executor[T]] =
      TestActorRef[Executor[T]](
        Props(new MockExecutor[T](app, shouldFail = true)), supervisor, s"failing-executor-${UUID.randomUUID}"
      )
  }