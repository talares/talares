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
import play.api.libs.json.{JsPath, Reads}

/**
 * A Scala representation of a Publication content type.
 *
 * Example Json:
 * {{{
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)",
 *      "type": "Tridion.ContentDelivery.Publication"
 *    },
 *    "Id": 1,
 *    "Key": "A key",
 *    "MultimediaPath": "\\Multimedia",
 *    "MultimediaUrl": "/Multimedia",
 *    "PublicationPath": "\\",
 *    "PublicationUrl": "/",
 *    "Title": "A title",
 *    "Schemas": {
 *      "__deferred": {
 *           "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Schemas"
 *      }
 *    },
 *    "ComponentPresentations": {
 *     "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/ComponentPresentations"
 *      }
 *    },
 *    "Keywords": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Keywords"
 *      }
 *    },
 *    "Binaries": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Binaries"
 *       }
 *    },
 *    "BinaryVariants": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/BinaryVariants"
 *      }
 *    },
 *    "Components": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Components"
 *      }
 *    },
 *    "CustomMetas": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/CustomMetas"
 *      }
 *    },
 *    "Pages": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Pages"
 *      }
 *    },
 *    "PageContents": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/PageContents"
 *      }
 *    },
 *    "StructureGroups": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/StructureGroups"
 *      }
 *    },
 *    "Templates": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Publications(1)/Templates"
 *      }
 *    }
 *  }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class Publication(metadata: Metadata,
                       id: Int,
                       key: Option[String],
                       multimediaPath: Option[String],
                       multimediaUrl: Option[String],
                       publicationPath: Option[String],
                       publicationUrl: Option[String],
                       title: Option[String],
                       schemas: DeferredSeq[Schema],
                       componentPresentations: DeferredSeq[ComponentPresentation],
                       keywords: DeferredSeq[Keyword],
                       binaries: DeferredSeq[Binary],
                       binaryVariants: DeferredSeq[BinaryVariant],
                       components: DeferredSeq[Component],
                       customMetas: DeferredSeq[CustomMeta],
                       pages: DeferredSeq[Page],
                       pageContents: DeferredSeq[PageContent],
                       structureGroups: DeferredSeq[StructureGroup],
                       templates: DeferredSeq[Template]) extends Item {

  val publicationId = id
}

object Publication {

  implicit object PublicationJsonReadable extends JsonReadable[Publication] {

    override implicit lazy val reads = Publication.reads
  }

  implicit lazy val reads: Reads[Publication] = (
    (JsPath \ "__metadata").read[Metadata] and
      (JsPath \ "Id").read[Int] and
      (JsPath \ "Key").readNullable[String] and
      (JsPath \ "MultimediaPath").readNullable[String] and
      (JsPath \ "MultimediaUrl").readNullable[String] and
      (JsPath \ "PublicationPath").readNullable[String] and
      (JsPath \ "PublicationUrl").readNullable[String] and
      (JsPath \ "Title").readNullable[String] and
      (JsPath \ "Schemas").read[DeferredSeq[Schema]] and
      (JsPath \ "ComponentPresentations").read[DeferredSeq[ComponentPresentation]] and
      (JsPath \ "Keywords").read[DeferredSeq[Keyword]] and
      (JsPath \ "Binaries").read[DeferredSeq[Binary]] and
      (JsPath \ "BinaryVariants").read[DeferredSeq[BinaryVariant]] and
      (JsPath \ "Components").read[DeferredSeq[Component]] and
      (JsPath \ "CustomMetas").read[DeferredSeq[CustomMeta]] and
      (JsPath \ "Pages").read[DeferredSeq[Page]] and
      (JsPath \ "PageContents").read[DeferredSeq[PageContent]] and
      (JsPath \ "StructureGroups").read[DeferredSeq[StructureGroup]] and
      (JsPath \ "Templates").read[DeferredSeq[Template]]
    )(Publication.apply _)

  implicit lazy val deferredReads = Deferred.reads[Publication]

  implicit lazy val deferredListReads = DeferredSeq.reads[Publication]
}