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
import org.talares.datatypes.DeferredList;
import play.libs.F;

import java.util.List;

import static org.talares.utils.Utils.await;

/**
 * Item's of this type can be tagged using keywords or custom meta data.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public abstract class Taxonomised extends ComponentPresentationsHolder {

  private final DeferredList<org.talares.api.datatypes.items.Keyword, Keyword> keywords;
  private final DeferredList<org.talares.api.datatypes.items.CustomMeta, CustomMeta> customMetas;

  public Taxonomised(final Talares api, final org.talares.api.datatypes.items.Taxonomised scalaTaxonomised) {
    super(api, scalaTaxonomised);
    this.keywords = new DeferredList<>(api, scalaTaxonomised.keywords(), Keyword.FROM_SCALA);
    this.customMetas = new DeferredList<>(api, scalaTaxonomised.customMetas(), CustomMeta.FROM_SCALA);
  }

  public final F.Promise<List<Keyword>> getKeywords() {
    return keywords.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Keyword's
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<Keyword> getKeywords_(final long timeout) throws TalaresException {
    return await(getKeywords(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of Keyword's
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<Keyword> getKeywords_() throws TalaresException {
    return getKeywords_(getApi().settings().timeout());
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
}