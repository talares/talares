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

import org.talares.api.datatypes.{Metadata, JsonReadable, Deferred, DeferredSeq}
import play.api.libs.functional.syntax._
import play.api.libs.json.JsPath

/**
 * A Scala representation of a StructureGroup content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(PublicationId=1,Id=2)",
 *      "type": "Tridion.ContentDelivery.StructureGroup"
 *    },
 *    "Depth": 0,
 *    "Directory": null,
 *    "Id": 123,
 *    "PublicationId": 1,
 *    "Title": "A title",
 *    "Pages": {
 *      "__deferred": {
 *          "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(Id=2,PublicationId=1)/Pages"
 *      }
 *    },
 *    "Children": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(Id=2,PublicationId=1)/Children"
 *      }
 *    },
 *    "Parent": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(Id=2,PublicationId=1)/Parent"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class StructureGroup(metadata: Metadata,
                          publicationId: Int,
                          id: Int,
                          title: String,
                          depth: Int,
                          directory: Option[String],
                          pages: DeferredSeq[Page],
                          parent: Deferred[StructureGroup],
                          children: DeferredSeq[StructureGroup]) extends Item

object StructureGroup {

  implicit object StructureGroupJsonReadable extends JsonReadable[StructureGroup] {

    override implicit lazy val reads = StructureGroup.reads
  }

  implicit lazy val reads = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "PublicationId").read[Int] and
      (JsPath \ "Id").read[Int] and
      (JsPath \ "Title").read[String] and
      (JsPath \ "Depth").read[Int] and
      (JsPath \ "Directory").readNullable[String] and
      (JsPath \ "Pages").read[DeferredSeq[Page]] and
      (JsPath \ "Parent").read[Deferred[StructureGroup]] and
      (JsPath \ "Children").read[DeferredSeq[StructureGroup]]
    )(StructureGroup.apply _)

  implicit lazy val deferredReads = Deferred.reads[StructureGroup]

  implicit lazy val deferredListReads = DeferredSeq.reads[StructureGroup]
}