package org.talares.cache;

/**
 * Interface whose instances allow the library to make use of a given cache implementation. This leaves the user free
 * to supply a cache solution of his own choosing.
 * <p>
 * An instance of one of the implementing types should be provided at library instantiation. When no cache instance is
 * provided the {@link org.talares.cache.NoCache} strategy is assumed. When an instance of
 * {@link org.talares.cache.Cache} is provided, the {@link org.talares.cache.SimpleCache} strategy is assumed.
 *
 * @author Dennis Vis
 * @see org.talares.cache.NoCache
 * @see org.talares.cache.SimpleCache
 * @see org.talares.cache.AutoUpdateCache
 * @since 0.1.0
 */
public interface Cache {

  /**
   * Method with which objects can be retrieved from cache with a given key.
   *
   * @param key the key to use
   * @return the cached item
   */
  Object get(final Object key);

  /**
   * Method with which objects can be stored in cache with a given key.
   *
   * @param key   the key to use
   * @param value the item to store
   */
  void put(final Object key, final Object value);

}