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

import org.joda.time.DateTime
import org.talares.api.datatypes._
import play.api.libs.functional.syntax._
import play.api.libs.json.JsPath

/**
 * A Scala representation of a Component content type.
 *
 * Example Json:
 * {{{
 *   {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)",
 *      "type": "Tridion.ContentDelivery.Component"
 *    },
 *    "Author": "User",
 *    "CreationDate": "/Date(1362655474000+60)/",
 *    "InitialPublishDate": "/Date(1374139562000+120)/",
 *    "Multimedia": false,
 *    "ItemId": 123,
 *    "LastPublishDate": "/Date(1374139562113+120)/",
 *    "MajorVersion": 1,
 *    "MinorVersion": 2,
 *    "ModificationDate": "/Date(1374139455000+120)/",
 *    "OwningPublication": 1,
 *    "PublicationId": 1,
 *    "SchemaId": 123,
 *    "Title": "component title",
 *    "Schema": {
 *      "__deferred": {
 *         "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/Schema"
 *      }
 *    },
 *    "ComponentPresentations": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/ComponentPresentations"
 *      }
 *    },
 *    "Keywords": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/Keywords"
 *      }
 *    },
 *    "CustomMetas": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/CustomMetas"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class Component(metadata: Metadata,
                     publicationId: Int,
                     itemId: Int,
                     title: Option[String],
                     author: Option[String],
                     creationDate: Option[DateTime],
                     initialPublishDate: Option[DateTime],
                     lastPublishDate: Option[DateTime],
                     modificationDate: Option[DateTime],
                     majorVersion: Option[Int],
                     minorVersion: Option[Int],
                     owningPublication: Option[Int],
                     multimedia: Option[Boolean],
                     schemaId: Int,
                     schema: Deferred[Schema],
                     componentPresentations: DeferredSeq[ComponentPresentation],
                     keywords: DeferredSeq[Keyword],
                     customMetas: DeferredSeq[CustomMeta]) extends Taxonomised

object Component {

  implicit object ComponentJsonReadable extends JsonReadable[Component] {

    override implicit lazy val reads = Component.reads
  }

  implicit lazy val reads = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "PublicationId").read[Int] and
      (JsPath \ "ItemId").read[Int] and
      (JsPath \ "Title").readNullable[String] and
      (JsPath \ "Author").readNullable[String] and
      (JsPath \ "CreationDate").readNullable[DateTime] and
      (JsPath \ "InitialPublishDate").readNullable[DateTime] and
      (JsPath \ "LastPublishDate").readNullable[DateTime] and
      (JsPath \ "ModificationDate").readNullable[DateTime] and
      (JsPath \ "MajorVersion").readNullable[Int] and
      (JsPath \ "MinorVersion").readNullable[Int] and
      (JsPath \ "OwningPublication").readNullable[Int] and
      (JsPath \ "Multimedia").readNullable[Boolean] and
      (JsPath \ "SchemaId").read[Int] and
      (JsPath \ "Schema").read[Deferred[Schema]] and
      (JsPath \ "ComponentPresentations").read[DeferredSeq[ComponentPresentation]] and
      (JsPath \ "Keywords").read[DeferredSeq[Keyword]] and
      (JsPath \ "CustomMetas").read[DeferredSeq[CustomMeta]]
    )(Component.apply _)

  implicit lazy val deferredReads = Deferred.reads[Component]

  implicit lazy val deferredListReads = DeferredSeq.reads[Component]
}