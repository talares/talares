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
package org.talares;

import org.talares.api.exceptions.TalaresException;
import org.talares.cache.Cache;
import org.talares.cache.CacheHelpers;
import org.talares.cache.NoCache;
import org.talares.datatypes.items.Binary;
import org.talares.datatypes.items.BinaryContent;
import org.talares.datatypes.items.BinaryVariant;
import org.talares.datatypes.items.Component;
import org.talares.datatypes.items.ComponentPresentation;
import org.talares.datatypes.items.CustomMeta;
import org.talares.datatypes.items.Item;
import org.talares.datatypes.items.Keyword;
import org.talares.datatypes.items.Page;
import org.talares.datatypes.items.PageContent;
import org.talares.datatypes.items.Publication;
import org.talares.datatypes.items.Schema;
import org.talares.datatypes.items.StructureGroup;
import org.talares.datatypes.items.Template;
import play.libs.F;
import play.libs.Scala;
import scala.collection.Seq;
import scala.concurrent.Future;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.talares.utils.Utils.await;

/**
 * Class holding the main client facing API.
 * <p>
 * An instance of this class should be created to gain access to the API.
 * A cache strategy should be decided upon and a corresponding instance of a subclass of {@link org.talares.cache.Cache}
 * should be provided to the constructor of this class.
 * <p>
 * Example:
 * <pre>
 * import org.talares.Talares;
 * import org.talares.datatypes.items.Page;
 * import play.libs.F;
 *
 * Talares talares = new Talares(cache);
 * F.Promise&amp;lt;Page&amp;gt; pagePromise = talares.getPage(1, 2);
 * </pre>
 *
 * @author Dennis Vis
 * @see Cache
 * @since 0.1.0
 */
public class Talares {

  private final org.talares.api.Talares api;
  private final long defaultTimeout;

  public Talares() {
    this.api = getApi(new NoCache());
    this.defaultTimeout = api.settings().timeout();
  }

  public Talares(final Cache cache) {
    this.api = getApi(cache);
    this.defaultTimeout = api.settings().timeout();
  }

  protected org.talares.api.Talares getApi(final Cache cache) {
    org.talares.api.cache.Cache scalaCache = CacheHelpers.asScala(cache);
    return new org.talares.api.Talares(scalaCache);
  }

  /**
   * Terminates the library which implies shutting down the actor system.
   * Should be used when the API will no longer be accesses and only then.
   */
  public final void terminate() {
    api.terminate();
  }

  private <A extends org.talares.api.datatypes.items.Item, B extends Item> F.Promise<B> get(final Future<A> itemFuture,
                                                                                            final Class<B> clazz) {

    final Class<org.talares.api.Talares> apiClass = org.talares.api.Talares.class;
    final F.Promise<A> itemPromise = F.Promise.wrap(itemFuture);

    return itemPromise.map(new F.Function<A, B>() {

      @Override
      public B apply(final A item)
          throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        final Constructor<B> constructor = clazz.getConstructor(apiClass, item.getClass());
        return constructor.newInstance(api, item);
      }
    });
  }

  private <A extends org.talares.api.datatypes.items.Item, B extends Item> F.Promise<List<B>> getL(final Future<Seq<A>> itemFuture,
                                                                                                   final Class<B> clazz) {

    final Class<org.talares.api.Talares> apiClass = org.talares.api.Talares.class;
    final F.Promise<Seq<A>> itemSeqPromise = F.Promise.wrap(itemFuture);

    return itemSeqPromise.map(new F.Function<Seq<A>, List<B>>() {

      @Override
      public List<B> apply(final Seq<A> itemSeq)
          throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        final List<B> items = new ArrayList<>();

        for (final A item : Scala.asJava(itemSeq)) {

          final Constructor<B> constructor = clazz.getConstructor(apiClass, item.getClass());
          items.add(constructor.newInstance(api, item));
        }

        return items;
      }
    });
  }

  /**
   * Get a {@link Binary} by it's publication ID and binary ID.
   *
   * @param publicationId the ID of the publication the desired {@link Binary} belongs to
   * @param binaryId      the binary ID of the desired {@link Binary}
   * @return a {@link play.libs.F.Promise} of a {@link Binary}
   */
  public final F.Promise<Binary> getBinary(final int publicationId, final int binaryId) {
    return get(api.getBinary(publicationId, binaryId), Binary.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Binary} by it's publication ID and binary ID.
   *
   * @param publicationId the ID of the publication the desired {@link Binary} belongs to
   * @param binaryId      the ID of the desired {@link Binary}
   * @param timeout       the timeout in milliseconds within which the {@link Binary} should be present
   * @return a {@link Binary}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Binary getBinary_(final int publicationId,
                                 final int binaryId,
                                 final long timeout) throws TalaresException {
    return await(getBinary(publicationId, binaryId), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Binary} by it's publication ID and binary ID.
   *
   * @param publicationId the ID of the publication the desired {@link Binary} belongs to
   * @param binaryId      the ID of the desired {@link Binary}
   * @return a {@link Binary}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Binary getBinary_(final int publicationId, final int binaryId) throws TalaresException {
    return getBinary_(publicationId, binaryId, defaultTimeout);
  }

  /**
   * Get a {@link BinaryContent} by it's publication ID, binary ID and variant ID.
   *
   * @param publicationId the ID of the publication the desired {@link BinaryContent} belongs to
   * @param binaryId      the ID of the desired {@link BinaryContent}
   * @param variantId     the variant ID of the desired {@link BinaryContent}
   * @return a {@link play.libs.F.Promise} of a {@link BinaryContent}
   */
  public final F.Promise<BinaryContent> getBinaryContent(final int publicationId,
                                                         final int binaryId,
                                                         final String variantId) {
    return get(api.getBinaryContent(publicationId, binaryId, variantId), BinaryContent.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link BinaryContent} by it's publication ID and binary ID.
   *
   * @param publicationId the ID of the publication the desired {@link BinaryContent} belongs to
   * @param binaryId      the ID of the desired {@link BinaryContent}
   * @param variantId     the variant ID of the desired {@link BinaryContent}
   * @param timeout       the timeout in milliseconds within which the {@link BinaryContent} should be present
   * @return a {@link BinaryContent}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final BinaryContent getBinaryContent_(final int publicationId,
                                               final int binaryId,
                                               final String variantId,
                                               final long timeout) throws TalaresException {
    return await(getBinaryContent(publicationId, binaryId, variantId), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link BinaryContent} by it's publication ID, binary ID and variant ID.
   *
   * @param publicationId the ID of the publication the desired {@link BinaryContent} belongs to
   * @param binaryId      the ID of the desired {@link BinaryContent}
   * @param variantId     the variant ID of the desired {@link BinaryContent}
   * @return a {@link BinaryContent}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final BinaryContent getBinaryContent_(final int publicationId,
                                               final int binaryId,
                                               final String variantId) throws TalaresException {
    return getBinaryContent_(publicationId, binaryId, variantId, defaultTimeout);
  }

  /**
   * Get a {@link BinaryVariant} by it's publication ID and binary ID.
   *
   * @param publicationId the ID of the publication the desired {@link BinaryVariant} belongs to
   * @param binaryId      the ID of the desired {@link BinaryVariant}
   * @return a {@link play.libs.F.Promise} of a {@link BinaryVariant}
   */
  public final F.Promise<BinaryVariant> getBinaryVariant(final int publicationId, final int binaryId) {
    return get(api.getBinaryVariant(publicationId, binaryId), BinaryVariant.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link BinaryVariant} by it's publication ID and binary ID.
   *
   * @param publicationId the ID of the publication the desired {@link BinaryVariant} belongs to
   * @param binaryId      the ID of the desired {@link BinaryVariant}
   * @param timeout       the timeout in milliseconds within which the {@link BinaryVariant} should be present
   * @return a {@link BinaryVariant}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final BinaryVariant getBinaryVariant_(final int publicationId,
                                               final int binaryId,
                                               final long timeout) throws TalaresException {
    return await(getBinaryVariant(publicationId, binaryId), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link BinaryVariant} by it's publication ID and binary ID.
   *
   * @param publicationId the ID of the publication the desired {@link BinaryVariant} belongs to
   * @param binaryId      the ID of the desired {@link BinaryVariant}
   * @return a {@link BinaryVariant}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final BinaryVariant getBinaryVariant_(final int publicationId, final int binaryId) throws TalaresException {
    return getBinaryVariant_(publicationId, binaryId, defaultTimeout);
  }

  /**
   * Get a {@link Component} by it's publication ID and item ID.
   *
   * @param publicationId the ID of the publication the desired {@link Component} belongs to
   * @param itemId        the item ID of the desired {@link Component}
   * @return a {@link play.libs.F.Promise} of a {@link Component}
   */
  public final F.Promise<Component> getComponent(final int publicationId, final int itemId) {
    return get(api.getComponent(publicationId, itemId), Component.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Component} by it's publication ID and item ID.
   *
   * @param publicationId the ID of the publication the desired {@link Component} belongs to
   * @param itemId        the item ID of the desired {@link Component}
   * @param timeout       the timeout in milliseconds within which the {@link Component} should be present
   * @return a {@link Component}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Component getComponent_(final int publicationId,
                                       final int itemId,
                                       final long timeout) throws TalaresException {
    return await(getComponent(publicationId, itemId), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Component} by it's publication ID and item ID.
   *
   * @param publicationId the ID of the publication the desired {@link Component} belongs to
   * @param itemId        the item ID of the desired {@link Component}
   * @return a {@link Component}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Component getComponent_(final int publicationId, final int itemId) throws TalaresException {
    return getComponent_(publicationId, itemId, defaultTimeout);
  }

  /**
   * Get a {@link ComponentPresentation} by it's publication ID, component ID and templateId ID.
   *
   * @param publicationId the ID of the publication the desired {@link ComponentPresentation} belongs to
   * @param componentId   the component ID of the desired {@link ComponentPresentation}
   * @param templateId    the template ID of the desired {@link ComponentPresentation}
   * @return a {@link play.libs.F.Promise} of a {@link ComponentPresentation}
   */
  public final F.Promise<ComponentPresentation> getComponentPresentation(final int publicationId,
                                                                         final int componentId,
                                                                         final int templateId) {
    return get(api.getComponentPresentation(publicationId, componentId, templateId), ComponentPresentation.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link ComponentPresentation} by it's publication ID, component ID and templateId ID.
   *
   * @param publicationId the ID of the publication the desired {@link ComponentPresentation} belongs to
   * @param componentId   the component ID of the desired {@link ComponentPresentation}
   * @param templateId    the template ID of the desired {@link ComponentPresentation}
   * @param timeout       the timeout in milliseconds within which the {@link ComponentPresentation} should be present
   * @return a {@link ComponentPresentation}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final ComponentPresentation getComponentPresentation_(final int publicationId,
                                                               final int componentId,
                                                               final int templateId,
                                                               final long timeout) throws TalaresException {
    return await(getComponentPresentation(publicationId, componentId, templateId), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link ComponentPresentation} by it's publication ID, component ID and templateId ID.
   *
   * @param publicationId the ID of the publication the desired {@link ComponentPresentation} belongs to
   * @param componentId   the component ID of the desired {@link ComponentPresentation}
   * @param templateId    the template ID of the desired {@link ComponentPresentation}
   * @return a {@link ComponentPresentation}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final ComponentPresentation getComponentPresentation_(final int publicationId,
                                                               final int componentId,
                                                               final int templateId) throws TalaresException {
    return getComponentPresentation_(publicationId, componentId, templateId, defaultTimeout);
  }

  /**
   * Get a {@link CustomMeta} by it's ID.
   *
   * @param id the ID of the desired {@link CustomMeta}
   * @return a {@link play.libs.F.Promise} of a {@link CustomMeta}
   */
  public final F.Promise<CustomMeta> getCustomMeta(final int id) {
    return get(api.getCustomMeta(id), CustomMeta.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link CustomMeta} by it's ID.
   *
   * @param id      the ID of the desired {@link CustomMeta}
   * @param timeout the timeout in milliseconds within which the {@link CustomMeta} should be present
   * @return a {@link CustomMeta}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final CustomMeta getCustomMeta_(final int id, final long timeout) throws TalaresException {
    return await(getCustomMeta(id), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link CustomMeta} by it's ID.
   *
   * @param id the ID of the desired {@link CustomMeta}
   * @return a {@link CustomMeta}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final CustomMeta getCustomMeta_(final int id) throws TalaresException {
    return getCustomMeta_(id, defaultTimeout);
  }

  /**
   * Get a {@link Keyword} by it's ID, publication ID and taxonomy ID.
   *
   * @param publicationId the ID of the publication the desired {@link Keyword} belongs to
   * @param id            the ID of the desired {@link Keyword}
   * @param taxonomyId    the taxonomy ID of the desired {@link Keyword}
   * @return a {@link play.libs.F.Promise} of a {@link Keyword}
   */
  public final F.Promise<Keyword> getKeyword(final int publicationId, final int id, final int taxonomyId) {
    return get(api.getKeyword(publicationId, id, taxonomyId), Keyword.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Keyword} by it's ID, publication ID and taxonomy ID.
   *
   * @param publicationId the ID of the publication the desired {@link Keyword} belongs to
   * @param id            the ID of the desired {@link Keyword}
   * @param taxonomyId    the taxonomy ID of the desired {@link Keyword}
   * @param timeout       the timeout in milliseconds within which the {@link Keyword} should be present
   * @return a {@link Keyword}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Keyword getKeyword_(final int publicationId,
                                   final int id,
                                   final int taxonomyId,
                                   final long timeout) throws TalaresException {
    return await(getKeyword(publicationId, id, taxonomyId), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Keyword} by it's ID, publication ID and taxonomy ID.
   *
   * @param publicationId the ID of the publication the desired {@link Keyword} belongs to
   * @param id            the ID of the desired {@link Keyword}
   * @param taxonomyId    the taxonomy ID of the desired {@link Keyword}
   * @return a {@link Keyword}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Keyword getKeyword_(final int publicationId,
                                   final int id,
                                   final int taxonomyId) throws TalaresException {
    return getKeyword_(publicationId, id, taxonomyId, defaultTimeout);
  }

  /**
   * Get a {@link Page} by it's publication ID and item ID.
   *
   * @param publicationId the ID of the publication the desired {@link Page} belongs to
   * @param itemId        the item ID of the desired {@link Page}
   * @return a {@link play.libs.F.Promise} of a {@link Page}
   */
  public final F.Promise<Page> getPage(final int publicationId, final int itemId) {
    return get(api.getPage(publicationId, itemId), Page.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Page} by it's publication ID and item ID.
   *
   * @param publicationId the ID of the publication the desired {@link Page} belongs to
   * @param itemId        the item ID of the desired {@link Page}
   * @param timeout       the timeout in milliseconds within which the {@link Page} should be present
   * @return a {@link Page}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Page getPage_(final int publicationId, final int itemId, final long timeout) throws TalaresException {
    return await(getPage(publicationId, itemId), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Page} by it's publication ID and item ID.
   *
   * @param publicationId the ID of the publication the desired {@link Page} belongs to
   * @param itemId        the item ID of the desired {@link Page}
   * @return a {@link Page}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Page getPage_(final int publicationId, final int itemId) throws TalaresException {
    return getPage_(publicationId, itemId, defaultTimeout);
  }

  /**
   * Get a {@link Page} by it's URL.
   *
   * @param url the URL of the page within Tridion
   * @return a {@link play.libs.F.Promise} of a {@link List} of {@link Page}'s
   */
  public final F.Promise<List<Page>> getPage(final String url) {
    return getL(api.getPage(url), Page.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Page} by it's URL.
   *
   * @param url     the URL of the page within Tridion
   * @param timeout the timeout in milliseconds within which the {@link Page} should be present
   * @return a {@link Page}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<Page> getPage_(final String url, final long timeout) throws TalaresException {
    return await(getPage(url), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Page} by it's URL.
   *
   * @param url the URL of the page within Tridion
   * @return a {@link Page}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<Page> getPage_(final String url) throws TalaresException {
    return getPage_(url, api.settings().timeout());
  }

  /**
   * Get a {@link PageContent} by it's publication ID and page ID.
   *
   * @param publicationId the ID of the publication the desired {@link PageContent} belongs to
   * @param pageId        the page ID of the desired {@link PageContent}
   * @return a {@link play.libs.F.Promise} of a {@link PageContent}
   */
  public final F.Promise<PageContent> getPageContent(final int publicationId,
                                                     final int pageId) {
    return get(api.getPageContent(publicationId, pageId), PageContent.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link PageContent} by it's publication ID and page ID.
   *
   * @param publicationId the ID of the publication the desired {@link PageContent} belongs to
   * @param pageId        the page ID of the desired {@link PageContent}
   * @param timeout       the timeout in milliseconds within which the {@link PageContent} should be present
   * @return a {@link PageContent}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final PageContent getPageContent_(final int publicationId,
                                           final int pageId,
                                           final long timeout) throws TalaresException {
    return await(getPageContent(publicationId, pageId), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link PageContent} by it's publication ID and page ID.
   *
   * @param publicationId the ID of the publication the desired {@link PageContent} belongs to
   * @param pageId        the page ID of the desired {@link PageContent}
   * @return a {@link PageContent}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final PageContent getPageContent_(final int publicationId,
                                           final int pageId) throws TalaresException {
    return getPageContent_(publicationId, pageId, defaultTimeout);
  }

  /**
   * Get a {@link Publication} by it's ID.
   *
   * @param id the ID of the desired {@link Publication}
   * @return a {@link play.libs.F.Promise} of a {@link Publication}
   */
  public final F.Promise<Publication> getPublication(final int id) {
    return get(api.getPublication(id), Publication.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Publication} by it's ID.
   *
   * @param id      the ID of the desired {@link Publication}
   * @param timeout the timeout in milliseconds within which the {@link Publication} should be present
   * @return a {@link Publication}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Publication getPublication_(final int id, final long timeout) throws TalaresException {
    return await(getPublication(id), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Publication} by it's ID.
   *
   * @param id the ID of the desired {@link Publication}
   * @return a {@link Publication}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Publication getPublication_(final int id) throws TalaresException {
    return getPublication_(id, defaultTimeout);
  }

  /**
   * Get a {@link Schema} by it's publication ID and schema ID.
   *
   * @param publicationId the ID of the publication the desired {@link Schema} belongs to
   * @param schemaId      the schema ID of the desired {@link Schema}
   * @return a {@link play.libs.F.Promise} of a {@link Schema}
   */
  public final F.Promise<Schema> getSchema(final int publicationId, final int schemaId) {
    return get(api.getSchema(publicationId, schemaId), Schema.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Schema} by it's publication ID and schema ID.
   *
   * @param publicationId the ID of the publication the desired {@link Schema} belongs to
   * @param schemaId      the schema ID of the desired {@link Schema}
   * @param timeout       the timeout in milliseconds within which the {@link Schema} should be present
   * @return a {@link Schema}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Schema getSchema_(final int publicationId,
                                 final int schemaId,
                                 final long timeout) throws TalaresException {
    return await(getSchema(publicationId, schemaId), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Schema} by it's publication ID and schema ID.
   *
   * @param publicationId the ID of the publication the desired {@link Schema} belongs to
   * @param schemaId      the schema ID of the desired {@link Schema}
   * @return a {@link Schema}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Schema getSchema_(final int publicationId, final int schemaId) throws TalaresException {
    return getSchema_(publicationId, schemaId, defaultTimeout);
  }

  /**
   * Get a {@link StructureGroup} by it's ID and publication ID.
   *
   * @param publicationId the ID of the publication the desired {@link StructureGroup} belongs to
   * @param id            the ID of the desired {@link StructureGroup}
   * @return a {@link play.libs.F.Promise} of a {@link StructureGroup}
   */
  public final F.Promise<StructureGroup> getStructureGroup(final int publicationId,
                                                           final int id) {
    return get(api.getStructureGroup(publicationId, id), StructureGroup.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link StructureGroup} by it's ID and publication ID.
   *
   * @param publicationId the ID of the publication the desired {@link StructureGroup} belongs to
   * @param id            the ID of the desired {@link StructureGroup}
   * @param timeout       the timeout in milliseconds within which the {@link StructureGroup} should be present
   * @return a {@link StructureGroup}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final StructureGroup getStructureGroup_(final int publicationId,
                                                 final int id,
                                                 final long timeout) throws TalaresException {
    return await(getStructureGroup(publicationId, id), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link StructureGroup} by it's ID and publication ID.
   *
   * @param publicationId the ID of the publication the desired {@link StructureGroup} belongs to
   * @param id            the ID of the desired {@link StructureGroup}
   * @return a {@link StructureGroup}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final StructureGroup getStructureGroup_(final int publicationId, final int id) throws TalaresException {
    return getStructureGroup_(publicationId, id, defaultTimeout);
  }

  /**
   * Get a {@link Template} by it's publication ID and item ID.
   *
   * @param publicationId the ID of the publication the desired {@link Template} belongs to
   * @param itemId        the item ID of the desired {@link Template}
   * @return a {@link play.libs.F.Promise} of a {@link Template}
   */
  public final F.Promise<Template> getTemplate(final int publicationId, final int itemId) {
    return get(api.getTemplate(publicationId, itemId), Template.class);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Template} by it's publication ID and item ID.
   *
   * @param publicationId the ID of the publication the desired {@link Template} belongs to
   * @param itemId        the item ID of the desired {@link Template}
   * @param timeout       the timeout in milliseconds within which the {@link Template} should be present
   * @return a {@link Template}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Template getTemplate_(final int publicationId,
                                     final int itemId,
                                     final long timeout) throws TalaresException {
    return await(getTemplate(publicationId, itemId), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get a {@link Template} by it's publication ID and item ID.
   *
   * @param publicationId the ID of the publication the desired {@link Template} belongs to
   * @param itemId        the item ID of the desired {@link Template}
   * @return a {@link Template}
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Template getTemplate_(final int publicationId, final int itemId) throws TalaresException {
    return getTemplate_(publicationId, itemId, defaultTimeout);
  }
}