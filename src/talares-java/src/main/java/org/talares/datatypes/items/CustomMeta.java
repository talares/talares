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

import org.joda.time.DateTime;
import org.talares.api.Talares;
import org.talares.api.exceptions.TalaresException;
import org.talares.datatypes.Deferred;
import play.libs.F;

import static org.talares.utils.Utils.await;

/**
 * A Java representation of a CustomMeta content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    &quot;__metadata&quot;: {
 *      &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)&quot;,
 *      &quot;type&quot;: &quot;Tridion.ContentDelivery.CustomMeta&quot;
 *    },
 *    &quot;DateValue&quot;: null,
 *    &quot;FloatValue&quot;: null,
 *    &quot;Id&quot;: 123,
 *    &quot;ItemId&quot;: 123,
 *    &quot;ItemType&quot;: 1,
 *    &quot;KeyName&quot;: &quot;key&quot;,
 *    &quot;PublicationId&quot;: 1,
 *    &quot;StringValue&quot;: &quot;A String&quot;,
 *    &quot;Component&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)/Component&quot;
 *      }
 *    },
 *    &quot;Page&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)/Page&quot;
 *      }
 *    },
 *    &quot;Keyword&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/CustomMetas(Id=12345)/Keyword&quot;
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class CustomMeta extends Item {

  public static final F.Function2<Talares, org.talares.api.datatypes.items.CustomMeta, CustomMeta> FROM_SCALA =
      new F.Function2<Talares, org.talares.api.datatypes.items.CustomMeta, CustomMeta>() {
        @Override
        public CustomMeta apply(final Talares app, final org.talares.api.datatypes.items.CustomMeta customMeta)
            {
          return new CustomMeta(app, customMeta);
        }
      };

  private final int itemId;
  private final int id;
  private final int itemType;
  private final String keyName;
  private final DateTime dateValue;
  private final Float floatValue;
  private final String stringValue;
  private final Deferred<org.talares.api.datatypes.items.Component, Component> component;
  private final Deferred<org.talares.api.datatypes.items.Page, Page> page;
  private final Deferred<org.talares.api.datatypes.items.Keyword, Keyword> keyword;

  public CustomMeta(final org.talares.api.Talares api,
                    final org.talares.api.datatypes.items.CustomMeta scalaCustomMeta) {
    super(api, scalaCustomMeta);
    this.itemId = scalaCustomMeta.itemId();
    this.id = scalaCustomMeta.id();
    this.itemType = scalaCustomMeta.itemType();
    this.keyName = scalaCustomMeta.keyName();
    this.dateValue = asJava(scalaCustomMeta.dateValue());
    this.floatValue = asJava(scalaCustomMeta.floatValue());
    this.stringValue = asJava(scalaCustomMeta.stringValue());
    this.component = new Deferred<>(api, scalaCustomMeta.component(), Component.FROM_SCALA);
    this.page = new Deferred<>(api, scalaCustomMeta.page(), Page.FROM_SCALA);
    this.keyword = new Deferred<>(api, scalaCustomMeta.keyword(), Keyword.FROM_SCALA);
  }

  public final int getItemId() {
    return itemId;
  }

  public final int getId() {
    return id;
  }

  public final int getItemType() {
    return itemType;
  }

  public final String getKeyName() {
    return keyName;
  }

  public final DateTime getDateValue() {
    return dateValue;
  }

  public final Float getFloatValue() {
    return floatValue;
  }

  public final String getStringValue() {
    return stringValue;
  }

  public final F.Promise<Component> getComponent() {
    return component.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a Component
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Component getComponent_(final long timeout) throws TalaresException {
    return await(getComponent(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a Component
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Component getComponent_() throws TalaresException {
    return getComponent_(getApi().settings().timeout());
  }

  public final F.Promise<Page> getPage() {
    return page.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a Page
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Page getPage_(final long timeout) throws TalaresException {
    return await(getPage(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a Page
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Page getPage_() throws TalaresException {
    return getPage_(getApi().settings().timeout());
  }

  public final F.Promise<Keyword> getKeyword() {
    return keyword.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a Keyword
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Keyword getKeyword_(final long timeout) throws TalaresException {
    return await(getKeyword(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a Keyword
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final Keyword getKeyword_() throws TalaresException {
    return getKeyword_(getApi().settings().timeout());
  }
}