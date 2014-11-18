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
 * <p>
 * Example Json:
 * <pre>
 *   {
 *    &quot;__metadata&quot;: {
 *      &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)&quot;,
 *      &quot;type&quot;: &quot;Tridion.ContentDelivery.Component&quot;
 *    },
 *    &quot;Author&quot;: &quot;User&quot;,
 *    &quot;CreationDate&quot;: &quot;/Date(1362655474000+60)/&quot;,
 *    &quot;InitialPublishDate&quot;: &quot;/Date(1374139562000+120)/&quot;,
 *    &quot;Multimedia&quot;: false,
 *    &quot;ItemId&quot;: 123,
 *    &quot;LastPublishDate&quot;: &quot;/Date(1374139562113+120)/&quot;,
 *    &quot;MajorVersion&quot;: 1,
 *    &quot;MinorVersion&quot;: 2,
 *    &quot;ModificationDate&quot;: &quot;/Date(1374139455000+120)/&quot;,
 *    &quot;OwningPublication&quot;: 1,
 *    &quot;PublicationId&quot;: 1,
 *    &quot;SchemaId&quot;: 123,
 *    &quot;Title&quot;: &quot;component title&quot;,
 *    &quot;Schema&quot;: {
 *      &quot;__deferred&quot;: {
 *         &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/Schema&quot;
 *      }
 *    },
 *    &quot;ComponentPresentations&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/ComponentPresentations&quot;
 *      }
 *    },
 *    &quot;Keywords&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/Keywords&quot;
 *      }
 *    },
 *    &quot;CustomMetas&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Components(ItemId=123,PublicationId=1)/CustomMetas&quot;
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
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Schema getSchema_(long timeout) throws TalaresException {
    return await(getSchema().getValue(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a Schema
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Schema getSchema_() throws TalaresException {
    return getSchema_(getApi().settings().timeout());
  }
}