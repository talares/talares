package org.talares.cache;

import scala.Function1;
import scala.Function2;
import scala.Option;
import scala.runtime.AbstractFunction1;
import scala.runtime.AbstractFunction2;
import scala.runtime.BoxedUnit;

/**
 * Holds utility methods for converting a given {@link Cache} instance into it's Scala counterpart for use within the
 * API.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public abstract class CacheHelpers {

  /**
   * Transforms a given {@link Cache} instance into it's Scala counterpart for use within the API.
   *
   * @param cache an instance of {@link Cache}
   * @return an instance of {@link org.talares.api.cache.Cache}
   */
  public static org.talares.api.cache.Cache asScala(final Cache cache) {
    if (cache instanceof NoCache) {
      return new org.talares.api.cache.NoCache();
    } else if (cache instanceof AutoUpdateCache) {
      return new org.talares.api.cache.AutoUpdateCache(getFunc(cache), putFunc(cache));
    } else {
      return new org.talares.api.cache.SimpleCache(getFunc(cache), putFunc(cache));
    }
  }

  /**
   * Transforms a {@link Cache#get} method into a Scala {@link scala.Function1}.
   *
   * @param cache an instance of {@link Cache}
   * @return a {@link scala.Function1} the API can use to fetch items from the cache
   */
  private static Function1<Object, Option<Object>> getFunc(final Cache cache) {

    return new AbstractFunction1<Object, Option<Object>>() {

      @Override
      public Option<Object> apply(final Object key) {
        return Option.apply(cache.get(key));
      }
    };
  }

  /**
   * Transforms a {@link Cache#put} method into a Scala {@link scala.Function2}.
   *
   * @param cache an instance of {@link Cache}
   * @return a {@link scala.Function2} the API can use to store items in the cache
   */
  private static Function2<Object, Object, BoxedUnit> putFunc(final Cache cache) {

    return new AbstractFunction2<Object, Object, BoxedUnit>() {

      @Override
      public BoxedUnit apply(final Object key, final Object value) {
        cache.put(key, value);
        return BoxedUnit.UNIT;
      }
    };
  }
}