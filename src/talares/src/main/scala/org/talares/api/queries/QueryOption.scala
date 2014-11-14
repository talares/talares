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
package org.talares.api.queries

import org.talares.api.queries.Filter.AndOr
import org.talares.api.queries.Operator.Operator

/**
 * Represents part(s) of a OData query that are optional and mutate the result in a specific way.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
trait QueryOption {

  val name: String
  val value: String

  override def toString: String = '$' + name + '=' + value
}

/**
 * Represents a [[QueryOption]] that filters a result according to a given set of arguments.
 *
 * @param andOr whether to AND or OR the filter arguments
 * @param args the [[Operation]]'s that make up the args
 */
case class Filter(andOr: AndOr, args: Seq[Operation]) extends QueryOption {

  override val name = "filter"

  override lazy val value =
    args.foldLeft("") {
      case (acc, op) =>
        if (acc.isEmpty) op.toString
        else acc + "%20" + andOr.toString + "%20" + op.toString
    }

  def /(operator: Operation): Filter = Filter(andOr, args ++ Seq(operator))
}

/**
 * Represents a specific operation to be included in a [[QueryOption]].
 *
 * @param key the key of the operation
 * @param operator the operator of the operation
 * @param value the value of the operation
 */
case class Operation(key: String, operator: Operator, value: Any) {

  private def isNumeric(str: String): Boolean = {
    !throwsNumberFormatException(str.toLong) || !throwsNumberFormatException(str.toDouble)
  }

  private def throwsNumberFormatException(f: => Any): Boolean = {
    try {
      f; false
    } catch {
      case e: NumberFormatException => true
    }
  }

  override def toString: String = {
    val formattedValue =
      if (isNumeric(value.toString)) value
      else s"'$value'"
    s"$key%20$operator%20$formattedValue"
  }
}

/**
 * Operators for specifying [[Operation]]'s.
 */
object Operator extends Enumeration {
  type Operator = Value
  val Eq = Value("eq")
  val Gt = Value("gt")
  val Ge = Value("ge")
  val Le = Value("le")
  val Lt = Value("lt")
  val Ne = Value("ne")
}

object Filter extends Enumeration {

  type AndOr = Value
  val And = Value("and")
  val Or = Value("or")

  def apply(andOr: AndOr, args: Operation): Filter = Filter(andOr, Seq(args))

  def apply(args: Operation*): Filter = Filter(And, args)
}