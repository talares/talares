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
package org.talares.api.actors

import akka.event.LoggingReceive
import dispatch.{Http, enrichFuture, url}
import org.talares.api.Talares
import org.talares.api.actors.messages.ExecutorMessages.{Result, Execute}
import org.talares.api.actors.messages.FetcherMessages
import org.talares.api.as
import org.talares.api.exceptions.ServiceErrorException
import play.api.libs.json.{JsArray, JsValue}

import scala.concurrent.Future

/**
 * Responsible for calling a webservice and parsing it's response into an instance of JsValue.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
class Executor[T](val app: Talares) extends BaseActor {

  import context.parent

  /**
   * Adds a parameter to the query string which should ensure the webservice will respond in Json format.
   *
   * @param url the URL to append the parameter to
   * @return the mutated URL
   */
  def addJsonParam(url: String): String =
    if (url.contains("?$")) url + "&$format=json"
    else url + "?$format=json"

  /**
   * Parses the appropriate parts from a JsValue.
   *
   * In practice this means whenever multiple values are detected within a 'result' node, a JsArray is returned
   * containing said values.
   * Otherwise the single value from the 'd' node is returned as a single JsValue.
   *
   * @param node the JsValue to parse
   * @return the parsed JsValue
   */
  def parseJsonResult(node: JsValue): JsValue = {
    val d = node \ "d"
    val results = d \ "results"
    results match {
      case results: JsArray => results
      case _ => d
    }
  }

  /**
   * Responsible for calling the webservice.
   *
   * Parses the webservice response and extracts an instance of JsValue from it.
   *
   * @param location the complete URL from which the response should be fetched (should include the json format
   *                 parameter)
   * @return a Future of the fetched and parsed JsValue
   */
  def callService(location: String): Future[Either[Throwable, JsValue]] = {

    val timeout = Settings.timeout.toInt

    val request = url(location)
    val client = Http.configure(_
      .setConnectionTimeoutInMs(timeout)
      .setIdleConnectionTimeoutInMs(timeout)
    )
    val response = client(request OK as.Json).either

    response map {
      case Left(error) => Left(ServiceErrorException(location, error))
      case Right(result) =>
        log.debug( s"""
          |Received response from service:
          |Location: $location
          |Body:
          |$result
          |""".stripMargin)
        Right(parseJsonResult(result))
    }
  }

  /**
   * Executes a [[org.talares.api.actors.messages.FetcherMessages.Task]].
   *
   * Takes a complete URL without the Json format parameter, adds this parameter by means of addJsonParam(), calls
   * the webservice by means of callService() and parses the desired Json node(s) from the result using
   * parseJsonResult(). This results in a Future of JsValue which is sent back to the parent.
   *
   * @param url the complete URL including parameters, excluding the json format parameter
   */
  def execute(fetcherTask: FetcherMessages.Task[_], url: String): Unit = {
    val modifiedUrl = addJsonParam(url)
    callService(modifiedUrl) map { serviceResult =>
      parent ! Result(fetcherTask, serviceResult)
    }
  }

  def receive: Receive = LoggingReceive {
    case Execute(fetcherTask, url) => execute(fetcherTask, url)
  }
}