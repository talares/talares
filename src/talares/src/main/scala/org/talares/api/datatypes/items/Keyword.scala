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
package org.talares.api.datatypes.items

import org.talares.api.datatypes._
import play.api.libs.functional.syntax._
import play.api.libs.json.JsPath

/**
 * A Scala representation of a Keyword content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(PublicationId=1,Id=2)",
 *      "type": "Tridion.ContentDelivery.Keyword"
 *    },
 *    "Id": 123,
 *    "PublicationId": 1,
 *    "TaxonomyId": 123,
 *    "Title": "A title",
 *    "Description": "A description",
 *    "HasChildren": false,
 *    "Abstract": false,
 *    "Navigable": false,
 *    "Key": "A key",
 *    "Depth": 0,
 *    "ItemType": 1,
 *    "TotalRelatedItems": 1,
 *    "Components": {
 *      "__deferred": {
 *          "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Components"
 *      }
 *    },
 *    "Pages": {
 *      "__deferred": {
 *          "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Pages"
 *      }
 *    },
 *    "CustomMetas": {
 *      "__deferred": {
 *          "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/CustomMetas"
 *      }
 *    },
 *    "Children": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Children"
 *      }
 *    },
 *    "Parent": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Parent"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class Keyword(metadata: Metadata,
                   id: Int,
                   publicationId: Int,
                   taxonomyId: Int,
                   title: String,
                   description: Option[String],
                   hasChildren: Boolean,
                   isAbstract: Boolean,
                   navigable: Boolean,
                   key: Option[String],
                   depth: Int,
                   itemType: Int,
                   totalRelatedItems: Option[Int],
                   components: DeferredSeq[Component],
                   pages: DeferredSeq[Page],
                   customMetas: DeferredSeq[CustomMeta],
                   children: DeferredSeq[Keyword],
                   parent: Deferred[Keyword]) extends Item

object Keyword {

  implicit object KeywordJsonReadable extends JsonReadable[Keyword] {

    override implicit lazy val reads = Keyword.reads
  }

  implicit lazy val reads = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "Id").read[Int] and
      (JsPath \ "PublicationId").read[Int] and
      (JsPath \ "TaxonomyId").read[Int] and
      (JsPath \ "Title").read[String] and
      (JsPath \ "Description").readNullable[String] and
      (JsPath \ "HasChildren").read[Boolean] and
      (JsPath \ "Abstract").read[Boolean] and
      (JsPath \ "Navigable").read[Boolean] and
      (JsPath \ "Key").readNullable[String] and
      (JsPath \ "Depth").read[Int] and
      (JsPath \ "ItemType").read[Int] and
      (JsPath \ "TotalRelatedItems").readNullable[Int] and
      (JsPath \ "Components").read[DeferredSeq[Component]] and
      (JsPath \ "Pages").read[DeferredSeq[Page]] and
      (JsPath \ "CustomMetas").read[DeferredSeq[CustomMeta]] and
      (JsPath \ "Children").read[DeferredSeq[Keyword]] and
      (JsPath \ "Parent").read[Deferred[Keyword]]
    )(Keyword.apply _)

  implicit lazy val deferredReads = Deferred.reads[Keyword]

  implicit lazy val deferredListReads = DeferredSeq.reads[Keyword]
}