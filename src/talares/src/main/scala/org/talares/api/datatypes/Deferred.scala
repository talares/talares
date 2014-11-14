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

import akka.pattern.ask
import org.talares.api.Talares
import org.talares.api.actors.messages.MediatorMessages
import org.talares.api.datatypes.items.Item
import play.api.libs.json.{Reads, JsPath}

import scala.annotation.implicitNotFound
import scala.concurrent.Future
import scala.reflect.ClassTag

/**
 * A deferred instance holds a Future of type 'T' to be resolved by an additional call to the webservice.
 *
 * The call to said webservice will only be triggered when the value field is accessed.
 *
 * @param uri the URI supplied by the webservice which holds the complete path where the data for 'T' resides
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class Deferred[T <: Item](uri: String)(implicit jsonReadable: JsonReadable[T], classTag: ClassTag[T]) {

  @implicitNotFound(
    "No implicit value for org.talares.api.Talares found. Try to import org.talares.api.Talares.current."
  ) def value(implicit app: Talares): Future[Option[T]] = {

    val system = app.system
    val mediator = app.mediator

    implicit val executionContext = system.dispatcher
    implicit val timeout = app.timeout

    (mediator ? MediatorMessages.URIRequest[T](uri)) map {
      case MediatorMessages.Response(value: T) => Some(value)
      case _ => None
    }
  }
}

object Deferred {

  implicit def reads[T <: Item](implicit jsonReadable: JsonReadable[T], classTag: ClassTag[T]): Reads[Deferred[T]] =
    (JsPath \ "__deferred" \ "uri").read[String].map(Deferred[T])
}