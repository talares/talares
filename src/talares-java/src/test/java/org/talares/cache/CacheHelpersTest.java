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
package org.talares.cache;

import org.junit.Test;
import org.talares.api.cache.AutoUpdateCache;
import org.talares.api.cache.NoCache;
import org.talares.api.cache.SimpleCache;

import static org.junit.Assert.assertTrue;

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
public class CacheHelpersTest extends CachesTest {

  @Test
  public void testNoCacheAsScala() {
    assertTrue(CacheHelpers.asScala(MOCK_NO_CACHE) instanceof NoCache);
  }

  @Test
  public void testSimpleCacheAsScala() {
    assertTrue(CacheHelpers.asScala(MOCK_SIMPLE_CACHE) instanceof SimpleCache);
  }

  @Test
  public void testAutoUpdateCacheAsScala() {
    assertTrue(CacheHelpers.asScala(TEST_AUTO_UPDATE_CACHE) instanceof AutoUpdateCache);
  }
}