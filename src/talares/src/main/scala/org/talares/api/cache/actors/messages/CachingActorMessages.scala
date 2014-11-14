package org.talares.api.cache.actors.messages

import org.talares.api.actors.messages.FetcherMessages

/**
 * Object holding messages in use by [[org.talares.api.cache.actors.CachingActor]]'s.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
object CachingActorMessages {
  
  case class RetrieveFromCache(fetcherTask: FetcherMessages.Task[_])

  case class StoreInCache(fetcherTask: FetcherMessages.Task[_], value: Any)

  trait Result

  case class NotFound(fetcherTask: FetcherMessages.Task[_]) extends Result

  case class Found(fetcherTask: FetcherMessages.Task[_], value: Any) extends Result
}
