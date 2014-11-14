package org.talares.api.cache

import org.specs2.mutable.Specification
import org.talares.api.cache.mock.MockCaches

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
class CacheSpec extends Specification with MockCaches {

  val key = "keyStub"
  val value = "valueStub"

  "Caches" should {

    "get with no cache" in {
      mockNoCache.get(key) must throwA[UnsupportedOperationException]
    }

    "put with no cache" in {
      mockNoCache.put(key, value) must throwA[UnsupportedOperationException]
    }

    "get and put with simple cache" in {
      mockSimpleCache.put(key, value)
      mockSimpleCache.get(key) == Some(value)
    }

    "get and put with auto update cache" in {
      mockAutoUpdateCache.put(key, value)
      mockAutoUpdateCache.get(key) == Some(value)
    }
  }
}