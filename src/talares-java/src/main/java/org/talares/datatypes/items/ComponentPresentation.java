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
 * A Java representation of a ComponentPresentation content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)",
 *      "type": "Tridion.ContentDelivery.ComponentPresentation"
 *    },
 *    "ComponentId": 123,
 *    "OutputFormat": "HTML Fragment",
 *    "PresentationContent": "<h1>Some content</h1>",
 *    "PublicationId": 1,
 *    "TemplateId": 123,
 *    "Component": {
 *     "__deferred": {
 *          "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)/Component"
 *      }
 *    },
 *    "Template": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)/Template"
 *      }
 *    },
 *    "Pages": {
 *      "__deferred": {
 *         "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/ComponentPresentations(PublicationId=1,ComponentId=123,TemplateId=1234)/Pages"
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class ComponentPresentation extends Item {

  public static final F.Function2<Talares, org.talares.api.datatypes.items.ComponentPresentation, ComponentPresentation> FROM_SCALA =
      new F.Function2<Talares, org.talares.api.datatypes.items.ComponentPresentation, ComponentPresentation>() {
        @Override
        public ComponentPresentation apply(final Talares app,
                                           final org.talares.api.datatypes.items.ComponentPresentation componentPresentation)
            {
          return new ComponentPresentation(app, componentPresentation);
        }
      };

  private final int componentId;
  private final int templateId;
  private final String outputFormat;
  private final String presentationContent;
  private final Deferred<org.talares.api.datatypes.items.Component, Component> component;
  private final Deferred<org.talares.api.datatypes.items.Template, Template> template;
  private final DeferredList<org.talares.api.datatypes.items.Page, Page> pages;

  public ComponentPresentation(final org.talares.api.Talares api,
                               final org.talares.api.datatypes.items.ComponentPresentation scalaComponentPresentation) {
    super(api, scalaComponentPresentation);
    this.componentId = scalaComponentPresentation.componentId();
    this.templateId = scalaComponentPresentation.templateId();
    this.outputFormat = scalaComponentPresentation.outputFormat();
    this.presentationContent = scalaComponentPresentation.presentationContent();
    this.component = new Deferred<>(api, scalaComponentPresentation.component(), Component.FROM_SCALA);
    this.template = new Deferred<>(api, scalaComponentPresentation.template(), Template.FROM_SCALA);
    this.pages = new DeferredList<>(api, scalaComponentPresentation.pages(), Page.FROM_SCALA);
  }

  public final int getComponentId() {
    return componentId;
  }

  public final int getTemplateId() {
    return templateId;
  }

  public final String getOutputFormat() {
    return outputFormat;
  }

  public final String getPresentationContent() {
    return presentationContent;
  }

  public final F.Promise<Component> getComponent() {
    return component.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a Component
   * @throws TalaresException
   */
  public final Component getComponent_(long timeout) throws TalaresException {
    return await(getComponent(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a Component
   * @throws TalaresException
   */
  public final Component getComponent_() throws TalaresException {
    return getComponent_(getApi().settings().timeout());
  }

  public final F.Promise<Template> getTemplate() {
    return template.getValue();
  }

  /**
   * <b>BLOCKING</b>
   *
   * @param timeout the timeout in which the result should be present
   * @return a Template
   * @throws TalaresException
   */
  public final Template getTemplate_(long timeout) throws TalaresException {
    return await(getTemplate(), timeout);
  }

  /**
   * <b>BLOCKING</b>
   *
   * @return a Template
   * @throws TalaresException
   */
  public final Template getTemplate_() throws TalaresException {
    return getTemplate_(getApi().settings().timeout());
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
  public final List<Page> getPages_(long timeout) throws TalaresException {
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
}