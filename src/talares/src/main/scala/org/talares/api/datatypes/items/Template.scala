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
import org.talares.api.datatypes.{Metadata, JsonReadable, Deferred, DeferredSeq}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

/**
 * A Scala representation of a Template content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Templates(PublicationId=1,ItemId=123)",
 *      "type": "Tridion.ContentDelivery.Template"
 *    },
 *    "Author": "User",
 *    "CreationDate": "/Date(1399546761343+120)/",
 *    "InitialPublishDate": "/Date(1399546761343+120)/",
 *    "ItemId": 123,
 *    "LastPublishDate": "/Date(1399546761343+120)/",
 *    "MajorVersion": 1,
 *    "MinorVersion": 2,
 *    "ModificationDate": "/Date(1399546761343+120)/",
 *    "OutputFormat": "HTML Fragment",
 *    "OwningPublication": 0,
 *    "PublicationId": 1,
 *    "TemplatePriority": 200,
 *    "Title": "A title",
 *    "ComponentPresentations": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Templates(PublicationId=1,ItemId=123)/ComponentPresentations"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class Template(metadata: Metadata,
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
                    outputFormat: String,
                    templatePriority: Int,
                    componentPresentations: DeferredSeq[ComponentPresentation]) extends ComponentPresentationsHolder

object Template {

  implicit object TemplateJsonReadable extends JsonReadable[Template] {

    override implicit lazy val reads = Template.reads
  }

  implicit val reads: Reads[Template] = (
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
      (JsPath \ "OutputFormat").read[String] and
      (JsPath \ "TemplatePriority").read[Int] and
      (JsPath \ "ComponentPresentations").read[DeferredSeq[ComponentPresentation]]
    )(Template.apply _)

  implicit lazy val deferredReads = Deferred.reads[Template]

  implicit lazy val deferredListReads = DeferredSeq.reads[Template]
}