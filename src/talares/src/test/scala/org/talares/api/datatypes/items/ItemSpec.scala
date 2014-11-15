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

import org.specs2.mutable.Specification
import org.talares.api.datatypes.Metadata
import org.talares.api.datatypes.items.stubs.ItemStubs._
import play.api.libs.json.{JsSuccess, Json}

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
class ItemSpec extends Specification {

  sequential

  "unmarshall Binary" in {
    (Json.fromJson[Binary](binaryStub) match {
      case JsSuccess(binary, _) => binary.binaryId == 123
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall BinaryContent" in {
    (Json.fromJson[BinaryContent](binaryContentStub) match {
      case JsSuccess(binaryContent, _) => binaryContent.binaryId == 123
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall BinaryVariant" in {
    (Json.fromJson[BinaryVariant](binaryVariantStub) match {
      case JsSuccess(binaryVariant, _) => binaryVariant.binaryId == 123
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall Component" in {
    (Json.fromJson[Component](componentStub) match {
      case JsSuccess(component, _) => component.author == Some("User")
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall ComponentPresentation" in {
    (Json.fromJson[ComponentPresentation](componentPresentationStub) match {
      case JsSuccess(componentPresentation, _) => componentPresentation.templateId == 123
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall CustomMeta" in {
    (Json.fromJson[CustomMeta](customMetaStub) match {
      case JsSuccess(customMeta, _) =>
        customMeta.stringValue == Some("Test") &&
          customMeta.floatValue == None
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall Keyword" in {
    (Json.fromJson[Keyword](keywordStub) match {
      case JsSuccess(keyword, _) => !keyword.isAbstract
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall Metadata" in {
    (Json.fromJson[Metadata](metadataStub) match {
      case JsSuccess(metadata, _) =>
        metadata.uri == "http://127.0.0.1:8080/cd_webservice/odata.svc" &&
          metadata.`type` == "Tridion.ContentDelivery.SomeContentType"
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall Page" in {
    (Json.fromJson[Page](pageStub) match {
      case JsSuccess(page, _) => page.author == None
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall PageContent" in {
    (Json.fromJson[PageContent](pageContentStub) match {
      case JsSuccess(pageContent, _) => pageContent.charset == Some("UTF8")
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall Publication" in {
    (Json.fromJson[Publication](publicationStub) match {
      case JsSuccess(publication, _) => publication.id == 1
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall Schema" in {
    (Json.fromJson[Schema](schemaStub) match {
      case JsSuccess(schema, _) => schema.schemaId == 123
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall StructureGroup" in {
    (Json.fromJson[StructureGroup](structureGroupStub) match {
      case JsSuccess(structureGroup, _) => structureGroup.depth == 0
      case _ => false
    }) must beEqualTo(true)
  }

  "unmarshall Template" in {
    (Json.fromJson[Template](templateStub) match {
      case JsSuccess(template, _) => template.author == Some("User")
      case _ => false
    }) must beEqualTo(true)
  }
}