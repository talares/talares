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
import org.talares.api.datatypes.DeferredSeq;
import org.talares.api.datatypes.items.Item;
import org.talares.api.exceptions.TalaresException;
import org.talares.exceptions.TransformException;
import play.libs.F;
import scala.collection.Seq;

import java.util.ArrayList;
import java.util.List;

import static org.talares.utils.Utils.await;
import static play.libs.Scala.asJava;

/**
 * A DeferredList instance holds a {@link play.libs.F.Promise} of a {@link java.util.List} of B to be resolved by an 
 * additional call to the webservice.
 * <p>
 * The call to said webservice will only be triggered when the value method is accessed.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class DeferredList<A extends Item, B> {

  private final Talares api;
  private final DeferredSeq<A> deferredSeq;
  private final F.Function2<Talares, A, B> transformFunc;

  public DeferredList(final Talares scalaApi,
                      final DeferredSeq<A> scalaDeferredSeq,
                      final F.Function2<Talares, A, B> scalaToJavaFunc) {
    this.api = scalaApi;
    this.deferredSeq = scalaDeferredSeq;
    this.transformFunc = scalaToJavaFunc;
  }

  /**
   * Gets the {@link play.libs.F.Promise} of a {@link java.util.List} of B this DeferredList holds.
   * <p>
   * Fetches the {@link scala.concurrent.Future} from the Scala DeferredSeq and wraps it in a
   * {@link play.libs.F.Promise}.
   * <p>
   * Then applies the {@link org.talares.datatypes.Deferred#transformFunc} to morph the result from a Scala
   * {@link scala.collection.Seq} of A it's Java counterpart; a {@link java.util.List} of B.
   *
   * @return a {@link play.libs.F.Promise} of B
   */
  public final F.Promise<List<B>> getValue() {

    final F.Promise<Seq<A>> promise = F.Promise.wrap(deferredSeq.value(api));

    return promise.map(new F.Function<Seq<A>, List<B>>() {

      @Override
      public List<B> apply(final Seq<A> itemSeq) throws TransformException {

        final List<B> itemList = new ArrayList<>();

        for (final A scalaItem : asJava(itemSeq)) {
          try {
            itemList.add(transformFunc.apply(api, scalaItem));
          } catch (Throwable throwable) {
            throw new TransformException(throwable);
          }
        }

        return itemList;
      }
    });
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get the {@link java.util.List} of B held by this DeferredList.
   *
   * @param timeout the timeout in milliseconds within which the {@link java.util.List} of B should be present
   * @return an instance of B
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<B> getValue_(long timeout) throws TalaresException {
    return await(getValue(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   * <p>
   * Get the {@link java.util.List} of B held by this DeferredList.
   *
   * @return an instance of B
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final List<B> getValue_() throws TalaresException {
    return getValue_(api.settings().timeout());
  }
}