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
 * A Java representation of a Schema content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Schemas(SchemaId=123,PublicationId=1)",
 *      "type": "Tridion.ContentDelivery.Schema"
 *    },
 *    "PublicationId": 1,
 *    "SchemaId": 123,
 *    "Title": "A title",
 *    "Components": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Schemas(SchemaId=123,PublicationId=1)/Components"
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class Schema extends Item {

  public static final F.Function2<Talares, org.talares.api.datatypes.items.Schema, Schema> FROM_SCALA =
      new F.Function2<Talares, org.talares.api.datatypes.items.Schema, Schema>() {
        @Override
        public Schema apply(final Talares app, final org.talares.api.datatypes.items.Schema schema) {
          return new Schema(app, schema);
        }
      };

  private final int schemaId;
  private final String title;
  private final DeferredList<org.talares.api.datatypes.items.Component, Component> components;

  public Schema(final org.talares.api.Talares api, final org.talares.api.datatypes.items.Schema scalaSchema) {
    super(api, scalaSchema);
    this.schemaId = scalaSchema.schemaId();
    this.title = scalaSchema.title();
    this.components = new DeferredList<>(api, scalaSchema.components(), Component.FROM_SCALA);
  }

  public final int getSchemaId() {
    return schemaId;
  }

  public final String getTitle() {
    return title;
  }

  public final DeferredList<org.talares.api.datatypes.items.Component, Component> getComponents() {
    return components;
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Component's
   * @throws TalaresException
   */
  public final List<Component> getComponents_(long timeout) throws TalaresException {
    return await(getComponents().getValue(), timeout);
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
}