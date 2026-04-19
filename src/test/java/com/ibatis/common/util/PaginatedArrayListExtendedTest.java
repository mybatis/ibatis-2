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
package com.ibatis.common.util;

import java.util.List;
import java.util.ListIterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Extends coverage of {@link PaginatedArrayList} for the collection-mutator and list-accessor methods not exercised by
 * the existing tests.
 */
class PaginatedArrayListExtendedTest {

  @Test
  void shouldSupportAlternativeConstructors() {
    // (initialCapacity, pageSize) constructor
    PaginatedArrayList pal = new PaginatedArrayList(20, 5);
    Assertions.assertTrue(pal.isEmpty());

    // (Collection, pageSize) constructor
    List<String> seed = List.of("a", "b", "c", "d", "e", "f");
    PaginatedArrayList fromCollection = new PaginatedArrayList(seed, 3);
    Assertions.assertEquals(3, fromCollection.size());
    Assertions.assertEquals("a", fromCollection.get(0));
  }

  @Test
  void shouldSupportToArrayOperations() {
    PaginatedArrayList pal = buildList(3);
    Object[] arr = pal.toArray();
    Assertions.assertEquals(3, arr.length);

    Object[] arr2 = pal.toArray(new Object[0]);
    Assertions.assertEquals(3, arr2.length);
  }

  @Test
  void shouldSupportContainsAll() {
    PaginatedArrayList pal = buildList(5);
    Assertions.assertTrue(pal.containsAll(List.of("item0", "item1")));
    Assertions.assertFalse(pal.containsAll(List.of("item0", "absent")));
  }

  @Test
  void shouldSupportAddAllCollection() {
    PaginatedArrayList pal = new PaginatedArrayList(3);
    pal.addAll(List.of("x", "y", "z"));
    Assertions.assertEquals(3, pal.size());
  }

  @Test
  void shouldSupportAddAllAtIndex() {
    PaginatedArrayList pal = new PaginatedArrayList(3);
    pal.add("a");
    pal.add("b");
    pal.addAll(1, List.of("m", "n"));
    Assertions.assertEquals(3, pal.size()); // page still has pageSize items
  }

  @Test
  void shouldSupportRemoveAll() {
    PaginatedArrayList pal = buildList(5);
    boolean changed = pal.removeAll(List.of("item0", "item1"));
    Assertions.assertTrue(changed);
    Assertions.assertFalse(pal.contains("item0"));
  }

  @Test
  void shouldSupportRetainAll() {
    PaginatedArrayList pal = buildList(5);
    pal.retainAll(List.of("item0", "item1"));
    Assertions.assertTrue(pal.contains("item0"));
    Assertions.assertFalse(pal.contains("item4"));
  }

  @Test
  void shouldSupportSet() {
    PaginatedArrayList pal = buildList(3);
    pal.set(0, "replaced");
    Assertions.assertEquals("replaced", pal.get(0));
  }

  @Test
  void shouldSupportAddAtIndex() {
    PaginatedArrayList pal = buildList(3);
    pal.add(0, "inserted");
    Assertions.assertEquals("inserted", pal.get(0));
  }

  @Test
  void shouldSupportRemoveAtIndex() {
    PaginatedArrayList pal = buildList(3);
    Object removed = pal.remove(0);
    Assertions.assertEquals("item0", removed);
  }

  @Test
  void shouldSupportIndexOf() {
    PaginatedArrayList pal = buildList(3);
    Assertions.assertEquals(0, pal.indexOf("item0"));
    Assertions.assertEquals(-1, pal.indexOf("absent"));
  }

  @Test
  void shouldSupportLastIndexOf() {
    PaginatedArrayList pal = new PaginatedArrayList(5);
    pal.add("a");
    pal.add("b");
    pal.add("a");
    Assertions.assertEquals(2, pal.lastIndexOf("a"));
  }

  @Test
  void shouldSupportListIterators() {
    PaginatedArrayList pal = buildList(3);
    ListIterator<?> it = pal.listIterator();
    Assertions.assertTrue(it.hasNext());

    ListIterator<?> it2 = pal.listIterator(1);
    Assertions.assertTrue(it2.hasNext());
  }

  @Test
  void shouldSupportSubList() {
    PaginatedArrayList pal = buildList(5);
    List<?> sub = pal.subList(0, 2);
    Assertions.assertEquals(2, sub.size());
  }

  @Test
  void shouldReturnPageIndex() {
    PaginatedArrayList pal = buildList(10); // 10 items, pageSize 5, 2 pages
    Assertions.assertEquals(0, pal.getPageIndex());
    pal.nextPage();
    Assertions.assertEquals(1, pal.getPageIndex());
  }

  @Test
  void shouldReportPageSize() {
    PaginatedArrayList pal = new PaginatedArrayList(5);
    Assertions.assertEquals(5, pal.getPageSize());
  }

  // --------------------------------------------------------------------------

  private static PaginatedArrayList buildList(int items) {
    PaginatedArrayList pal = new PaginatedArrayList(5);
    for (int i = 0; i < items; i++) {
      pal.add("item" + i);
    }
    return pal;
  }
}
