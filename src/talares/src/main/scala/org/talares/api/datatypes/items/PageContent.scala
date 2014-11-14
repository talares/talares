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
 * A Scala representation of a PageContent content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/PageContents(PageId=123,PublicationId=1)",
 *      "type": "Tridion.ContentDelivery.PageContent"
 *    },
 *    "CharSet": "UTF8",
 *    "Content": "<h1>Some content</h1>",
 *    "PageId": 123,
 *    "PublicationId": 1,
 *    "Page": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/PageContents(PageId=123,PublicationId=1)/Page"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class PageContent(metadata: Metadata,
                       publicationId: Int,
                       pageId: Int,
                       charset: Option[String],
                       content: Option[String],
                       page: Deferred[Page]) extends Item

object PageContent {

  implicit object PageContentJsonReadable extends JsonReadable[PageContent] {

    override implicit val reads = PageContent.reads
  }

  implicit lazy val reads = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "PublicationId").read[Int] and
      (JsPath \ "PageId").read[Int] and
      (JsPath \ "CharSet").readNullable[String] and
      (JsPath \ "Content").readNullable[String] and
      (JsPath \ "Page").read[Deferred[Page]]
    )(PageContent.apply _)

  implicit lazy val deferredReads = Deferred.reads[PageContent]

  implicit lazy val deferredListReads = DeferredSeq.reads[PageContent]
}