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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
public abstract class CachesTest {

  protected static final Map<Object, Object> MOCK_CACHE = new HashMap<>();

  protected static final Object KEY_STUB = "keyStub";
  protected static final Object VALUE_STUB = "valueStub";

  protected static final NoCache MOCK_NO_CACHE = new NoCache();
  protected static final SimpleCache MOCK_SIMPLE_CACHE = new SimpleCache() {

    @Override
    public Object get(Object key) {
      return MOCK_CACHE.get(key);
    }

    @Override
    public void put(Object key, Object value) {
      MOCK_CACHE.put(key, value);
    }
  };

  protected static final AutoUpdateCache TEST_AUTO_UPDATE_CACHE = new AutoUpdateCache() {

    @Override
    public Object get(Object key) {
      return MOCK_CACHE.get(key);
    }

    @Override
    public void put(Object key, Object value) {
      MOCK_CACHE.put(key, value);
    }
  };
}