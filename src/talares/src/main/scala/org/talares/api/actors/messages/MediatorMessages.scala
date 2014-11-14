package org.talares.api.actors.messages

import org.talares.api.datatypes.JsonReadable
import org.talares.api.datatypes.items.Item
import org.talares.api.queries.Query

import scala.reflect.ClassTag

/**
 * Object holding messages in use by [[org.talares.api.actors.Mediator]]'s.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
object MediatorMessages {

  trait Request[T <: Item] {

    implicit val jsonReadable: JsonReadable[T]
    implicit val classTag: ClassTag[T]
  }

  case class URIRequest[T <: Item](uri: String)
                                  (implicit val jsonReadable: JsonReadable[T], val classTag: ClassTag[T])
    extends Request[T]

  case class IDRequest[T <: Item](webserviceLocation: String, IDs: (String, Any)*)
                                 (implicit val jsonReadable: JsonReadable[T], val classTag: ClassTag[T])
    extends Request[T]

  case class SearchRequest[T <: Item](webserviceLocation: String, searchParams: (String, Any)*)
                                     (implicit val jsonReadable: JsonReadable[T], val classTag: ClassTag[T])
    extends Request[T]

  case class QueryRequest[T <: Item](webserviceLocation: String, query: Query)
                                    (implicit val jsonReadable: JsonReadable[T], val classTag: ClassTag[T])
    extends Request[T]

  case class Response(value: Any)

}