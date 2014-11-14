package org.talares.utils;

import org.talares.api.exceptions.TalaresException;
import org.talares.api.exceptions.TalaresException$;
import play.libs.F;

/**
 * Holds commonly used utility functions.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public abstract class Utils {

  /**
   * Await a {@link play.libs.F.Promise} of type 'T'.
   * <p/>
   * Uses the specified long as timeout. Wraps any exception thrown by the {@link play.libs.F.Promise} in a
   * {@link org.talares.api.exceptions.TalaresException}.
   *
   * @param promise the {@link play.libs.F.Promise} to await
   * @param timeout the timeout used for waiting
   * @param <T>     the type of object the {@link play.libs.F.Promise} holds
   * @return the value of type 'T' produced by the {@link play.libs.F.Promise}
   * @throws TalaresException
   */
  public static <T> T await(F.Promise<T> promise, long timeout) throws TalaresException {
    try {
      return promise.get(timeout);
    } catch (Exception e) {
      throw TalaresException$.MODULE$.apply(e);
    }
  }
}