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
package org.talares.api.datatypes

import java.util.regex.Pattern

import org.joda.time.DateTime
import play.api.data.validation.ValidationError
import play.api.libs.json.{JsPath, JsError, JsSuccess, JsString, JsValue, Reads}

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
package object items {

  implicit object EdmDateTimeReads extends Reads[DateTime] {

    val pattern = Pattern.compile("/Date\\(([0-9]+\\+[0-9]*)\\)/")

    def reads(json: JsValue) = json match {
      case JsString(s) =>

        val matcher = pattern.matcher(s)

        if (matcher.matches) {

          val split = matcher.group(1).split("\\+")
          val ticks = split(0).toLong
          val offset = split(1).toInt

          JsSuccess(new DateTime(ticks).plusMinutes(offset))
        }

        else JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.edm.datetime", pattern.toString))))

      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsstring"))))
    }
  }

}