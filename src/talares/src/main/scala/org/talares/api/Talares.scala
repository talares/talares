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
package org.talares.api

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.talares.api.actors.Mediator
import org.talares.api.actors.messages.MediatorMessages
import org.talares.api.cache.{Cache, NoCache}
import org.talares.api.datatypes.JsonReadable
import org.talares.api.datatypes.items._
import org.talares.api.exceptions.{TalaresException, UnexpectedResultException}
import org.talares.api.queries.Query

import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}
import scala.reflect.ClassTag
import scala.util.{Failure, Success}

/**
 * Class holding the main client facing API.
 *
 * An instance of this class should be created to gain access to the API.
 * A cache strategy should be decided upon and a corresponding instance of a subclass of
 * [[org.talares.api.cache.Cache]] should be provided to the constructor of this class.
 *
 * Example:
 * {{{
 *   import org.talares.api.Talares
 *   import org.talares.api.datatypes.items.Page
 *   import scala.concurrent.Future
 *
 *   val talares = Talares(cache)
 *   Future[Page] pageFuture = talares.getPage(1, 2)
 * }}}
 *
 * @author Dennis Vis
 * @since 0.1.0
 * @see org.talares.api.cache.Cache
 */
class Talares private[api](val cache: Cache = NoCache()) {

  /** Reference to the configured settings **/
  val settings = Settings()

  /**
   * The Talares actor system.
   *
   * Created once by the library. Can and should be terminated by the terminate() function.
   */
  private[talares] val system = ActorSystem("talares-actor-system")

  import system.dispatcher

  /** Timeout used by Akka when leveraging the aks pattern **/
  implicit val timeout = Timeout(settings.timeout millis)

  /** The [[org.talares.api.actors.Mediator]] for use within the library. **/
  lazy val mediator = system.actorOf(Props(new Mediator(this, cache)), "mediator")

  /**
   * Fetches a 'T' by it's ID's.
   *
   * First composes a [[org.talares.api.actors.messages.MediatorMessages.IDRequest]] from the given ID's and the
   * configured web service location. Then passes this to the [[org.talares.api.actors.Mediator]] for further handling.
   * It does this using the ask pattern.
   *
   * Once complete, the result of the ask is inspected. If the result contains a value or a [[scala.collection.Seq]] of
   * the expected type 'T', a promise is completed successfully and it's future returned.
   * Otherwise the promise is failed with a [[org.talares.api.exceptions.TalaresException]], either directly from the
   * Future's result, or by wrapping a unknown Throwable in a [[org.talares.api.exceptions.TalaresException]].
   *
   * @param IDs the ID's to compose the [[org.talares.api.actors.messages.MediatorMessages.IDRequest]] with
   * @param jsonReadable the [[org.talares.api.datatypes.JsonReadable]] which can turn the Json representation of a 'T'
   *                     into an instance of 'T'
   * @param classTag the class tag of 'T' to be injected by the compiler
   * @tparam T the type of item that is expected in return
   * @return a future of 'T'
   */
  private def getIDResponse[T <: Item](IDs: (String, Any)*)
                                      (implicit jsonReadable: JsonReadable[T], classTag: ClassTag[T]): Future[T] = {

    val p = Promise[T]()

    val request = MediatorMessages.IDRequest[T](settings.webserviceLocation, IDs: _*)

    (mediator ? request) onComplete {
      case Success(MediatorMessages.Response(value)) if classTag.runtimeClass.isInstance(value) =>
        p.success(value.asInstanceOf[T])
      case Success(other) =>
        val location = IDs.foldLeft("") {
          case (acc, (name, value)) => s"$acc|$name=$value"
        }
        p.failure(new UnexpectedResultException(location, classOf[MediatorMessages.Response], other))
      case Failure(exception: TalaresException) => p.failure(exception)
      case Failure(throwable) => p.failure(TalaresException(throwable))
    }

    p.future
  }

  /**
   * Fetches a 'T' by a set of search parameters.
   *
   * First composes a [[org.talares.api.actors.messages.MediatorMessages.SearchRequest]] from the given search
   * parameters and the configured web service location. Then passes this to the [[org.talares.api.actors.Mediator]]
   * for further handling. It does this using the ask pattern.
   *
   * Once complete, the result of the ask is inspected. If the result contains a value or a [[scala.collection.Seq]] of
   * the expected type 'T', a promise is completed successfully and it's future returned.
   * Otherwise the promise is failed with a [[org.talares.api.exceptions.TalaresException]], either directly from the
   * Future's result, or by wrapping a unknown Throwable in a [[org.talares.api.exceptions.TalaresException]].
   *
   * @param searchParams the search parameters to compose the
   *                     [[org.talares.api.actors.messages.MediatorMessages.SearchRequest]] with
   * @param jsonReadable the [[org.talares.api.datatypes.JsonReadable]] which can turn the Json representation of a 'T'
   *                     into an instance of 'T'
   * @param classTag the class tag of 'T' to be injected by the compiler
   * @tparam T the type of item that is expected in return
   * @return a future of 'T'
   */
  private def getSearchResponse[T <: Item](searchParams: (String, Any)*)
                                          (implicit jsonReadable: JsonReadable[T],
                                           classTag: ClassTag[T],
                                           seqClassTag: ClassTag[Seq[T]]): Future[Seq[T]] = {

    val p = Promise[Seq[T]]()

    val request = MediatorMessages.SearchRequest[Page](settings.webserviceLocation, searchParams: _*)

    (mediator ? request) onComplete {
      case Success(MediatorMessages.Response(value)) if seqClassTag.runtimeClass.isInstance(value) =>
        p.success(value.asInstanceOf[Seq[T]])
      case Success(other) => p.failure(
        new UnexpectedResultException(searchParams.toString(), classOf[MediatorMessages.Response], other)
      )
      case Failure(exception: TalaresException) => p.failure(exception)
      case Failure(throwable) => p.failure(TalaresException(throwable))
    }

    p.future
  }

  /**
   * Fetches a 'T' by a [[org.talares.api.queries.Query]].
   *
   * First composes a [[org.talares.api.actors.messages.MediatorMessages.QueryRequest]] from the given
   * [[org.talares.api.queries.Query]] and the configured web service location. Then passes this to the
   * [[org.talares.api.actors.Mediator]] for further handling. It does this using the ask pattern.
   *
   * Once complete, the result of the ask is inspected. If the result contains a value or a Seq of the expected type
   * 'T', a promise is completed successfully and it's future returned.
   * Otherwise the promise is failed with a [[org.talares.api.exceptions.TalaresException]], either directly from the
   * Future's result, or by wrapping a unknown Throwable in a [[org.talares.api.exceptions.TalaresException]].
   *
   * @param query the [[org.talares.api.queries.Query]] to compose the
   *              [[org.talares.api.actors.messages.MediatorMessages.QueryRequest]] with
   * @param jsonReadable the [[org.talares.api.datatypes.JsonReadable]] which can turn the Json representation of a 'T'
   *                     into an instance of 'T'
   * @param classTag the class tag of 'T' to be injected by the compiler
   * @tparam T the type of item that is expected in return
   * @return a future of 'T'
   */
  def executeQuery[T <: Item](query: Query)(implicit jsonReadable: JsonReadable[T],
                                            classTag: ClassTag[T],
                                            seqClassTag: ClassTag[Seq[T]]): Future[Seq[T]] = {

    val p = Promise[Seq[T]]()

    val request = MediatorMessages.QueryRequest[T](settings.webserviceLocation, query)

    (mediator ? request) onComplete {
      case Success(MediatorMessages.Response(value: T)) if classTag.runtimeClass.isInstance(value) => Seq(value)
      case Success(MediatorMessages.Response(value)) if seqClassTag.runtimeClass.isInstance(value) =>
        value.asInstanceOf[Seq[T]]
      case Success(other) => p.failure(
        new UnexpectedResultException(query.toString(), classOf[MediatorMessages.Response], other)
      )
      case Failure(exception: TalaresException) => p.failure(exception)
      case Failure(throwable) => p.failure(TalaresException(throwable))
    }

    p.future
  }

  /**
   * Terminates the library which implies shutting down the actor system.
   * Should be used when the API will no longer be accesses and only then.
   */
  def terminate(): Unit = {
    system.shutdown()
    Talares._currentApp = null
  }

  /**
   * Get a [[org.talares.api.datatypes.items.Binary]] by it's publication ID and it's binary ID.
   *
   * @param publicationId the ID of the publication the desired [[org.talares.api.datatypes.items.Binary]] belongs to
   * @param binaryId the binary ID of the desired [[org.talares.api.datatypes.items.Binary]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.Binary]]
   */
  def getBinary(publicationId: Int, binaryId: Int): Future[Binary] = {

    implicit val reads = Binary.reads
    implicit val classTag = ClassTag(Binary.getClass)

    getIDResponse[Binary]("PublicationId" -> publicationId, "BinaryId" -> binaryId)
  }

  /**
   * Get a [[org.talares.api.datatypes.items.BinaryContent]] by it's publication ID, it's binary ID and it's variant ID.
   *
   * @param publicationId the ID of the publication the desired [[org.talares.api.datatypes.items.BinaryContent]]
   *                      belongs to
   * @param binaryId the binary ID of the desired [[org.talares.api.datatypes.items.BinaryContent]]
   * @param variantId the variantId ID of the desired [[org.talares.api.datatypes.items.BinaryContent]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.BinaryContent]]
   */
  def getBinaryContent(publicationId: Int, binaryId: Int, variantId: String): Future[BinaryContent] = {

    implicit val reads = BinaryContent.reads
    implicit val classTag = ClassTag(BinaryContent.getClass)

    getIDResponse[BinaryContent](
      "PublicationId" -> publicationId, "BinaryId" -> binaryId, "VariantId" -> variantId
    )
  }

  /**
   * Get a [[org.talares.api.datatypes.items.BinaryVariant]] by it's publication ID and it's binary ID.
   *
   * @param publicationId the ID of the publication the desired [[org.talares.api.datatypes.items.BinaryVariant]]
   *                      belongs to
   * @param binaryId the binary ID of the desired [[org.talares.api.datatypes.items.BinaryVariant]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.BinaryVariant]]
   */
  def getBinaryVariant(publicationId: Int, binaryId: Int): Future[BinaryVariant] = {

    implicit val reads = BinaryVariant.reads
    implicit val classTag = ClassTag(BinaryVariant.getClass)

    getIDResponse[BinaryVariant]("PublicationId" -> publicationId, "BinaryId" -> binaryId)
  }

  /**
   * Get a [[org.talares.api.datatypes.items.Component]] by it's publication ID and it's item ID.
   *
   * @param publicationId the ID of the publication the desired [[org.talares.api.datatypes.items.Component]] belongs to
   * @param itemId the item ID of the desired [[org.talares.api.datatypes.items.Component]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.Component]]
   */
  def getComponent(publicationId: Int, itemId: Int): Future[Component] = {

    implicit val reads = Component.reads
    implicit val classTag = ClassTag(Component.getClass)

    getIDResponse[Component]("PublicationId" -> publicationId, "ItemId" -> itemId)
  }

  /**
   * Get a [[org.talares.api.datatypes.items.ComponentPresentation]] by it's publication ID, it's component ID and it's
   * template ID.
   *
   * @param publicationId the ID of the publication the desired
   *                      [[org.talares.api.datatypes.items.ComponentPresentation]] belongs to
   * @param componentId the component ID of the desired [[org.talares.api.datatypes.items.ComponentPresentation]]
   * @param templateId the template ID of the desired [[org.talares.api.datatypes.items.ComponentPresentation]]
   * @return a successful or failed Future of a
   *         [[org.talares.api.datatypes.items.ComponentPresentation]]
   */
  def getComponentPresentation(publicationId: Int, componentId: Int, templateId: Int): Future[ComponentPresentation] = {

    implicit val reads = ComponentPresentation.reads
    implicit val classTag = ClassTag(ComponentPresentation.getClass)

    getIDResponse[ComponentPresentation](
      "PublicationId" -> publicationId, "ComponentId" -> componentId, "TemplateId" -> templateId
    )
  }

  /**
   * Get a [[org.talares.api.datatypes.items.CustomMeta]] by it's ID.
   *
   * @param id the ID of the desired [[org.talares.api.datatypes.items.CustomMeta]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.CustomMeta]]
   */
  def getCustomMeta(id: Int): Future[CustomMeta] = {

    implicit val reads = CustomMeta.reads
    implicit val classTag = ClassTag(CustomMeta.getClass)

    getIDResponse[CustomMeta]("Id" -> id)
  }

  /**
   * Get a [[org.talares.api.datatypes.items.Keyword]] by it's ID, it's publication ID and it's taxonomy ID.
   *
   * @param publicationId the ID of the publication the desired [[org.talares.api.datatypes.items.Keyword]] belongs to
   * @param id the ID of the desired [[org.talares.api.datatypes.items.Keyword]]
   * @param taxonomyId the taxonomy ID of the desired [[org.talares.api.datatypes.items.Keyword]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.Keyword]]
   */
  def getKeyword(publicationId: Int, id: Int, taxonomyId: Int): Future[Keyword] = {

    implicit val reads = Keyword.reads
    implicit val classTag = ClassTag(Keyword.getClass)

    getIDResponse[Keyword]("PublicationId" -> publicationId, "Id" -> id, "TaxonomyId" -> taxonomyId)
  }

  /**
   * Get a [[org.talares.api.datatypes.items.Page]] by it's publication ID and it's item ID.
   *
   * @param publicationId the ID of the publication the desired [[org.talares.api.datatypes.items.Page]] belongs to
   * @param itemId the item ID of the desired [[org.talares.api.datatypes.items.Page]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.Page]]
   */
  def getPage(publicationId: Int, itemId: Int): Future[Page] = {

    implicit val reads = Page.reads
    implicit val classTag = ClassTag(Page.getClass)

    getIDResponse[Page]("PublicationId" -> publicationId, "ItemId" -> itemId)
  }

  /**
   * Get a [[org.talares.api.datatypes.items.Page]] by it's URL.
   *
   * @param url the URL of the [[org.talares.api.datatypes.items.Page]] within the Tridion CMS.
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.Page]]
   */
  def getPage(url: String): Future[Seq[Page]] = {

    implicit val reads = Page.reads
    implicit val classTag = ClassTag(Page.getClass)

    getSearchResponse[Page]("Url" -> url)
  }

  /**
   * Get a [[org.talares.api.datatypes.items.PageContent]] by it's publication ID and it's page ID.
   *
   * @param publicationId the ID of the publication the desired [[org.talares.api.datatypes.items.PageContent]] belongs
   *                      to
   * @param pageId the page ID of the desired [[org.talares.api.datatypes.items.PageContent]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.PageContent]]
   */
  def getPageContent(publicationId: Int, pageId: Int): Future[PageContent] = {

    implicit val reads = PageContent.reads
    implicit val classTag = ClassTag(PageContent.getClass)

    getIDResponse[PageContent]("PublicationId" -> publicationId, "PageId" -> pageId)
  }

  /**
   * Get a [[org.talares.api.datatypes.items.Publication]] by it's ID
   *
   * @param id the ID of the desired [[org.talares.api.datatypes.items.Publication]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.Publication]]
   */
  def getPublication(id: Int): Future[Publication] = {

    implicit val reads = Publication.reads
    implicit val classTag = ClassTag(Publication.getClass)

    getIDResponse[Publication]("Id" -> id)
  }

  /**
   * Get a [[org.talares.api.datatypes.items.Schema]] by it's publication ID and it's schema ID.
   *
   * @param publicationId the ID of the publication the desired [[org.talares.api.datatypes.items.Schema]] belongs to
   * @param schemaId the schema ID of the desired [[org.talares.api.datatypes.items.Schema]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.Schema]]
   */
  def getSchema(publicationId: Int, schemaId: Int): Future[Schema] = {

    implicit val reads = Schema.reads
    implicit val classTag = ClassTag(Schema.getClass)

    getIDResponse[Schema]("PublicationId" -> publicationId, "SchemaId" -> schemaId)
  }

  /**
   * Get a [[org.talares.api.datatypes.items.StructureGroup]] by it's ID and it's publication ID.
   *
   * @param publicationId the ID of the publication the desired [[org.talares.api.datatypes.items.StructureGroup]]
   *                      belongs to
   * @param id the ID of the desired [[org.talares.api.datatypes.items.StructureGroup]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.StructureGroup]]
   */
  def getStructureGroup(publicationId: Int, id: Int): Future[StructureGroup] = {

    implicit val reads = StructureGroup.reads
    implicit val classTag = ClassTag(StructureGroup.getClass)

    getIDResponse[StructureGroup]("PublicationId" -> publicationId, "Id" -> id)
  }

  /**
   * Get a [[org.talares.api.datatypes.items.Template]] by it's publication ID and it's item ID.
   *
   * @param publicationId the ID of the publication the desired [[org.talares.api.datatypes.items.Template]] belongs to
   * @param itemId the item ID of the desired [[org.talares.api.datatypes.items.Template]]
   * @return a successful or failed Future of a [[org.talares.api.datatypes.items.Template]]
   */
  def getTemplate(publicationId: Int, itemId: Int): Future[Template] = {

    implicit val reads = Template.reads
    implicit val classTag = ClassTag(Template.getClass)

    getIDResponse[Template]("PublicationId" -> publicationId, "ItemId" -> itemId)
  }
}

object Talares {

  @volatile private[Talares] var _currentApp: Talares = _

  /**
   * Returns an Option of the Talares instance that may or may not have been created.
   */
  private[Talares] def maybeApplication: Option[Talares] = Option(_currentApp)

  /**
   * Implicitly import the previously created Talares instance.
   */
  implicit def current: Talares = maybeApplication.getOrElse(sys.error("There is no Talares instance available"))

  /**
   * Either create a new or return the previously created Talares instance.
   *
   * @param cache the [[org.talares.api.cache.Cache]] instance to use
   * @return an instance of [[Talares]]
   */
  def apply(cache: Cache = NoCache()): Talares = maybeApplication getOrElse {
    _currentApp = new Talares(cache)
    _currentApp
  }
}

/**
 * Convenience class holding all configuration values the library could need.
 *
 * The settings are parsed from an .conf file present in the class path. Essential settings are checked for their
 * presence and an IllegalArgumentException will be thrown if they are not.
 *
 * @see com.typesafe.config.ConfigFactory
 */
sealed case class Settings() {
  val config = ConfigFactory.load()
  val cacheOnFailure = config.getBoolean("talares.cache.storefailure")
  val cacheRefreshRatio = config.getInt("talares.cache.refreshratio")
  val retries = config.getInt("talares.retries")
  val timeout = config.getLong("talares.timeout")
  val webserviceLocation = config.getString("talares.webservicelocation")
  require(
    webserviceLocation.toString.startsWith("http://") || webserviceLocation.toString.startsWith("https://"),
    "The webservice location must include the appropriate protocol"
  )
  require(webserviceLocation.toString.endsWith("odata.svc"), "The webservice location must end with odata.svc")
}