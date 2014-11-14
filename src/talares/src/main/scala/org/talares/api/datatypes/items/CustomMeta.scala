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
 * A Scala representation of a CustomMeta content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)",
 *      "type": "Tridion.ContentDelivery.CustomMeta"
 *    },
 *    "DateValue": null,
 *    "FloatValue": null,
 *    "Id": 123,
 *    "ItemId": 123,
 *    "ItemType": 1,
 *    "KeyName": "key",
 *    "PublicationId": 1,
 *    "StringValue": "A String",
 *    "Component": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)/Component"
 *      }
 *    },
 *    "Page": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)/Page"
 *      }
 *    },
 *    "Keyword": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)/Keyword"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class CustomMeta(metadata: Metadata,
                      publicationId: Int,
                      itemId: Int,
                      id: Int,
                      itemType: Int,
                      keyName: String,
                      dateValue: Option[DateTime],
                      floatValue: Option[Float],
                      stringValue: Option[String],
                      component: Deferred[Component],
                      page: Deferred[Page],
                      keyword: Deferred[Keyword]) extends Item

object CustomMeta {

  implicit object CustomMetaJsonReadable extends JsonReadable[CustomMeta] {

    override implicit lazy val reads = CustomMeta.reads
  }

  implicit lazy val reads = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "PublicationId").read[Int] and
      (JsPath \ "ItemId").read[Int] and
      (JsPath \ "Id").read[Int] and
      (JsPath \ "ItemType").read[Int] and
      (JsPath \ "KeyName").read[String] and
      (JsPath \ "DateValue").readNullable[DateTime] and
      (JsPath \ "FloatValue").readNullable[Float] and
      (JsPath \ "StringValue").readNullable[String] and
      (JsPath \ "Component").read[Deferred[Component]] and
      (JsPath \ "Page").read[Deferred[Page]] and
      (JsPath \ "Keyword").read[Deferred[Keyword]]
    )(CustomMeta.apply _)

  implicit lazy val deferredReads = Deferred.reads[CustomMeta]

  implicit lazy val deferredListReads = DeferredSeq.reads[CustomMeta]
}