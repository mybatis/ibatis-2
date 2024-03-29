/*
 * Copyright 2004-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibatis.sqlmap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Book;
import testdomain.Document;
import testdomain.Magazine;
import testdomain.PersonDocument;

class DiscriminatorTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/docs-init.sql");
  }

  @Test
  void testDiscriminator() throws Exception {

    List<?> list = sqlMap.queryForList("getDocuments", null);
    assertEquals(6, list.size());

    assertTrue(list.get(0) instanceof Book);
    assertTrue(list.get(1) instanceof Magazine);
    assertTrue(list.get(2) instanceof Book);
    assertTrue(list.get(3) instanceof Magazine);
    assertTrue(list.get(4) instanceof Document);
    assertTrue(list.get(5) instanceof Document);

    assertEquals(1, ((Document) list.get(0)).getId());
    assertEquals(2, ((Document) list.get(1)).getId());
    assertEquals(3, ((Document) list.get(2)).getId());
    assertEquals(4, ((Document) list.get(3)).getId());
    assertEquals(5, ((Document) list.get(4)).getId());
    assertEquals(6, ((Document) list.get(5)).getId());

    assertEquals(Integer.valueOf(55), ((Book) list.get(0)).getPages());
    assertEquals("Lyon", ((Magazine) list.get(1)).getCity());
    assertEquals(Integer.valueOf(3587), ((Book) list.get(2)).getPages());
    assertEquals("Paris", ((Magazine) list.get(3)).getCity());
  }

  @Test
  void testDiscriminatorInNestedResultMap() throws Exception {
    List<?> list = sqlMap.queryForList("getPersonDocuments");
    assertEquals(3, list.size());

    assertTrue(((PersonDocument) list.get(0)).getFavoriteDocument() instanceof Magazine);
    assertTrue(((PersonDocument) list.get(1)).getFavoriteDocument() instanceof Book);
    assertTrue(((PersonDocument) list.get(2)).getFavoriteDocument() instanceof Document);

  }

  @Test
  void testDiscriminatorWithNestedResultMap() throws Exception {
    List<?> list = sqlMap.queryForList("getDocumentsWithAttributes");
    assertEquals(6, list.size());

    assertTrue(list.get(0) instanceof Book);
    Book b = (Book) list.get(0);
    assertEquals(2, b.getAttributes().size());

    assertTrue(list.get(1) instanceof Magazine);
    Magazine m = (Magazine) list.get(1);
    assertEquals(1, m.getAttributes().size());

    assertTrue(list.get(2) instanceof Book);
    b = (Book) list.get(2);
    assertEquals(2, b.getAttributes().size());

    Document d = (Document) list.get(3);
    assertEquals(0, d.getAttributes().size());

    d = (Document) list.get(4);
    assertEquals(0, d.getAttributes().size());

    d = (Document) list.get(5);
    assertEquals(0, d.getAttributes().size());
  }
}
