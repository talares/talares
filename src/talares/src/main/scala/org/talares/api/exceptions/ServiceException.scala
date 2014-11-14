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
package org.talares.api.exceptions

/**
 * Exception indicating a discrepancy regarding the web service.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
class ServiceErrorException(url: String, message: String) extends TalaresException(
  s"""|Service returned error:
      |Location: $url
      |Error:
      |$message
      |""".stripMargin
)

object ServiceErrorException {

  def apply(url: String, exception: Throwable): ServiceErrorException = {
    new ServiceErrorException(url, exception.getStackTrace.mkString("\n"))
  }
}