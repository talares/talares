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
package org.talares.mock;

import org.talares.Talares;
import org.talares.cache.Cache;
import org.talares.cache.CacheHelpers;

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
public class MockTalares extends Talares {

  public MockTalares(final Cache cache) {
    super(cache);
  }

  public MockTalares() {
    super();
  }

  @Override
  protected org.talares.api.Talares getApi(final Cache cache) {
    org.talares.api.cache.Cache scalaCache = CacheHelpers.asScala(cache);
    return new org.talares.api.mock.MockTalares(scalaCache);
  }
}