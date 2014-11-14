package org.talares.api.cache.mock

import org.talares.api.cache.{AutoUpdateCache, NoCache, SimpleCache}

import scala.collection.mutable

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
trait MockCaches {

  val mockCache: mutable.Map[Any, Any] = mutable.Map()

  val get = (key: Any) => mockCache.get(key)
  val put = (key: Any, value: Any) => {
    val x = mockCache.put(key, value)
  }

  val mockNoCache = NoCache()
  val mockSimpleCache = SimpleCache(get, put)
  val mockAutoUpdateCache = AutoUpdateCache(get, put)
}