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

import org.talares.api.queries.Filter._

/**
 * Holds implicit conversion classes and functions for working with [[org.talares.api.queries.Query]]'s.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
package object queries {

  /**
   * Takes an [[Operation]] and a implicit AndOr and turns it into a [[QueryOption]].
   *
   * @param operator the [[Operation]] to be transformed
   * @param andOr whether to AND or OR the given [[Operation]]
   * @return an instance of [[QueryOption]] which represents the conditions expressed by the [[Operation]] and AndOr
   */
  implicit def operatorToFilter(operator: Operation)(implicit andOr: AndOr = And): QueryOption = Filter(andOr, operator)

  /**
   * Holds functions for transforming key value pairs of String to Any into [[Operation]]'s.
   *
   * @param key the operator key
   */
  implicit class QueryOps(key: String) {

    /** Creates an 'equal to' [[Operation]] **/
    def ==|(value: Any): Operation = Operation(key, Operator.Eq, value)

    /** Creates a 'not equal to' [[Operation]] **/
    def !=|(value: Any): Operation = Operation(key, Operator.Ne, value)

    /** Creates a 'greater then' [[Operation]] **/
    def >|(value: Any): Operation = Operation(key, Operator.Gt, value)

    /** Creates a 'greater then or equal to' [[Operation]] **/
    def >=|(value: Any): Operation = Operation(key, Operator.Ge, value)

    /** Creates a 'less then' [[Operation]] **/
    def <|(value: Any): Operation = Operation(key, Operator.Lt, value)

    /** Creates a 'less then or equal to' [[Operation]] **/
    def <=|(value: Any): Operation = Operation(key, Operator.Le, value)
  }

  /**
   * Turns two [[Operation]]'s into a [[Filter]] by either AND'ing or OR'ing them together.
   *
   * @param operator the first operator
   */
  implicit class FilterOps(operator: Operation) {

    /**
     * Creates a [[Filter]] by AND'ing two [[Operation]]'s together.
     *
     * @param otherOperator the [[Operation]] to AND to the first [[Operation]]
     * @return an instance of [[Filter]] which represents the conditions expressed by the AND'ing of both [[Operation]]'s
     */
    def &&(otherOperator: Operation): Filter = Filter(And, Seq(operator, otherOperator))

    /**
     * Creates a [[Filter]] by OR'ing two [[Operation]]'s together.
     *
     * @param otherOperator the [[Operation]] to OR to the first [[Operation]]
     * @return an instance of [[Filter]] which represents the conditions expressed by the OR'ing of both [[Operation]]'s
     */
    def ||(otherOperator: Operation): Filter = Filter(Or, Seq(operator, otherOperator))
  }

}