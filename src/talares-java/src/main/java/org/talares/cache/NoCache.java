package org.talares.cache;

/**
 * Caching strategy which does not apply any caching.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class NoCache implements Cache {

  @Override
  public final Object get(Object key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final void put(Object key, Object value) {
    throw new UnsupportedOperationException();
  }
}