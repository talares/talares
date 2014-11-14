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
 * Exception indicating a result was received which was not of the expected type and can therefore not be handled.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
class UnexpectedResultException(location: String, expected: Any, received: Any) extends TalaresException(
  s"""|Received unexpected result:
      |Location: $location
      |Expected: $expected
      |Received: $received
      |""".stripMargin
)