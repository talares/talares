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
 * A Scala representation of a BinaryVariant content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/BinaryVariants(BinaryId=123,PublicationId=1,VariantId='default')",
 *      "type": "Tridion.ContentDelivery.BinaryVariant"
 *    },
 *    "URLPath": "/dir/image.jpg",
 *    "BinaryId": 123,
 *    "Description": null,
 *    "IsComponent": true,
 *    "Path": "/dir/image.jpg",
 *    "PublicationId": 1,
 *    "Type": "image/jpeg",
 *    "VariantId": "default",
 *    "Binary": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/BinaryVariants(BinaryId=123,PublicationId=1,VariantId='default')/Binary"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class BinaryVariant(metadata: Metadata,
                         publicationId: Int,
                         binaryId: Int,
                         `type`: String,
                         URLPath: String,
                         isComponent: Boolean,
                         path: String,
                         variantId: String,
                         description: Option[String],
                         binary: Deferred[Binary]) extends Item

object BinaryVariant {

  implicit object BinaryVariantJsonReadable extends JsonReadable[BinaryVariant] {

    override implicit val reads = BinaryVariant.reads
  }

  implicit lazy val reads = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "PublicationId").read[Int] and
      (JsPath \ "BinaryId").read[Int] and
      (JsPath \ "Type").read[String] and
      (JsPath \ "URLPath").read[String] and
      (JsPath \ "IsComponent").read[Boolean] and
      (JsPath \ "Path").read[String] and
      (JsPath \ "VariantId").read[String] and
      (JsPath \ "Description").readNullable[String] and
      (JsPath \ "Binary").read[Deferred[Binary]]
    )(BinaryVariant.apply _)

  implicit lazy val deferredReads = Deferred.reads[BinaryVariant]

  implicit lazy val deferredListReads = DeferredSeq.reads[BinaryVariant]
}