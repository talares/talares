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
package org.talares.api.as

import com.ning.http.client.Response
import play.api.libs.json.JsValue

/**
 * Function for parsing a JsValue from a HTTP client Response.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
object Json extends (Response => JsValue) {

  override def apply(r: Response): JsValue =
    (dispatch.as.String andThen (s => play.api.libs.json.Json.parse(s)))(r)
}