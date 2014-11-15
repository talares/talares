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
package org.talares.api

import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions
import org.talares.api.cache.mock.MockCaches
import org.talares.api.mock.MockTalares

import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
class TalaresSpec extends Specification with NoTimeConversions with MockCaches {

  def pageWithComponentPresentations(app: Talares): Future[Boolean] = {
    for {
      pages <- app.getPage("/path/of/page") if pages.nonEmpty
      cps <- pages(0).componentPresentations.value(app) if cps.nonEmpty
    } yield {
      cps(0).componentId == 123
    }
  }

  def loadTest(app: Talares): Future[Boolean] = {
    (0 to 1000).foldLeft(Future.successful[Boolean](true)) { (acc, future) =>
      acc flatMap (_ => pageWithComponentPresentations(app))
    } map { result =>
      app.terminate()
      result
    }
  }

  sequential

  "Talares" should {

    "instantiate" in {
      val app = Talares()
      val result = app != null
      result
    }

    "use no cache" in {

      val talaresNoCache = MockTalares()

      val test1 = pageWithComponentPresentations(talaresNoCache)
      val test2 = pageWithComponentPresentations(talaresNoCache)

      val tests = for {
        result1 <- test1
        result2 <- test2
      } yield {
        talaresNoCache.terminate()
        result1 && result2
      }

      tests.await(timeout = 30 seconds)
    }

    "use simple cache" in {

      val talaresSimpleCache = MockTalares(mockSimpleCache)

      lazy val test1 = pageWithComponentPresentations(talaresSimpleCache)
      lazy val test2 = pageWithComponentPresentations(talaresSimpleCache)

      val tests = for {
        result1 <- test1
        result2 <- test2
      } yield {
        talaresSimpleCache.terminate()
        result1 && result2
      }

      tests.await(timeout = 30 seconds)
    }

    "use auto update cache" in {

      val talaresAutoUpdateCache = MockTalares(mockAutoUpdateCache)

      lazy val test1 = pageWithComponentPresentations(talaresAutoUpdateCache)
      lazy val test2 = pageWithComponentPresentations(talaresAutoUpdateCache)
      lazy val test3 = pageWithComponentPresentations(talaresAutoUpdateCache)

      val tests = for {
        result1 <- test1
        result2 <- test2
        result3 <- test3
      } yield {
        talaresAutoUpdateCache.terminate()
        result1 && result2 && result3
      }

      tests.await(timeout = 30 seconds)
    }

    "perform under load with no cache" in {

      val talares = MockTalares()
      val test = loadTest(talares)

      test.await(timeout = 30 seconds)
    }

    "perform under load with simple cache" in {

      val talaresSimpleCache = MockTalares(mockSimpleCache)
      val test = loadTest(talaresSimpleCache)

      test.await(timeout = 30 seconds)
    }

    "perform under load with auto update cache" in {

      val talaresAutoUpdateCache = MockTalares(mockAutoUpdateCache)
      val test = loadTest(talaresAutoUpdateCache)

      test.await(timeout = 30 seconds)
    }

    "get a Binary" in {

      val talares = MockTalares()
      val test = talares.getBinary(1, 2) map { binary =>
        talares.terminate()
        binary.binaryId == 123
      }

      test.await(timeout = 30 seconds)
    }

    "get a BinaryContent" in {

      val talares = MockTalares()
      val test = talares.getBinaryContent(1, 2, "3") map { bc =>
        talares.terminate()
        bc.binaryId == 123
      }

      test.await(timeout = 30 seconds)
    }

    "get a BinaryVariant" in {

      val talares = MockTalares()
      val test = talares.getBinaryVariant(1, 2) map { bv =>
        talares.terminate()
        bv.binaryId == 123
      }

      test.await(timeout = 30 seconds)
    }

    "get a Component" in {

      val talares = MockTalares()
      val test = talares.getComponent(1, 2) map { component =>
        talares.terminate()
        component.itemId == 123
      }

      test.await(timeout = 30 seconds)
    }

    "get a ComponentPresentation" in {

      val talares = MockTalares()
      val test = talares.getComponentPresentation(1, 2, 3) map { cp =>
        talares.terminate()
        cp.componentId == 123
      }

      test.await(timeout = 30 seconds)
    }

    "get a CustomMeta" in {

      val talares = MockTalares()
      val test = talares.getCustomMeta(1) map { cm =>
        talares.terminate()
        cm.id == 123
      }

      test.await(timeout = 30 seconds)
    }

    "get a Keyword" in {

      val talares = MockTalares()
      val test = talares.getKeyword(1, 2, 3) map { keyword =>
        talares.terminate()
        keyword.taxonomyId == 123
      }

      test.await(timeout = 30 seconds)
    }

    "get a Page" in {

      val talares = MockTalares()
      val test = talares.getPage(1, 2) map { page =>
        talares.terminate()
        page.itemId == 123
      }

      test.await(timeout = 30 seconds)
    }

    "get a PageContent" in {

      val talares = MockTalares()
      val test = talares.getPageContent(1, 2) map { pageContent =>
        talares.terminate()
        pageContent.pageId == 123
      }

      test.await(timeout = 30 seconds)
    }

    "get a Publication" in {

      val talares = MockTalares()
      val test = talares.getPublication(1) map { publication =>
        talares.terminate()
        publication.id == 1
      }

      test.await(timeout = 30 seconds)
    }

    "get a Schema" in {

      val talares = MockTalares()
      val test = talares.getSchema(1, 2) map { schema =>
        talares.terminate()
        schema.schemaId == 123
      }

      test.await(timeout = 30 seconds)
    }

    "get a StructureGroup" in {

      val talares = MockTalares()
      val test = talares.getStructureGroup(1, 2) map { sg =>
        talares.terminate()
        sg.depth == 0
      }

      test.await(timeout = 30 seconds)
    }

    "get a Template" in {

      val talares = MockTalares()
      val test = talares.getTemplate(1, 2) map { template =>
        talares.terminate()
        template.itemId == 123
      }

      test.await(timeout = 30 seconds)
    }
  }
}