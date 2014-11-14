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
package org.talares.api.cache

/**
 * Container class holding values to be cached.
 * These values are accompanied by meta data, supplying additional information about an item in regards to the cache.
 *
 * @author Dennis Vis
 * @since 0.1.0
 */
case class CacheItem(value: Any, timesAccessed: Int = 0)