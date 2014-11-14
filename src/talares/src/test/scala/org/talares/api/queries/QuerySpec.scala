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
package org.talares.api.queries

import org.specs2.mutable.Specification

/**
 * @author Dennis Vis
 * @version 0.1.0
 */
class QuerySpec extends Specification {

  "Query" should {

    "create query 1" in {
      val query = """/Pages/Component(ItemId=123,PublicationId=1)?$filter=Title%20eq%20'Test Title'%20and%20Url%20ne%20'Foo'"""
      val q = Query / "Pages" / "Component" %("ItemId" -> 123, "PublicationId" -> 1) $ ("Title" ==| "Test Title") && ("Url" !=| "Foo")
      q.value must beEqualTo(query)
    }

    "create query 2" in {
      val query = """/Pages(ItemId=123)/Components(ComponentId=321)/Title?$filter=Url%20eq%20'Foo'"""
      val q = Query / "Pages" % ("ItemId" -> 123) / "Components" % ("ComponentId" -> 321) / "Title" $ ("Url" ==| "Foo")
      q.value must beEqualTo(query)
    }

    "create query 3" in {
      val query = """/Keywords(Id=1,PublicationId=1,TaxonomyId=123)?$filter=Depth%20gt%201"""
      val q = Query / "Keywords" %("Id" -> 1, "PublicationId" -> 1, "TaxonomyId" -> 123) $ ("Depth" >| 1)
      q.value must beEqualTo(query)
    }
  }
}