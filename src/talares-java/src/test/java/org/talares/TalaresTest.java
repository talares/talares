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
package org.talares;

import org.junit.AfterClass;
import org.junit.Test;
import org.talares.mock.MockTalares;
import org.talares.datatypes.items.Template;
import org.talares.api.exceptions.TalaresException;
import org.talares.cache.AutoUpdateCache;
import org.talares.cache.SimpleCache;
import org.talares.datatypes.items.Binary;
import org.talares.datatypes.items.BinaryContent;
import org.talares.datatypes.items.BinaryVariant;
import org.talares.datatypes.items.Component;
import org.talares.datatypes.items.ComponentPresentation;
import org.talares.datatypes.items.CustomMeta;
import org.talares.datatypes.items.Keyword;
import org.talares.datatypes.items.Page;
import org.talares.datatypes.items.PageContent;
import org.talares.datatypes.items.Publication;
import org.talares.datatypes.items.Schema;
import org.talares.datatypes.items.StructureGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Dennis Vis
 * @since 0.1.0
 */
public class TalaresTest {

  private static final Talares TALARES;
  private static final Talares SC_TALARES;
  private static final Talares AU_TALARES;

  private static final SimpleCache SIMPLE_CACHE = new SimpleCache() {

    private final Map<Object, Object> cache = new HashMap<>();

    @Override
    public void put(Object key, Object value) {
      cache.put(key, value);
    }

    @Override
    public Object get(Object key) {
      return cache.get(key);
    }
  };

  private static final AutoUpdateCache AUTO_UPDATE_CACHE = new AutoUpdateCache() {

    private final Map<Object, Object> cache = new HashMap<>();

    @Override
    public void put(Object key, Object value) {
      cache.put(key, value);
    }

    @Override
    public Object get(Object key) {
      return cache.get(key);
    }
  };

  static {
    TALARES = new MockTalares();
    SC_TALARES = new MockTalares(SIMPLE_CACHE);
    AU_TALARES = new MockTalares(AUTO_UPDATE_CACHE);
  }

  @Test
  public void testGetBinary() throws TalaresException {

    final Binary binary = TALARES.getBinary_(1, 2);

    assertFalse(binary == null);
    assertTrue(binary.getBinaryId() == 123);
  }

  @Test
  public void testGetBinaryContent() throws TalaresException {

    final BinaryContent binary = TALARES.getBinaryContent_(1, 2, "3");

    assertFalse(binary == null);
    assertTrue(binary.getBinaryId() == 123);
  }

  @Test
  public void testGetBinaryVariant() throws TalaresException {

    final BinaryVariant binary = TALARES.getBinaryVariant_(1, 2);

    assertFalse(binary == null);
    assertTrue(binary.getBinaryId() == 123);
  }

  @Test
  public void testGetComponent() throws TalaresException {

    final Component component = TALARES.getComponent_(1, 2);

    assertFalse(component == null);
    assertTrue(component.getItemId() == 123);
  }

  @Test
  public void testGetComponentPresentation() throws TalaresException {

    final ComponentPresentation componentPresentation = TALARES.getComponentPresentation_(1, 2, 3);

    assertFalse(componentPresentation == null);
    assertTrue(componentPresentation.getComponentId() == 123);
  }

  @Test
  public void testGetCustomMeta() throws TalaresException {

    final CustomMeta customMeta = TALARES.getCustomMeta_(1);

    assertFalse(customMeta == null);
    assertTrue(customMeta.getItemId() == 123);
  }

  @Test
  public void testGetKeyword() throws TalaresException {

    final Keyword keyword = TALARES.getKeyword_(1, 2, 3);

    assertFalse(keyword == null);
    assertTrue(keyword.getTaxonomyId() == 123);
  }

  @Test
  public void testGetPage() throws TalaresException {

    final Page page = TALARES.getPage_(1, 2);

    assertFalse(page == null);
    assertTrue(page.getTemplateId() == 123);
  }

  @Test
  public void testGetPageByURL() throws TalaresException {

    final List<Page> pages = TALARES.getPage_("/path/of/page");

    assertFalse(pages == null);
    assertFalse(pages.isEmpty());

    final Page page = pages.get(0);
    assertTrue(page.getTitle() != null);
  }

  @Test
  public void testGetPageContent() throws TalaresException {

    final PageContent pageContent = TALARES.getPageContent_(1, 2);

    assertFalse(pageContent == null);
    assertTrue(pageContent.getPageId() == 123);
  }

  @Test
  public void testGetPublication() throws TalaresException {

    final Publication publication = TALARES.getPublication_(1);

    assertFalse(publication == null);
    assertTrue(publication.getId() == 1);
  }

  @Test
  public void testGetSchema() throws TalaresException {

    final Schema schema = TALARES.getSchema_(1, 2);

    assertFalse(schema == null);
    assertTrue(schema.getSchemaId() == 123);
  }

  @Test
  public void testGetStructureGroup() throws TalaresException {

    final StructureGroup structureGroup = TALARES.getStructureGroup_(1, 2);

    assertFalse(structureGroup == null);
    assertTrue(structureGroup.getId() == 123);
  }

  @Test
  public void testGetTemplate() throws TalaresException {

    final Template template = TALARES.getTemplate_(1, 2);

    assertFalse(template == null);
    assertTrue(template.getItemId() == 123);
  }

  @AfterClass
  public static void shutDown() {
    TALARES.terminate();
    SC_TALARES.terminate();
    AU_TALARES.terminate();
  }
}