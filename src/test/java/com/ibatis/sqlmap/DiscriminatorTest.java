/*
 * Copyright 2004-2026 the original author or authors.
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

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Book;
import testdomain.Document;
import testdomain.Magazine;
import testdomain.PersonDocument;

class DiscriminatorTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    BaseSqlMap.initScript("scripts/docs-init.sql");
  }

  @Test
  void testDiscriminator() throws Exception {

    final List<?> list = BaseSqlMap.sqlMap.queryForList("getDocuments", null);
    Assertions.assertEquals(6, list.size());

    Assertions.assertTrue(list.get(0) instanceof Book);
    Assertions.assertTrue(list.get(1) instanceof Magazine);
    Assertions.assertTrue(list.get(2) instanceof Book);
    Assertions.assertTrue(list.get(3) instanceof Magazine);
    Assertions.assertTrue(list.get(4) instanceof Document);
    Assertions.assertTrue(list.get(5) instanceof Document);

    Assertions.assertEquals(1, ((Document) list.get(0)).getId());
    Assertions.assertEquals(2, ((Document) list.get(1)).getId());
    Assertions.assertEquals(3, ((Document) list.get(2)).getId());
    Assertions.assertEquals(4, ((Document) list.get(3)).getId());
    Assertions.assertEquals(5, ((Document) list.get(4)).getId());
    Assertions.assertEquals(6, ((Document) list.get(5)).getId());

    Assertions.assertEquals(Integer.valueOf(55), ((Book) list.get(0)).getPages());
    Assertions.assertEquals("Lyon", ((Magazine) list.get(1)).getCity());
    Assertions.assertEquals(Integer.valueOf(3587), ((Book) list.get(2)).getPages());
    Assertions.assertEquals("Paris", ((Magazine) list.get(3)).getCity());
  }

  @Test
  void testDiscriminatorInNestedResultMap() throws Exception {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getPersonDocuments");
    Assertions.assertEquals(3, list.size());

    Assertions.assertTrue(((PersonDocument) list.get(0)).getFavoriteDocument() instanceof Magazine);
    Assertions.assertTrue(((PersonDocument) list.get(1)).getFavoriteDocument() instanceof Book);
    Assertions.assertTrue(((PersonDocument) list.get(2)).getFavoriteDocument() instanceof Document);

  }

  @Test
  void testDiscriminatorWithNestedResultMap() throws Exception {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getDocumentsWithAttributes");
    Assertions.assertEquals(6, list.size());

    Assertions.assertTrue(list.get(0) instanceof Book);
    Book b = (Book) list.get(0);
    Assertions.assertEquals(2, b.getAttributes().size());

    Assertions.assertTrue(list.get(1) instanceof Magazine);
    final Magazine m = (Magazine) list.get(1);
    Assertions.assertEquals(1, m.getAttributes().size());

    Assertions.assertTrue(list.get(2) instanceof Book);
    b = (Book) list.get(2);
    Assertions.assertEquals(2, b.getAttributes().size());

    Document d = (Document) list.get(3);
    Assertions.assertEquals(0, d.getAttributes().size());

    d = (Document) list.get(4);
    Assertions.assertEquals(0, d.getAttributes().size());

    d = (Document) list.get(5);
    Assertions.assertEquals(0, d.getAttributes().size());
  }
}
