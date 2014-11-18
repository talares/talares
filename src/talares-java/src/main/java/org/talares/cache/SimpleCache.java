package org.talares.cache;

/**
 * Leverages the given functions get and set to either retrieve or store objects from and into the cache respectively.
 * <p>
 * Does not add any additional caching logic and as such honors the given cache's configuration.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public interface SimpleCache extends Cache {
}