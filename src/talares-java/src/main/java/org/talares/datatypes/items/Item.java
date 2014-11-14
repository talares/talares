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
import org.talares.datatypes.Metadata;
import scala.Option;

/**
 * The common class for all concrete content type's.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public abstract class Item {

  private final org.talares.api.Talares api;
  private final Metadata metadata;
  private final int publicationId;

  public Item(final org.talares.api.Talares scalaApi, final org.talares.api.datatypes.items.Item scalaItem) {
    this.api = scalaApi;
    this.metadata = new Metadata(scalaItem.metadata());
    this.publicationId = scalaItem.publicationId();
  }

  @SuppressWarnings("unchecked")
  protected final <A, B> B asJava(final Option<A> option) {
    if (option.isDefined()) {
      A a = option.get();
      if (a instanceof scala.Boolean) return (B) Boolean.valueOf(scala.Boolean.unbox(a));
      if (a instanceof scala.Float) return (B) new Float(scala.Float.unbox(a));
      if (a instanceof scala.Int) return (B) new Integer(scala.Int.unbox(a));
      return (B) a;
    }
    return null;
  }

  public final Talares getApi() {
    return api;
  }

  public final Metadata getMetadata() {
    return metadata;
  }

  public final int getPublicationId() {
    return publicationId;
  }
}