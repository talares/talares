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
 * Items of this type hold a collection of {@link ComponentPresentation}'s.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public abstract class ComponentPresentationsHolder extends PublishedItem {

  private final DeferredList<org.talares.api.datatypes.items.ComponentPresentation, ComponentPresentation> componentPresentations;

  public ComponentPresentationsHolder(final org.talares.api.Talares api,
                                      final org.talares.api.datatypes.items.ComponentPresentationsHolder scalaComponentPresentationsHolder) {
    super(api, scalaComponentPresentationsHolder);
    this.componentPresentations = new DeferredList<>(
        api, scalaComponentPresentationsHolder.componentPresentations(), ComponentPresentation.FROM_SCALA
    );
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
}