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
 *    &quot;__metadata&quot;: {
 *      &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)&quot;,
 *      &quot;type&quot;: &quot;Tridion.ContentDelivery.Page&quot;
 *    },
 *    &quot;Author&quot;: null,
 *    &quot;CreationDate&quot;: &quot;/Date(1362655241000+60)/&quot;,
 *    &quot;InitialPublishDate&quot;: &quot;/Date(1371220702600+120)/&quot;,
 *    &quot;ItemId&quot;: 123,
 *    &quot;LastPublishDate&quot;: &quot;/Date(1371793702520+120)/&quot;,
 *    &quot;MajorVersion&quot;: 1,
 *    &quot;MinorVersion&quot;: 2,
 *    &quot;ModificationDate&quot;: &quot;/Date(1363341350000+60)/&quot;,
 *    &quot;OwningPublication&quot;: 1,
 *    &quot;PagePath&quot;: &quot;/path/file.doc&quot;,
 *    &quot;PublicationId&quot;: 1,
 *    &quot;TemplateId&quot;: 123,
 *    &quot;Title&quot;: &quot;A File&quot;,
 *    &quot;Url&quot;: &quot;/path/file.doc&quot;,
 *    &quot;PageContent&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/PageContent&quot;
 *      }
 *    },
 *    &quot;StructureGroup&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/StructureGroup&quot;
 *      }
 *    },
 *    &quot;ComponentPresentations&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/ComponentPresentations&quot;
 *      }
 *    },
 *    &quot;Keywords&quot;: {
 *     &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/Keywords&quot;
 *      }
 *    },
 *    &quot;CustomMetas&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Pages(ItemId=123,PublicationId=1)/CustomMetas&quot;
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
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final PageContent getPageContent_(final long timeout) throws TalaresException {
    return await(getPageContent(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a PageContent
   * @throws TalaresException wrapping multiple possible exception cases
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
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final StructureGroup getStructureGroup_(final long timeout) throws TalaresException {
    return await(getStructureGroup(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a StructureGroup
   * @throws TalaresException wrapping multiple possible exception cases
   */
  public final StructureGroup getStructureGroup_() throws TalaresException {
    return getStructureGroup_(getApi().settings().timeout());
  }
}