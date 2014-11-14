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

import org.junit.AfterClass;
import org.junit.Test;
import org.talares.api.Talares;
import org.talares.api.cache.NoCache;
import org.talares.api.datatypes.JsonReadable;
import org.talares.api.datatypes.items.Page;
import org.talares.api.datatypes.items.Page$;
import org.talares.api.mock.MockTalares;
import play.api.libs.json.Reads;
import scala.reflect.ClassTag$;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
public class DeferredListTest {

  private static final Talares MOCK_API = new MockTalares(new NoCache());
  private static final String WEBSERVICE_LOCATION = MOCK_API.settings().webserviceLocation();
  private static final JsonReadable<Page> JSON_READABLE = new JsonReadable<Page>() {
    @Override
    public Reads<Page> reads() {
      return Page$.MODULE$.reads();
    }
  };
  private static final org.talares.api.datatypes.DeferredSeq<Page> MOCK_DEFERRED_SEQ =
      new org.talares.api.datatypes.DeferredSeq<>(
          WEBSERVICE_LOCATION + "/Pages(PublicationId=1,ItemId=123)?$filter=bogus",
          JSON_READABLE,
          ClassTag$.MODULE$.<Page>apply(Page.class)
      );

  @Test
  public void testGetValue() throws Exception {

    final DeferredList<Page, org.talares.datatypes.items.Page> deferredList =
        new DeferredList<>(MOCK_API, MOCK_DEFERRED_SEQ, org.talares.datatypes.items.Page.FROM_SCALA);

    final List<org.talares.datatypes.items.Page> pages = deferredList.getValue_();

    assertFalse(pages == null);
    assertFalse(pages.isEmpty());
  }

  @AfterClass
  public static void shutDown() {
    MOCK_API.terminate();
  }
}