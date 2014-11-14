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

import org.talares.api.datatypes.{Deferred, DeferredSeq, JsonReadable, Metadata}
import play.api.libs.functional.syntax._
import play.api.libs.json.JsPath

/**
 * A Scala representation of a BinaryContent content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Binaries(BinaryId=123,PublicationId=1)",
 *      "type": "Tridion.ContentDelivery.BinaryContent"
 *    },
 *    "BinaryId": 123,
 *    "PublicationId": 1,
 *    "VariantId": "123"
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class BinaryContent(metadata: Metadata, publicationId: Int, binaryId: Int, variantId: String) extends Item

object BinaryContent {

  implicit object BinaryContentJsonReadable extends JsonReadable[BinaryContent] {

    override implicit lazy val reads = BinaryContent.reads
  }

  implicit lazy val reads = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "PublicationId").read[Int] and
      (JsPath \ "BinaryId").read[Int] and
      (JsPath \ "VariantId").read[String]
    )(BinaryContent.apply _)

  implicit lazy val deferredReads = Deferred.reads[BinaryContent]

  implicit lazy val deferredListReads = DeferredSeq.reads[BinaryContent]
}