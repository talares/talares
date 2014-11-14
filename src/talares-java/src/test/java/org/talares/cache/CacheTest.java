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

import static org.junit.Assert.assertTrue;

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
public class CacheTest extends CachesTest {

  @Test(expected = UnsupportedOperationException.class)
  public void testNoCacheGet() {
    MOCK_NO_CACHE.get(KEY_STUB);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNoCachePut() {
    MOCK_NO_CACHE.put(KEY_STUB, VALUE_STUB);
  }

  @Test
  public void testSimpleCacheGetAndPut() throws Exception {
    MOCK_SIMPLE_CACHE.put(KEY_STUB, VALUE_STUB);
    assertTrue(MOCK_SIMPLE_CACHE.get(KEY_STUB).equals(VALUE_STUB));
  }

  @Test
  public void testAutoUpdateCacheGetAndPut() throws Exception {
    TEST_AUTO_UPDATE_CACHE.put(KEY_STUB, VALUE_STUB);
    assertTrue(TEST_AUTO_UPDATE_CACHE.get(KEY_STUB).equals(VALUE_STUB));
  }
}