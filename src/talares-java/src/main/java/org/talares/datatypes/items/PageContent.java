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
package org.talares.datatypes.items;

import org.talares.api.Talares;
import org.talares.api.exceptions.TalaresException;
import org.talares.datatypes.Deferred;
import play.libs.F;

import static org.talares.utils.Utils.await;

/**
 * A Java representation of a PageContent content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/PageContents(PageId=123,PublicationId=1)",
 *      "type": "Tridion.ContentDelivery.PageContent"
 *    },
 *    "CharSet": "UTF8",
 *    "Content": "<h1>Some content</h1>",
 *    "PageId": 123,
 *    "PublicationId": 1,
 *    "Page": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/PageContents(PageId=123,PublicationId=1)/Page"
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class PageContent extends Item {

  public static final F.Function2<Talares, org.talares.api.datatypes.items.PageContent, PageContent> FROM_SCALA =
      new F.Function2<Talares, org.talares.api.datatypes.items.PageContent, PageContent>() {
        @Override
        public PageContent apply(final Talares app,final  org.talares.api.datatypes.items.PageContent pageContent)
            {
          return new PageContent(app, pageContent);
        }
      };

  private final int pageId;
  private final String charset;
  private final String content;
  private final Deferred<org.talares.api.datatypes.items.Page, Page> page;

  public PageContent(final org.talares.api.Talares api,
                     final org.talares.api.datatypes.items.PageContent scalaPageContent) {
    super(api, scalaPageContent);
    this.pageId = scalaPageContent.pageId();
    this.charset = asJava(scalaPageContent.charset());
    this.content = asJava(scalaPageContent.content());
    this.page = new Deferred<>(api, scalaPageContent.page(), Page.FROM_SCALA);
  }

  public final int getPageId() {
    return pageId;
  }

  public final String getCharset() {
    return charset;
  }

  public final String getContent() {
    return content;
  }

  public final F.Promise<Page> getPage() {
    return page.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a Page
   * @throws TalaresException
   */
  public final Page getPage_(final long timeout) throws TalaresException {
    return await(getPage(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a Page
   * @throws TalaresException
   */
  public final Page getPage_() throws TalaresException {
    return getPage_(getApi().settings().timeout());
  }
}