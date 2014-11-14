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

import org.talares.api.datatypes._
import play.api.libs.functional.syntax._
import play.api.libs.json.JsPath

/**
 * A Scala representation of a ComponentPresentation content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)",
 *      "type": "Tridion.ContentDelivery.ComponentPresentation"
 *    },
 *    "ComponentId": 123,
 *    "OutputFormat": "HTML Fragment",
 *    "PresentationContent": "<h1>Some content</h1>",
 *    "PublicationId": 1,
 *    "TemplateId": 123,
 *    "Component": {
 *     "__deferred": {
 *          "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)/Component"
 *      }
 *    },
 *    "Template": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)/Template"
 *      }
 *    },
 *    "Pages": {
 *      "__deferred": {
 *         "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)/Pages"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class ComponentPresentation(metadata: Metadata,
                                 publicationId: Int,
                                 componentId: Int,
                                 templateId: Int,
                                 outputFormat: String,
                                 presentationContent: String,
                                 component: Deferred[Component],
                                 template: Deferred[Template],
                                 pages: DeferredSeq[Page]) extends Item

object ComponentPresentation {

  implicit object ComponentPresentationJsonReadable extends JsonReadable[ComponentPresentation] {

    override implicit lazy val reads = ComponentPresentation.reads
  }

  implicit lazy val reads = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "PublicationId").read[Int] and
      (JsPath \ "ComponentId").read[Int] and
      (JsPath \ "TemplateId").read[Int] and
      (JsPath \ "OutputFormat").read[String] and
      (JsPath \ "PresentationContent").read[String] and
      (JsPath \ "Component").read[Deferred[Component]] and
      (JsPath \ "Template").read[Deferred[Template]] and
      (JsPath \ "Pages").read[DeferredSeq[Page]]
    )(ComponentPresentation.apply _)

  implicit lazy val deferredReads = Deferred.reads[ComponentPresentation]

  implicit lazy val deferredListReads = DeferredSeq.reads[ComponentPresentation]
}