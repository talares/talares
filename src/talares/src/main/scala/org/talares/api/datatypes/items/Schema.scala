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
 * A Scala representation of a Schema content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Schemas(SchemaId=123,PublicationId=1)",
 *      "type": "Tridion.ContentDelivery.Schema"
 *    },
 *    "PublicationId": 1,
 *    "SchemaId": 123,
 *    "Title": "A title",
 *    "Components": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Schemas(SchemaId=123,PublicationId=1)/Components"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class Schema(metadata: Metadata,
                  publicationId: Int,
                  schemaId: Int,
                  title: String,
                  components: DeferredSeq[Component]) extends Item

object Schema {

  implicit object SchemaJsonReadable extends JsonReadable[Schema] {

    override implicit lazy val reads = Schema.reads
  }

  implicit lazy val reads = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "PublicationId").read[Int] and
      (JsPath \ "SchemaId").read[Int] and
      (JsPath \ "Title").read[String] and
      (JsPath \ "Components").read[DeferredSeq[Component]]
    )(Schema.apply _)

  implicit lazy val deferredReads = Deferred.reads[Schema]

  implicit lazy val deferredListReads = DeferredSeq.reads[Schema]
}