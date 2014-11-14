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
package org.talares.datatypes.items;

import org.joda.time.DateTime;

/**
 * Items of this type represent versioned pieces of content.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
public abstract class PublishedItem extends Item {

  private final int itemId;
  private final String title;
  private final String author;
  private final DateTime creationDate;
  private final DateTime initialPublishDate;
  private final DateTime lastPublishDate;
  private final DateTime modificationDate;
  private final Integer majorVersion;
  private final Integer minorVersion;
  private final Integer owningPublication;

  public PublishedItem(final org.talares.api.Talares api,
                       final org.talares.api.datatypes.items.PublishedItem scalaPublishedItem) {
    super(api, scalaPublishedItem);
    this.itemId = scalaPublishedItem.itemId();
    this.title = asJava(scalaPublishedItem.title());
    this.author = asJava(scalaPublishedItem.author());
    this.creationDate = asJava(scalaPublishedItem.creationDate());
    this.initialPublishDate = asJava(scalaPublishedItem.initialPublishDate());
    this.lastPublishDate = asJava(scalaPublishedItem.lastPublishDate());
    this.modificationDate = asJava(scalaPublishedItem.modificationDate());
    this.majorVersion = asJava(scalaPublishedItem.majorVersion());
    this.minorVersion = asJava(scalaPublishedItem.minorVersion());
    this.owningPublication = asJava(scalaPublishedItem.owningPublication());
  }

  public final int getItemId() {
    return itemId;
  }

  public final String getTitle() {
    return title;
  }

  public final String getAuthor() {
    return author;
  }

  public final DateTime getCreationDate() {
    return creationDate;
  }

  public final DateTime getInitialPublishDate() {
    return initialPublishDate;
  }

  public final DateTime getLastPublishDate() {
    return lastPublishDate;
  }

  public final DateTime getModificationDate() {
    return modificationDate;
  }

  public final Integer getMajorVersion() {
    return majorVersion;
  }

  public final Integer getMinorVersion() {
    return minorVersion;
  }

  public final Integer getOwningPublication() {
    return owningPublication;
  }
}