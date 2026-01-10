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

import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaginatedArrayListTest {

  private PaginatedArrayList smallPageList;
  private PaginatedArrayList oddPageList;
  private PaginatedArrayList evenPageList;

  @BeforeEach
  void setUp() {
    this.smallPageList = new PaginatedArrayList(5);
    this.smallPageList.add(Integer.valueOf(0));
    this.smallPageList.add(Integer.valueOf(1));
    this.smallPageList.add(Integer.valueOf(2));

    this.oddPageList = new PaginatedArrayList(5);
    this.oddPageList.add(Integer.valueOf(0));
    this.oddPageList.add(Integer.valueOf(1));
    this.oddPageList.add(Integer.valueOf(2));
    this.oddPageList.add(Integer.valueOf(3));
    this.oddPageList.add(Integer.valueOf(4));
    this.oddPageList.add(Integer.valueOf(5));
    this.oddPageList.add(Integer.valueOf(6));
    this.oddPageList.add(Integer.valueOf(7));
    this.oddPageList.add(Integer.valueOf(8));
    this.oddPageList.add(Integer.valueOf(9));
    this.oddPageList.add(Integer.valueOf(10));
    this.oddPageList.add(Integer.valueOf(11));
    this.oddPageList.add(Integer.valueOf(12));
    this.oddPageList.add(Integer.valueOf(13));
    this.oddPageList.add(Integer.valueOf(14));
    this.oddPageList.add(Integer.valueOf(15));
    this.oddPageList.add(Integer.valueOf(16));
    this.oddPageList.add(Integer.valueOf(17));

    this.evenPageList = new PaginatedArrayList(5);
    this.evenPageList.add(Integer.valueOf(0));
    this.evenPageList.add(Integer.valueOf(1));
    this.evenPageList.add(Integer.valueOf(2));
    this.evenPageList.add(Integer.valueOf(3));
    this.evenPageList.add(Integer.valueOf(4));
    this.evenPageList.add(Integer.valueOf(5));
    this.evenPageList.add(Integer.valueOf(6));
    this.evenPageList.add(Integer.valueOf(7));
    this.evenPageList.add(Integer.valueOf(8));
    this.evenPageList.add(Integer.valueOf(9));
    this.evenPageList.add(Integer.valueOf(10));
    this.evenPageList.add(Integer.valueOf(11));
    this.evenPageList.add(Integer.valueOf(12));
    this.evenPageList.add(Integer.valueOf(13));
    this.evenPageList.add(Integer.valueOf(14));

  }

  @Test
  void testOddPaginatedIterator() {

    Assertions.assertEquals(true, this.oddPageList.isFirstPage());
    Assertions.assertEquals(false, this.oddPageList.isPreviousPageAvailable());

    int count = 0;
    Iterator<?> i = this.oddPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    Assertions.assertEquals(5, count);

    this.oddPageList.nextPage();

    count = 0;
    i = this.oddPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    Assertions.assertEquals(5, count);

    this.oddPageList.nextPage();

    Assertions.assertEquals(true, this.oddPageList.isMiddlePage());

    count = 0;
    i = this.oddPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    Assertions.assertEquals(5, count);

    this.oddPageList.nextPage();

    count = 0;
    i = this.oddPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    Assertions.assertEquals(3, count);

    Assertions.assertEquals(true, this.oddPageList.isLastPage());
    Assertions.assertEquals(false, this.oddPageList.isNextPageAvailable());

    this.oddPageList.nextPage();

    Assertions.assertEquals(true, this.oddPageList.isLastPage());
    Assertions.assertEquals(false, this.oddPageList.isNextPageAvailable());

    this.oddPageList.previousPage();

    Assertions.assertEquals(Integer.valueOf(10), this.oddPageList.get(0));
    Assertions.assertEquals(Integer.valueOf(12), this.oddPageList.get(2));

    this.oddPageList.gotoPage(500);

    Assertions.assertEquals(Integer.valueOf(0), this.oddPageList.get(0));
    Assertions.assertEquals(Integer.valueOf(4), this.oddPageList.get(4));

    this.oddPageList.gotoPage(-500);

    Assertions.assertEquals(Integer.valueOf(15), this.oddPageList.get(0));
    Assertions.assertEquals(Integer.valueOf(17), this.oddPageList.get(2));
  }

  @Test
  void testEvenPaginatedIterator() {

    Assertions.assertEquals(true, this.evenPageList.isFirstPage());
    Assertions.assertEquals(false, this.evenPageList.isPreviousPageAvailable());

    int count = 0;
    Iterator<?> i = this.evenPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    Assertions.assertEquals(5, count);

    this.evenPageList.nextPage();

    Assertions.assertEquals(true, this.evenPageList.isMiddlePage());

    count = 0;
    i = this.evenPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    Assertions.assertEquals(5, count);

    this.evenPageList.nextPage();

    count = 0;
    i = this.evenPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    Assertions.assertEquals(5, count);

    Assertions.assertEquals(true, this.evenPageList.isLastPage());
    Assertions.assertEquals(false, this.evenPageList.isNextPageAvailable());

    this.evenPageList.nextPage();

    Assertions.assertEquals(Integer.valueOf(10), this.evenPageList.get(0));
    Assertions.assertEquals(Integer.valueOf(14), this.evenPageList.get(4));

    this.evenPageList.previousPage();

    Assertions.assertEquals(Integer.valueOf(5), this.evenPageList.get(0));
    Assertions.assertEquals(Integer.valueOf(9), this.evenPageList.get(4));

    this.evenPageList.gotoPage(500);

    Assertions.assertEquals(Integer.valueOf(0), this.evenPageList.get(0));
    Assertions.assertEquals(Integer.valueOf(4), this.evenPageList.get(4));

    this.evenPageList.gotoPage(-500);

    Assertions.assertEquals(Integer.valueOf(10), this.evenPageList.get(0));
    Assertions.assertEquals(Integer.valueOf(14), this.evenPageList.get(4));
  }

  @Test
  void testSmallPaginatedIterator() {

    Assertions.assertEquals(true, this.smallPageList.isFirstPage());
    Assertions.assertEquals(true, this.smallPageList.isLastPage());
    Assertions.assertEquals(false, this.smallPageList.isMiddlePage());
    Assertions.assertEquals(false, this.smallPageList.isPreviousPageAvailable());
    Assertions.assertEquals(false, this.smallPageList.isNextPageAvailable());

    int count = 0;
    Iterator<?> i = this.smallPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    Assertions.assertEquals(3, count);

    this.smallPageList.nextPage();

    count = 0;
    i = this.smallPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    Assertions.assertEquals(3, count);

    Assertions.assertEquals(true, this.smallPageList.isFirstPage());
    Assertions.assertEquals(true, this.smallPageList.isLastPage());
    Assertions.assertEquals(false, this.smallPageList.isMiddlePage());
    Assertions.assertEquals(false, this.smallPageList.isPreviousPageAvailable());
    Assertions.assertEquals(false, this.smallPageList.isNextPageAvailable());

    this.smallPageList.nextPage();

    count = 0;
    i = this.smallPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    Assertions.assertEquals(3, count);

    this.smallPageList.nextPage();

    Assertions.assertEquals(Integer.valueOf(0), this.smallPageList.get(0));
    Assertions.assertEquals(Integer.valueOf(2), this.smallPageList.get(2));

    this.smallPageList.previousPage();

    Assertions.assertEquals(Integer.valueOf(0), this.smallPageList.get(0));
    Assertions.assertEquals(Integer.valueOf(2), this.smallPageList.get(2));

    this.smallPageList.gotoPage(500);

    Assertions.assertEquals(Integer.valueOf(0), this.smallPageList.get(0));
    Assertions.assertEquals(Integer.valueOf(2), this.smallPageList.get(2));

    this.smallPageList.gotoPage(-500);

    Assertions.assertEquals(Integer.valueOf(0), this.smallPageList.get(0));
    Assertions.assertEquals(Integer.valueOf(2), this.smallPageList.get(2));

    Assertions.assertEquals(true, this.smallPageList.isFirstPage());
    Assertions.assertEquals(true, this.smallPageList.isLastPage());
    Assertions.assertEquals(false, this.smallPageList.isMiddlePage());
    Assertions.assertEquals(false, this.smallPageList.isPreviousPageAvailable());
    Assertions.assertEquals(false, this.smallPageList.isNextPageAvailable());
  }
}
