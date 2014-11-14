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
import org.talares.datatypes.DeferredList;
import play.libs.F;

import java.util.List;

import static org.talares.utils.Utils.await;

/**
 * A Java representation of a StructureGroup content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(PublicationId=1,Id=2)",
 *      "type": "Tridion.ContentDelivery.StructureGroup"
 *    },
 *    "Depth": 0,
 *    "Directory": null,
 *    "Id": 123,
 *    "PublicationId": 1,
 *    "Title": "A title",
 *    "Pages": {
 *      "__deferred": {
 *          "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(Id=2,PublicationId=1)/Pages"
 *      }
 *    },
 *    "Children": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(Id=2,PublicationId=1)/Children"
 *      }
 *    },
 *    "Parent": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/StructureGroups(Id=2,PublicationId=1)/Parent"
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class StructureGroup extends Item {

  public static final F.Function2<Talares, org.talares.api.datatypes.items.StructureGroup, StructureGroup> FROM_SCALA =
      new F.Function2<Talares, org.talares.api.datatypes.items.StructureGroup, StructureGroup>() {
        @Override
        public StructureGroup apply(final Talares app,
                                    final org.talares.api.datatypes.items.StructureGroup structureGroup)
            {
          return new StructureGroup(app, structureGroup);
        }
      };

  private final int id;
  private final String title;
  private final int depth;
  private final String directory;
  private final DeferredList<org.talares.api.datatypes.items.Page, Page> pages;
  private final Deferred<org.talares.api.datatypes.items.StructureGroup, StructureGroup> parent;
  private final DeferredList<org.talares.api.datatypes.items.StructureGroup, StructureGroup> children;

  public StructureGroup(final org.talares.api.Talares api,
                        final org.talares.api.datatypes.items.StructureGroup scalaStructureGroup) {
    super(api, scalaStructureGroup);
    this.id = scalaStructureGroup.id();
    this.title = scalaStructureGroup.title();
    this.depth = scalaStructureGroup.depth();
    this.directory = asJava(scalaStructureGroup.directory());
    this.pages = new DeferredList<>(api, scalaStructureGroup.pages(), Page.FROM_SCALA);
    this.parent = new Deferred<>(api, scalaStructureGroup.parent(), StructureGroup.FROM_SCALA);
    this.children = new DeferredList<>(api, scalaStructureGroup.children(), StructureGroup.FROM_SCALA);
  }

  public final int getId() {
    return id;
  }

  public final String getTitle() {
    return title;
  }

  public final int getDepth() {
    return depth;
  }

  public final String getDirectory() {
    return directory;
  }

  public final F.Promise<List<Page>> getPages() {
    return pages.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of Page's
   * @throws TalaresException
   */
  public final List<Page> getPages_(final long timeout) throws TalaresException {
    return await(getPages(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of Page's
   * @throws TalaresException
   */
  public final List<Page> getPages_() throws TalaresException {
    return getPages_(getApi().settings().timeout());
  }

  public final F.Promise<StructureGroup> getParent() {
    return parent.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a StructureGroup
   * @throws TalaresException
   */
  public final StructureGroup getParent_(final long timeout) throws TalaresException {
    return await(getParent(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a StructureGroup
   * @throws TalaresException
   */
  public final StructureGroup getParent_() throws TalaresException {
    return getParent_(getApi().settings().timeout());
  }

  public final F.Promise<List<StructureGroup>> getChildren() {
    return children.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a List of StructureGroup's
   * @throws TalaresException
   */
  public final List<StructureGroup> getChildren_(final long timeout) throws TalaresException {
    return await(getChildren(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a List of StructureGroup's
   * @throws TalaresException
   */
  public final List<StructureGroup> getChildren_() throws TalaresException {
    return getChildren_(getApi().settings().timeout());
  }
}