package org.talares.api.actors.messages

import play.api.libs.json.JsValue

/**
 * Object holding messages in use by [[org.talares.api.actors.Executor]]'s.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
object ExecutorMessages {

  case class Execute[T](fetcherTask: FetcherMessages.Task[T], url: String)

  trait Result[T] {
    val fetcherTask: FetcherMessages.Task[T]
  }

  case class Success[T](fetcherTask: FetcherMessages.Task[T], result: JsValue) extends Result[T]

  case class Failure[T](fetcherTask: FetcherMessages.Task[T], throwable: Throwable) extends Result[T]

  object Result {

    def apply[T](fetcherTask: FetcherMessages.Task[T], serviceResult: Either[Throwable, JsValue]): Result[T] = {
      serviceResult match {
        case Left(throwable) => Failure(fetcherTask, throwable)
        case Right(result) => Success(fetcherTask, result)
      }
    }
  }
}