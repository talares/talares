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
 *    &quot;__metadata&quot;: {
 *      &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Templates(PublicationId=1,ItemId=123)&quot;,
 *      &quot;type&quot;: &quot;Tridion.ContentDelivery.Template&quot;
 *    },
 *    &quot;Author&quot;: &quot;User&quot;,
 *    &quot;CreationDate&quot;: &quot;/Date(1399546761343+120)/&quot;,
 *    &quot;InitialPublishDate&quot;: &quot;/Date(1399546761343+120)/&quot;,
 *    &quot;ItemId&quot;: 123,
 *    &quot;LastPublishDate&quot;: &quot;/Date(1399546761343+120)/&quot;,
 *    &quot;MajorVersion&quot;: 1,
 *    &quot;MinorVersion&quot;: 2,
 *    &quot;ModificationDate&quot;: &quot;/Date(1399546761343+120)/&quot;,
 *    &quot;OutputFormat&quot;: &quot;HTML Fragment&quot;,
 *    &quot;OwningPublication&quot;: 0,
 *    &quot;PublicationId&quot;: 1,
 *    &quot;TemplatePriority&quot;: 200,
 *    &quot;Title&quot;: &quot;A title&quot;,
 *    &quot;ComponentPresentations&quot;: {
 *      &quot;__deferred&quot;: {
 *        &quot;uri&quot;: &quot;http://127.0.0.1:8080/cd_webservice/odata.svc/Templates(PublicationId=1,ItemId=123)/ComponentPresentations&quot;
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