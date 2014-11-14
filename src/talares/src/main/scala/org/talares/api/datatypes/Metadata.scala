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
package org.talares.api.datatypes

import play.api.libs.functional.syntax._
import play.api.libs.json.JsPath

/**
 * A Scala representation of a &#95;&#95;metadata field present in all Json representations of specific
 * [[org.talares.api.datatypes.items.Item]]'s.
 *
 * Example Json:
 * {{{
 *   __metadata": {
 *     "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc",
 *     "type": "Tridion.ContentDelivery.Item"
 *   }
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class Metadata(uri: String, `type`: String)

object Metadata {

  implicit object MetadataJsonReadable extends JsonReadable[Metadata] {

    override implicit val reads = Metadata.reads
  }

  implicit lazy val reads = (
    (JsPath \ "uri").read[String]
      and (JsPath \ "type").read[String]
    )(Metadata.apply _)
}