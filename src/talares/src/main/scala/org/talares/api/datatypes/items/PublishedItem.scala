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
package org.talares.api.datatypes.items

import org.joda.time.DateTime

/**
 * Items of this type represent versioned pieces of content.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
trait PublishedItem extends Item {

  val itemId: Int
  val title: Option[String]
  val author: Option[String]
  val creationDate: Option[DateTime]
  val initialPublishDate: Option[DateTime]
  val lastPublishDate: Option[DateTime]
  val modificationDate: Option[DateTime]
  val majorVersion: Option[Int]
  val minorVersion: Option[Int]
  val owningPublication: Option[Int]
}