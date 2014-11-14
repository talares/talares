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
package org.talares.datatypes;

import org.talares.api.Talares;
import org.talares.api.datatypes.items.Item;
import org.talares.api.exceptions.TalaresException;
import org.talares.exceptions.TransformException;
import play.libs.F;
import scala.Option;

import static org.talares.utils.Utils.await;

/**
 * A Deferred instance holds a {@link play.libs.F.Promise} of a B to be resolved by an additional call to the
 * webservice.
 * <p/>
 * The call to said webservice will only be triggered when the value method is accessed.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class Deferred<A extends Item, B> {

  private final Talares api;
  private final org.talares.api.datatypes.Deferred<A> scalaDeferred;
  private final F.Function2<Talares, A, B> transformFunc;

  public Deferred(final Talares scalaApi,
                  final org.talares.api.datatypes.Deferred<A> scalaDeferred,
                  final F.Function2<Talares, A, B> scalaToJavaFunc) {
    this.api = scalaApi;
    this.scalaDeferred = scalaDeferred;
    this.transformFunc = scalaToJavaFunc;
  }

  /**
   * Gets the {@link play.libs.F.Promise} of a B this Deferred holds.
   * <p/>
   * Fetches the {@link scala.concurrent.Future} from the Scala Deferred and wraps it in a {@link play.libs.F.Promise}.
   * <p/>
   * Then applies the {@link org.talares.datatypes.Deferred#transformFunc} to morph the result from a Scala A to it's
   * Java counterpart B.
   *
   * @return a {@link play.libs.F.Promise} of B
   */
  public final F.Promise<B> getValue() {

    final F.Promise<Option<A>> promise = F.Promise.wrap(scalaDeferred.value(api));

    return promise.map(new F.Function<Option<A>, B>() {

      @Override
      public B apply(final Option<A> option) throws TransformException {
        if (option.isDefined()) {
          try {
            return transformFunc.apply(api, option.get());
          } catch (Throwable throwable) {
            throw new TransformException(throwable);
          }
        }
        return null;
      }
    });
  }

  /**
   * <b>BLOCKING</b>
   * <p/>
   * Get the B held by this Deferred.
   *
   * @param timeout the timeout in milliseconds within which the B should be present
   * @return an instance of B
   * @throws TalaresException
   */
  public final B getValue_(long timeout) throws TalaresException {
    return await(getValue(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p/>
   * Get the B held by this Deferred.
   *
   * @return an instance of B
   * @throws TalaresException
   */
  public final B getValue_() throws TalaresException {
    return getValue_(api.settings().timeout());
  }
}