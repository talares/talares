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
 * A Java representation of a Binary content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    &quot;__metadata&quot;: {
 *      &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Binaries(BinaryId=123,PublicationId=1)&quot;,
 *      &quot;type&quot;: &quot;Tridion.ContentDelivery.Binary&quot;
 *    },
 *    &quot;BinaryId&quot;: 123,
 *    &quot;PublicationId&quot;: 1,
 *    &quot;Type&quot;: &quot;application/vnd.ms-fontobject&quot;,
 *    &quot;BinaryVariants&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Binaries(BinaryId=123,PublicationId=1)/BinaryVariants&quot;
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class Binary extends Item {

  public static final F.Function2<Talares, org.talares.api.datatypes.items.Binary, Binary> FROM_SCALA =
      new F.Function2<Talares, org.talares.api.datatypes.items.Binary, Binary>() {
        @Override
        public Binary apply(final Talares app, org.talares.api.datatypes.items.Binary binary) {
          return new Binary(app, binary);
        }
      };

  private final int binaryId;
  private final String type;
  private final DeferredList<org.talares.api.datatypes.items.BinaryVariant, BinaryVariant> binaryVariants;

  public Binary(final org.talares.api.Talares api, final org.talares.api.datatypes.items.Binary scalaBinary) {
    super(api, scalaBinary);
    this.binaryId = scalaBinary.binaryId();
    this.type = scalaBinary.type();
    this.binaryVariants = new DeferredList<>(
        api, scalaBinary.binaryVariants(), BinaryVariant.FROM_SCALA
    );
  }

  public final int getBinaryId() {
    return binaryId;
  }

  public final String getType() {
    return type;
  }

  public final F.Promise<List<BinaryVariant>> getBinaryVariants() {
    return binaryVariants.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a BinaryVariant
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<BinaryVariant> getBinaryVariants_(long timeout) throws TalaresException {
    return await(getBinaryVariants(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a BinaryVariant
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<BinaryVariant> getBinaryVariants_() throws TalaresException {
    return getBinaryVariants_(getApi().settings().timeout());
  }
}