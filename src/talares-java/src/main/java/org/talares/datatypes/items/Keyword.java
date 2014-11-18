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

import org.talares.api.Talares;
import org.talares.api.exceptions.TalaresException;
import org.talares.datatypes.Deferred;
import org.talares.datatypes.DeferredList;
import play.libs.F;

import java.util.List;

import static org.talares.utils.Utils.await;

/**
 * A Java representation of a Keyword content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    &quot;__metadata&quot;: {
 *      &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(PublicationId=1,Id=2)&quot;,
 *      &quot;type&quot;: &quot;Tridion.ContentDelivery.Keyword&quot;
 *    },
 *    &quot;Id&quot;: 123,
 *    &quot;PublicationId&quot;: 1,
 *    &quot;TaxonomyId&quot;: 123,
 *    &quot;Title&quot;: &quot;A title&quot;,
 *    &quot;Description&quot;: &quot;A description&quot;,
 *    &quot;HasChildren&quot;: false,
 *    &quot;Abstract&quot;: false,
 *    &quot;Navigable&quot;: false,
 *    &quot;Key&quot;: &quot;A key&quot;,
 *    &quot;Depth&quot;: 0,
 *    &quot;ItemType&quot;: 1,
 *    &quot;TotalRelatedItems&quot;: 1,
 *    &quot;Components&quot;: {
 *      &quot;__deferred&quot;: {
 *          &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Components&quot;
 *      }
 *    },
 *    &quot;Pages&quot;: {
 *      &quot;__deferred&quot;: {
 *          &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Pages&quot;
 *      }
 *    },
 *    &quot;CustomMetas&quot;: {
 *      &quot;__deferred&quot;: {
 *          &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/CustomMetas&quot;
 *      }
 *    },
 *    &quot;Children&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Children&quot;
 *      }
 *    },
 *    &quot;Parent&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Keywords(Id=2,PublicationId=1)/Parent&quot;
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class Keyword extends Item {

  public static final F.Function2<Talares, org.talares.api.datatypes.items.Keyword, Keyword> FROM_SCALA =
      new F.Function2<Talares, org.talares.api.datatypes.items.Keyword, Keyword>() {
        @Override
        public Keyword apply(final Talares app, final org.talares.api.datatypes.items.Keyword keyword)
            {
          return new Keyword(app, keyword);
        }
      };

  private final int id;
  private final int taxonomyId;
  private final String title;
  private final String description;
  private final boolean hasChildren;
  private final boolean isAbstract;
  private final boolean navigable;
  private final String key;
  private final int depth;
  private final int itemType;
  private final Integer totalRelatedItems;
  private final DeferredList<org.talares.api.datatypes.items.Component, Component> components;
  private final DeferredList<org.talares.api.datatypes.items.Page, Page> pages;
  private final DeferredList<org.talares.api.datatypes.items.CustomMeta, CustomMeta> customMetas;
  private final DeferredList<org.talares.api.datatypes.items.Keyword, Keyword> children;
  private final Deferred<org.talares.api.datatypes.items.Keyword, Keyword> parent;

  public Keyword(final org.talares.api.Talares api, final org.talares.api.datatypes.items.Keyword scalaKeyword) {
    super(api, scalaKeyword);
    this.id = scalaKeyword.id();
    this.taxonomyId = scalaKeyword.taxonomyId();
    this.title = scalaKeyword.title();
    this.description = asJava(scalaKeyword.description());
    this.hasChildren = scalaKeyword.hasChildren();
    this.isAbstract = scalaKeyword.isAbstract();
    this.navigable = scalaKeyword.navigable();
    this.key = asJava(scalaKeyword.key());
    this.depth = scalaKeyword.depth();
    this.itemType = scalaKeyword.itemType();
    this.totalRelatedItems = asJava(scalaKeyword.totalRelatedItems());
    this.components = new DeferredList<>(api, scalaKeyword.components(), Component.FROM_SCALA);
    this.pages = new DeferredList<>(api, scalaKeyword.pages(), Page.FROM_SCALA);
    this.customMetas = new DeferredList<>(api, scalaKeyword.customMetas(), CustomMeta.FROM_SCALA);
    this.children = new DeferredList<>(api, scalaKeyword.children(), Keyword.FROM_SCALA);
    this.parent = new Deferred<>(api, scalaKeyword.parent(), Keyword.FROM_SCALA);
  }

  public final int getId() {
    return id;
  }

  public final int getTaxonomyId() {
    return taxonomyId;
  }

  public final String getTitle() {
    return title;
  }

  public final String getDescription() {
    return description;
  }

  public final boolean hasChildren() {
    return hasChildren;
  }

  public final boolean isAbstract() {
    return isAbstract;
  }

  public final boolean getNavigable() {
    return navigable;
  }

  public final String getKey() {
    return key;
  }

  public final int getDepth() {
    return depth;
  }

  public final int getItemType() {
    return itemType;
  }

  public final Integer getTotalRelatedItems() {
    return totalRelatedItems;
  }

  public final F.Promise<List<Component>> getComponents() {
    return components.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Component's
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<Component> getComponents_(final long timeout) throws TalaresException {
    return await(getComponents(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of Component's
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<Component> getComponents_() throws TalaresException {
    return getComponents_(getApi().settings().timeout());
  }

  public final F.Promise<List<Page>> getPages() {
    return pages.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Page's
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<Page> getPages_(final long timeout) throws TalaresException {
    return await(getPages(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of Page's
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<Page> getPages_() throws TalaresException {
    return getPages_(getApi().settings().timeout());
  }

  public final F.Promise<List<CustomMeta>> getCustomMetas() {
    return customMetas.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of CustomMeta's
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<CustomMeta> getCustomMetas_(final long timeout) throws TalaresException {
    return await(getCustomMetas(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of CustomMeta's
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<CustomMeta> getCustomMetas_() throws TalaresException {
    return getCustomMetas_(getApi().settings().timeout());
  }

  public final F.Promise<List<Keyword>> getChildren() {
    return children.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Keyword's
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<Keyword> getChildren_(final long timeout) throws TalaresException {
    return await(getChildren(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of Keyword's
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<Keyword> getChildren_() throws TalaresException {
    return getChildren_(getApi().settings().timeout());
  }

  public final F.Promise<Keyword> getParent() {
    return parent.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a Keyword
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Keyword getParent_(final long timeout) throws TalaresException {
    return await(getParent(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a Keyword
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Keyword getParent_() throws TalaresException {
    return getParent_(getApi().settings().timeout());
  }
}