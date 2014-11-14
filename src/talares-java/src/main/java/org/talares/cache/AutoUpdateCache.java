package org.talares.cache;

/**
 * Leverages the given functions get and set to either retrieve or store objects from and into the cache respectively.
 * <p/>
 * Always returns a cached instance of a specific item once it has entered the cache once.
 * <p/>
 * A configurable amount of times will be accepted for accessing a specific item in the cache, before this strategy
 * issues an update of said item.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public interface AutoUpdateCache extends Cache {
}