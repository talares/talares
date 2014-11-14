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
package org.talares.datatypes;

/**
 * A Java representation of a __metadata field present in all Json representations of specific
 * {@link org.talares.datatypes.items.Item}'s.
 * <p/>
 * Example Json:
 * <pre>
 * __metadata": {
 *   "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc",
 *   "type": "Tridion.ContentDelivery.Item"
 * }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class Metadata {

  private final String uri;
  private final String type;

  public Metadata(final org.talares.api.datatypes.Metadata scalaMetadata) {
    this.uri = scalaMetadata.uri();
    this.type = scalaMetadata.type();
  }

  public final String getUri() {
    return uri;
  }

  public final String getType() {
    return type;
  }
}