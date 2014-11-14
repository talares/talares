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
 * A Java representation of a Page content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)",
 *      "type": "Tridion.ContentDelivery.Page"
 *    },
 *    "Author": null,
 *    "CreationDate": "/Date(1362655241000+60)/",
 *    "InitialPublishDate": "/Date(1371220702600+120)/",
 *    "ItemId": 123,
 *    "LastPublishDate": "/Date(1371793702520+120)/",
 *    "MajorVersion": 1,
 *    "MinorVersion": 2,
 *    "ModificationDate": "/Date(1363341350000+60)/",
 *    "OwningPublication": 1,
 *    "PagePath": "/path/file.doc",
 *    "PublicationId": 1,
 *    "TemplateId": 123,
 *    "Title": "A File",
 *    "Url": "/path/file.doc",
 *    "PageContent": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/PageContent"
 *      }
 *    },
 *    "StructureGroup": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/StructureGroup"
 *      }
 *    },
 *    "ComponentPresentations": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/ComponentPresentations"
 *      }
 *    },
 *    "Keywords": {
 *     "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/Keywords"
 *      }
 *    },
 *    "CustomMetas": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/CustomMetas"
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class Page extends Taxonomised {

  public static final F.Function2<Talares, org.talares.api.datatypes.items.Page, Page> FROM_SCALA =
      new F.Function2<Talares, org.talares.api.datatypes.items.Page, Page>() {
        @Override
        public Page apply(final Talares app, final org.talares.api.datatypes.items.Page page) {
          return new Page(app, page);
        }
      };

  private final Integer templateId;
  private final String pagePath;
  private final String url;
  private final Deferred<org.talares.api.datatypes.items.PageContent, PageContent> pageContent;
  private final Deferred<org.talares.api.datatypes.items.StructureGroup, StructureGroup> structureGroup;

  public Page(final org.talares.api.Talares api, final org.talares.api.datatypes.items.Page scalaPage) {
    super(api, scalaPage);
    this.templateId = asJava(scalaPage.templateId());
    this.pagePath = asJava(scalaPage.pagePath());
    this.url = asJava(scalaPage.url());
    this.pageContent = new Deferred<>(api, scalaPage.pageContent(), PageContent.FROM_SCALA);
    this.structureGroup = new Deferred<>(api, scalaPage.structureGroup(), StructureGroup.FROM_SCALA);
  }

  public final Integer getTemplateId() {
    return templateId;
  }

  public final String getPagePath() {
    return pagePath;
  }

  public final String getUrl() {
    return url;
  }

  public final F.Promise<PageContent> getPageContent() {
    return pageContent.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a PageContent
   * @throws TalaresException
   */
  public final PageContent getPageContent_(final long timeout) throws TalaresException {
    return await(getPageContent(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a PageContent
   * @throws TalaresException
   */
  public final PageContent getPageContent_() throws TalaresException {
    return getPageContent_(getApi().settings().timeout());
  }

  public final F.Promise<StructureGroup> getStructureGroup() {
    return structureGroup.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a StructureGroup
   * @throws TalaresException
   */
  public final StructureGroup getStructureGroup_(final long timeout) throws TalaresException {
    return await(getStructureGroup(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a StructureGroup
   * @throws TalaresException
   */
  public final StructureGroup getStructureGroup_() throws TalaresException {
    return getStructureGroup_(getApi().settings().timeout());
  }
}