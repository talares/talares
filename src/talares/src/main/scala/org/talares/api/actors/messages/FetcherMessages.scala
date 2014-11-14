package org.talares.api.actors.messages

import akka.actor.ActorRef
import org.talares.api.datatypes.JsonReadable
import org.talares.api.queries.Query

import scala.reflect.ClassTag

/**
 * Object holding messages in use by [[org.talares.api.actors.Fetcher]]'s.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
object FetcherMessages {

  trait Task[T] {

    val client: ActorRef
    val cacheKey: Any

    implicit val jsonReadable: JsonReadable[T]
    implicit val classTag: ClassTag[T]
  }

  case class FetchByURI[T](client: ActorRef, uri: String)
                          (implicit val jsonReadable: JsonReadable[T], val classTag: ClassTag[T]) extends Task[T] {

    lazy val cacheKey = uri.hashCode
  }

  case class FetchByID[T](client: ActorRef, webserviceLocation: String, IDs: (String, Any)*)
                         (implicit val jsonReadable: JsonReadable[T], val classTag: ClassTag[T]) extends Task[T] {

    lazy val cacheKey = IDs.toList.sortBy(_._1).toString().hashCode
  }

  case class FetchBySearch[T](client: ActorRef, webserviceLocation: String, searchParams: (String, Any)*)
                             (implicit val jsonReadable: JsonReadable[T], val classTag: ClassTag[T]) extends Task[T] {

    lazy val cacheKey = searchParams.sortBy(_._1).toString().hashCode
  }

  case class FetchByQuery[T](client: ActorRef, webserviceLocation: String, query: Query)
                            (implicit val jsonReadable: JsonReadable[T], val classTag: ClassTag[T]) extends Task[T] {

    lazy val cacheKey = query.toString.hashCode
  }

  trait Result[A] {
    val task: Task[A]
  }

  trait Success[A, B] extends Result[A] {
    val value: B
  }

  case class SingleResult[A, B](task: Task[A], value: B) extends Success[A, B]

  case class MultiResult[A, B](task: Task[A], value: Seq[A]) extends Success[A, Seq[A]]

  case class Failure[T](task: Task[T], throwable: Throwable) extends Result[T]

  object Success {

    def unapply[A, B](success: Success[A, B]): Option[(Task[A], B)] = success match {
      case result: SingleResult[A, B] => SingleResult.unapply(result)
      case result: MultiResult[A, B] => MultiResult.unapply(result)
    }
  }

}