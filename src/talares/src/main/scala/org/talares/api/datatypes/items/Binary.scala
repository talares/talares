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
 * A Scala representation of a Binary content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Binaries(BinaryId=123,PublicationId=1)",
 *      "type": "Tridion.ContentDelivery.Binary"
 *    },
 *    "BinaryId": 123,
 *    "PublicationId": 1,
 *    "Type": "application/vnd.ms-fontobject",
 *    "BinaryVariants": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Binaries(BinaryId=123,PublicationId=1)/BinaryVariants"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class Binary(metadata: Metadata,
                  publicationId: Int,
                  binaryId: Int,
                  `type`: String,
                  binaryVariants: DeferredSeq[BinaryVariant]) extends Item

object Binary {

  implicit object BinaryJsonReadable extends JsonReadable[Binary] {

    override implicit lazy val reads = Binary.reads
  }

  implicit lazy val reads = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "PublicationId").read[Int] and
      (JsPath \ "BinaryId").read[Int] and
      (JsPath \ "Type").read[String] and
      (JsPath \ "BinaryVariants").read[DeferredSeq[BinaryVariant]]
    )(Binary.apply _)

  implicit lazy val deferredReads = Deferred.reads[Binary]

  implicit lazy val deferredListReads = DeferredSeq.reads[Binary]
}