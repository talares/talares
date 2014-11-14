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
package org.talares.datatypes.items;

import org.talares.api.exceptions.TalaresException;
import org.talares.datatypes.DeferredList;
import play.libs.F;

import java.util.List;

import static org.talares.utils.Utils.await;

/**
 * A Java representation of a Publication content type.
 * <p/>
 * Example Json:
 * <pre>
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
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class Publication extends Item {

  private final int id;
  private final String key;
  private final String multimediaPath;
  private final String multimediaUrl;
  private final String publicationPath;
  private final String publicationUrl;
  private final String title;
  private final DeferredList<org.talares.api.datatypes.items.Schema, Schema> schemas;
  private final DeferredList<org.talares.api.datatypes.items.ComponentPresentation, ComponentPresentation>
      componentPresentations;
  private final DeferredList<org.talares.api.datatypes.items.Keyword, Keyword> keywords;
  private final DeferredList<org.talares.api.datatypes.items.Binary, Binary> binaries;
  private final DeferredList<org.talares.api.datatypes.items.BinaryVariant, BinaryVariant> binaryVariants;
  private final DeferredList<org.talares.api.datatypes.items.Component, Component> components;
  private final DeferredList<org.talares.api.datatypes.items.CustomMeta, CustomMeta> customMetas;
  private final DeferredList<org.talares.api.datatypes.items.Page, Page> pages;
  private final DeferredList<org.talares.api.datatypes.items.PageContent, PageContent> pageContents;
  private final DeferredList<org.talares.api.datatypes.items.StructureGroup, StructureGroup> structureGroups;
  private final DeferredList<org.talares.api.datatypes.items.Template, Template> templates;

  public Publication(final org.talares.api.Talares api,
                     final org.talares.api.datatypes.items.Publication scalaPublication) {
    super(api, scalaPublication);
    this.id = scalaPublication.id();
    this.key = asJava(scalaPublication.key());
    this.multimediaPath = asJava(scalaPublication.multimediaPath());
    this.multimediaUrl = asJava(scalaPublication.multimediaUrl());
    this.publicationPath = asJava(scalaPublication.publicationPath());
    this.publicationUrl = asJava(scalaPublication.publicationUrl());
    this.title = asJava(scalaPublication.title());
    this.schemas = new DeferredList<>(api, scalaPublication.schemas(), Schema.FROM_SCALA);
    this.componentPresentations = new DeferredList<>(
        api, scalaPublication.componentPresentations(), ComponentPresentation.FROM_SCALA
    );
    this.keywords = new DeferredList<>(api, scalaPublication.keywords(), Keyword.FROM_SCALA);
    this.binaries = new DeferredList<>(api, scalaPublication.binaries(), Binary.FROM_SCALA);
    this.binaryVariants = new DeferredList<>(api, scalaPublication.binaryVariants(), BinaryVariant.FROM_SCALA);
    this.components = new DeferredList<>(api, scalaPublication.components(), Component.FROM_SCALA);
    this.customMetas = new DeferredList<>(api, scalaPublication.customMetas(), CustomMeta.FROM_SCALA);
    this.pages = new DeferredList<>(api, scalaPublication.pages(), Page.FROM_SCALA);
    this.pageContents = new DeferredList<>(api, scalaPublication.pageContents(), PageContent.FROM_SCALA);
    this.structureGroups = new DeferredList<>(api, scalaPublication.structureGroups(), StructureGroup.FROM_SCALA);
    this.templates = new DeferredList<>(api, scalaPublication.templates(), Template.FROM_SCALA);
  }

  public final int getId() {
    return id;
  }

  public final String getKey() {
    return key;
  }

  public final String getMultimediaPath() {
    return multimediaPath;
  }

  public final String getMultimediaUrl() {
    return multimediaUrl;
  }

  public final String getPublicationPath() {
    return publicationPath;
  }

  public final String getPublicationUrl() {
    return publicationUrl;
  }

  public final String getTitle() {
    return title;
  }

  public final F.Promise<List<Schema>> getSchemas() {
    return schemas.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Schema's
   * @throws TalaresException
   */
  public final List<Schema> getSchemas_(final long timeout) throws TalaresException {
    return await(getSchemas(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of Schema's
   * @throws TalaresException
   */
  public final List<Schema> getSchemas_() throws TalaresException {
    return getSchemas_(getApi().settings().timeout());
  }

  public final F.Promise<List<ComponentPresentation>> getComponentPresentations() {
    return componentPresentations.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of ComponentPresentation's
   * @throws TalaresException
   */
  public final List<ComponentPresentation> getComponentPresentations_(final long timeout) throws TalaresException {
    return await(getComponentPresentations(), timeout);
  }


  /**
   * <b>BLOCKING</b>
   *
   * @return a List of ComponentPresentation's
   * @throws TalaresException
   */
  public final List<ComponentPresentation> getComponentPresentations_() throws TalaresException {
    return getComponentPresentations_(getApi().settings().timeout());
  }

  public final F.Promise<List<Keyword>> getKeywords() {
    return keywords.getValue();
  }


  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Keyword's
   * @throws TalaresException
   */
  public final List<Keyword> getKeywords_(final long timeout) throws TalaresException {
    return await(getKeywords(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of Keyword's
   * @throws TalaresException
   */
  public final List<Keyword> getKeywords_() throws TalaresException {
    return getKeywords_(getApi().settings().timeout());
  }

  public final F.Promise<List<Binary>> getBinaries() {
    return binaries.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Binary's
   * @throws TalaresException
   */
  public final List<Binary> getBinaries_(final long timeout) throws TalaresException {
    return await(getBinaries(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of Binary's
   * @throws TalaresException
   */
  public final List<Binary> getBinaries_() throws TalaresException {
    return getBinaries_(getApi().settings().timeout());
  }

  public final F.Promise<List<BinaryVariant>> getBinaryVariants() {
    return binaryVariants.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of BinaryVariant's
   * @throws TalaresException
   */
  public final List<BinaryVariant> getBinaryVariants_(final long timeout) throws TalaresException {
    return await(getBinaryVariants(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of BinaryVariant's
   * @throws TalaresException
   */
  public final List<BinaryVariant> getBinaryVariants_() throws TalaresException {
    return getBinaryVariants_(getApi().settings().timeout());
  }

  public final F.Promise<List<Component>> getComponents() {
    return components.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Component's
   * @throws TalaresException
   */
  public final List<Component> getComponents_(final long timeout) throws TalaresException {
    return await(getComponents(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of Component's
   * @throws TalaresException
   */
  public final List<Component> getComponents_() throws TalaresException {
    return getComponents_(getApi().settings().timeout());
  }

  public final F.Promise<List<CustomMeta>> getCustomMetas() {
    return customMetas.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of CustomMeta's
   * @throws TalaresException
   */
  public final List<CustomMeta> getCustomMetas_(final long timeout) throws TalaresException {
    return await(getCustomMetas(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of CustomMeta's
   * @throws TalaresException
   */
  public final List<CustomMeta> getCustomMetas_() throws TalaresException {
    return getCustomMetas_(getApi().settings().timeout());
  }

  public final F.Promise<List<Page>> getPages() {
    return pages.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Page's
   * @throws TalaresException
   */
  public final List<Page> getPages_(final long timeout) throws TalaresException {
    return await(getPages(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of Page's
   * @throws TalaresException
   */
  public final List<Page> getPages_() throws TalaresException {
    return getPages_(getApi().settings().timeout());
  }

  public final F.Promise<List<PageContent>> getPageContents() {
    return pageContents.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of PageContent's
   * @throws TalaresException
   */
  public final List<PageContent> getPageContents_(final long timeout) throws TalaresException {
    return await(getPageContents(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of PageContent's
   * @throws TalaresException
   */
  public final List<PageContent> getPageContents_() throws TalaresException {
    return getPageContents_(getApi().settings().timeout());
  }

  public final F.Promise<List<StructureGroup>> getStructureGroups() {
    return structureGroups.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of StructureGroup's
   * @throws TalaresException
   */
  public final List<StructureGroup> getStructureGroups_(final long timeout) throws TalaresException {
    return await(getStructureGroups(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of StructureGroup's
   * @throws TalaresException
   */
  public final List<StructureGroup> getStructureGroups_() throws TalaresException {
    return getStructureGroups_(getApi().settings().timeout());
  }

  public final F.Promise<List<Template>> getTemplates() {
    return templates.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Template's
   * @throws TalaresException
   */
  public final List<Template> getTemplates_(final long timeout) throws TalaresException {
    return await(getTemplates(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of Template's
   * @throws TalaresException
   */
  public final List<Template> getTemplates_() throws TalaresException {
    return getTemplates_(getApi().settings().timeout());
  }
}