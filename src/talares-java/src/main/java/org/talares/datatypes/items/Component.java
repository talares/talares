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
import play.libs.F;

import static org.talares.utils.Utils.await;

/**
 * A Java representation of a Component content type.
 * <p/>
 * Example Json:
 * <pre>
 *   {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)",
 *      "type": "Tridion.ContentDelivery.Component"
 *    },
 *    "Author": "User",
 *    "CreationDate": "/Date(1362655474000+60)/",
 *    "InitialPublishDate": "/Date(1374139562000+120)/",
 *    "Multimedia": false,
 *    "ItemId": 123,
 *    "LastPublishDate": "/Date(1374139562113+120)/",
 *    "MajorVersion": 1,
 *    "MinorVersion": 2,
 *    "ModificationDate": "/Date(1374139455000+120)/",
 *    "OwningPublication": 1,
 *    "PublicationId": 1,
 *    "SchemaId": 123,
 *    "Title": "component title",
 *    "Schema": {
 *      "__deferred": {
 *         "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/Schema"
 *      }
 *    },
 *    "ComponentPresentations": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/ComponentPresentations"
 *      }
 *    },
 *    "Keywords": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/Keywords"
 *      }
 *    },
 *    "CustomMetas": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/CustomMetas"
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class Component extends Taxonomised {

  public static final F.Function2<Talares, org.talares.api.datatypes.items.Component, Component> FROM_SCALA =
      new F.Function2<Talares, org.talares.api.datatypes.items.Component, Component>() {
        @Override
        public Component apply(final Talares app, final org.talares.api.datatypes.items.Component component) {
          return new Component(app, component);
        }
      };

  private final boolean multimedia;
  private final int schemaId;
  private final Deferred<org.talares.api.datatypes.items.Schema, Schema> schema;

  public Component(final org.talares.api.Talares api, final org.talares.api.datatypes.items.Component scalaComponent) {
    super(api, scalaComponent);
    this.multimedia = asJava(scalaComponent.multimedia());
    this.schemaId = scalaComponent.schemaId();
    this.schema = new Deferred<>(api, scalaComponent.schema(), Schema.FROM_SCALA);
  }

  public final boolean isMultimedia() {
    return multimedia;
  }

  public final int getSchemaId() {
    return schemaId;
  }

  public final Deferred<org.talares.api.datatypes.items.Schema, Schema> getSchema() {
    return schema;
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a Schema
   * @throws TalaresException
   */
  public final Schema getSchema_(long timeout) throws TalaresException {
    return await(getSchema().getValue(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a Schema
   * @throws TalaresException
   */
  public final Schema getSchema_() throws TalaresException {
    return getSchema_(getApi().settings().timeout());
  }
}