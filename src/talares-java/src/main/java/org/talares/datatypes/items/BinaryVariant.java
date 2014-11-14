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
 * A Java representation of a BinaryVariant content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/BinaryVariants(BinaryId=123,PublicationId=1,VariantId='default')",
 *      "type": "Tridion.ContentDelivery.BinaryVariant"
 *    },
 *    "URLPath": "/dir/image.jpg",
 *    "BinaryId": 123,
 *    "Description": null,
 *    "IsComponent": true,
 *    "Path": "/dir/image.jpg",
 *    "PublicationId": 1,
 *    "Type": "image/jpeg",
 *    "VariantId": "default",
 *    "Binary": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/BinaryVariants(BinaryId=123,PublicationId=1,VariantId='default')/Binary"
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class BinaryVariant extends Item {

  public static final F.Function2<Talares, org.talares.api.datatypes.items.BinaryVariant, BinaryVariant> FROM_SCALA =
      new F.Function2<Talares, org.talares.api.datatypes.items.BinaryVariant, BinaryVariant>() {
        @Override
        public BinaryVariant apply(final Talares app, org.talares.api.datatypes.items.BinaryVariant binaryVariant) {
          return new BinaryVariant(app, binaryVariant);
        }
      };

  private final int binaryId;
  private final String type;
  private final String urlPath;
  private final boolean component;
  private final String path;
  private final String variantId;
  private final String description;
  private final Deferred<org.talares.api.datatypes.items.Binary ,Binary> binary;

  public BinaryVariant(final org.talares.api.Talares api, final org.talares.api.datatypes.items.BinaryVariant scalaBinaryVariant) {
    super(api, scalaBinaryVariant);
    this.binaryId = scalaBinaryVariant.binaryId();
    this.type = scalaBinaryVariant.type();
    this.urlPath = scalaBinaryVariant.URLPath();
    this.component = scalaBinaryVariant.isComponent();
    this.path = scalaBinaryVariant.path();
    this.variantId = scalaBinaryVariant.variantId();
    this.description = asJava(scalaBinaryVariant.description());
    this.binary = new Deferred<>(api, scalaBinaryVariant.binary(), Binary.FROM_SCALA);
  }

  public final int getBinaryId() {
    return binaryId;
  }

  public final String getType() {
    return type;
  }

  public final String getUrlPath() {
    return urlPath;
  }

  public final boolean isComponent() {
    return component;
  }

  public final String getPath() {
    return path;
  }

  public final String getVariantId() {
    return variantId;
  }

  public final String getDescription() {
    return description;
  }

  public final Deferred<org.talares.api.datatypes.items.Binary, Binary> getBinary() {
    return binary;
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a Binary
   * @throws TalaresException
   */
  public final Binary getBinary_(long timeout) throws TalaresException {
    return await(getBinary().getValue(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a Binary
   * @throws TalaresException
   */
  public final Binary getBinary_() throws TalaresException {
    return getBinary_(getApi().settings().timeout());
  }
}