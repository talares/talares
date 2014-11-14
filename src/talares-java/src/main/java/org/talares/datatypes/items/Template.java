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
import play.libs.F;

/**
 * A Java representation of a Template content type.
 *
 * Example Json:
 * <pre>
 *  {
 *    "__metadata": {
 *      "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Templates(PublicationId=1,ItemId=123)",
 *      "type": "Tridion.ContentDelivery.Template"
 *    },
 *    "Author": "User",
 *    "CreationDate": "/Date(1399546761343+120)/",
 *    "InitialPublishDate": "/Date(1399546761343+120)/",
 *    "ItemId": 123,
 *    "LastPublishDate": "/Date(1399546761343+120)/",
 *    "MajorVersion": 1,
 *    "MinorVersion": 2,
 *    "ModificationDate": "/Date(1399546761343+120)/",
 *    "OutputFormat": "HTML Fragment",
 *    "OwningPublication": 0,
 *    "PublicationId": 1,
 *    "TemplatePriority": 200,
 *    "Title": "A title",
 *    "ComponentPresentations": {
 *      "__deferred": {
 *        "uri": "http://127.0.0.1:8080/cd_webservice/odata.svc/Templates(PublicationId=1,ItemId=123)/ComponentPresentations"
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public class Template extends ComponentPresentationsHolder {

  public static final F.Function2<Talares, org.talares.api.datatypes.items.Template, Template> FROM_SCALA =
      new F.Function2<Talares, org.talares.api.datatypes.items.Template, Template>() {
        @Override
        public Template apply(final Talares app,final  org.talares.api.datatypes.items.Template template)
            {
          return new Template(app, template);
        }
      };

  private final String outputFormat;
  private final int templatePriority;

  public Template(final org.talares.api.Talares api, final org.talares.api.datatypes.items.Template scalaTemplate) {
    super(api, scalaTemplate);
    this.outputFormat = scalaTemplate.outputFormat();
    this.templatePriority = scalaTemplate.templatePriority();
  }

  public final String getOutputFormat() {
    return outputFormat;
  }

  public final int getTemplatePriority() {
    return templatePriority;
  }
}