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
import org.talares.api.datatypes.{Deferred, DeferredSeq, JsonReadable, Metadata}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

/**
 * A Scala representation of a Page content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)",
 *      "type": "Tridion.ContentDelivery.Page"
 *    },
 *    "Author": null,
 *    "CreationDate": "/Date(1362655241000+60)/",
 *    "InitialPublishDate": "/Date(1371220702600+120)/",
 *    "ItemId": 123,
 *    "LastPublishDate": "/Date(1371793702520+120)/",
 *    "MajorVersion": 1,
 *    "MinorVersion": 2,
 *    "ModificationDate": "/Date(1363341350000+60)/",
 *    "OwningPublication": 1,
 *    "PagePath": "/path/file.doc",
 *    "PublicationId": 1,
 *    "TemplateId": 123,
 *    "Title": "A File",
 *    "Url": "/path/file.doc",
 *    "PageContent": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/PageContent"
 *      }
 *    },
 *    "StructureGroup": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/StructureGroup"
 *      }
 *    },
 *    "ComponentPresentations": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/ComponentPresentations"
 *      }
 *    },
 *    "Keywords": {
 *     "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/Keywords"
 *      }
 *    },
 *    "CustomMetas": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/CustomMetas"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class Page(metadata: Metadata,
                publicationId: Int,
                itemId: Int,
                templateId: Option[Int],
                title: Option[String],
                author: Option[String],
                creationDate: Option[DateTime],
                initialPublishDate: Option[DateTime],
                lastPublishDate: Option[DateTime],
                modificationDate: Option[DateTime],
                majorVersion: Option[Int],
                minorVersion: Option[Int],
                owningPublication: Option[Int],
                pagePath: Option[String],
                url: Option[String],
                pageContent: Deferred[PageContent],
                structureGroup: Deferred[StructureGroup],
                componentPresentations: DeferredSeq[ComponentPresentation],
                keywords: DeferredSeq[Keyword],
                customMetas: DeferredSeq[CustomMeta]) extends Taxonomised

object Page {

  implicit object PageJsonReadable extends JsonReadable[Page] {

    override implicit lazy val reads = Page.reads
  }

  implicit lazy val reads: Reads[Page] = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "PublicationId").read[Int] and
      (JsPath \ "ItemId").read[Int] and
      (JsPath \ "TemplateId").readNullable[Int] and
      (JsPath \ "Title").readNullable[String] and
      (JsPath \ "Author").readNullable[String] and
      (JsPath \ "CreationDate").readNullable[DateTime] and
      (JsPath \ "InitialPublishDate").readNullable[DateTime] and
      (JsPath \ "LastPublishDate").readNullable[DateTime] and
      (JsPath \ "ModificationDate").readNullable[DateTime] and
      (JsPath \ "MajorVersion").readNullable[Int] and
      (JsPath \ "MinorVersion").readNullable[Int] and
      (JsPath \ "OwningPublication").readNullable[Int] and
      (JsPath \ "PagePath").readNullable[String] and
      (JsPath \ "Url").readNullable[String] and
      (JsPath \ "PageContent").read[Deferred[PageContent]] and
      (JsPath \ "StructureGroup").read[Deferred[StructureGroup]] and
      (JsPath \ "ComponentPresentations").read[DeferredSeq[ComponentPresentation]] and
      (JsPath \ "Keywords").read[DeferredSeq[Keyword]] and
      (JsPath \ "CustomMetas").read[DeferredSeq[CustomMeta]]
    )(Page.apply _)

  implicit lazy val deferredReads = Deferred.reads[Page]

  implicit lazy val deferredListReads = DeferredSeq.reads[Page]
}