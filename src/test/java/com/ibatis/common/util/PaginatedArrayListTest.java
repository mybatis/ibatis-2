/**
 * Copyright 2004-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibatis.common.util;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class PaginatedArrayListTest {

  private PaginatedArrayList smallPageList;
  private PaginatedArrayList oddPageList;
  private PaginatedArrayList evenPageList;

  @Before
  public void setUp() {
    smallPageList = new PaginatedArrayList(5);
    smallPageList.add(Integer.valueOf(0));
    smallPageList.add(Integer.valueOf(1));
    smallPageList.add(Integer.valueOf(2));

    oddPageList = new PaginatedArrayList(5);
    oddPageList.add(Integer.valueOf(0));
    oddPageList.add(Integer.valueOf(1));
    oddPageList.add(Integer.valueOf(2));
    oddPageList.add(Integer.valueOf(3));
    oddPageList.add(Integer.valueOf(4));
    oddPageList.add(Integer.valueOf(5));
    oddPageList.add(Integer.valueOf(6));
    oddPageList.add(Integer.valueOf(7));
    oddPageList.add(Integer.valueOf(8));
    oddPageList.add(Integer.valueOf(9));
    oddPageList.add(Integer.valueOf(10));
    oddPageList.add(Integer.valueOf(11));
    oddPageList.add(Integer.valueOf(12));
    oddPageList.add(Integer.valueOf(13));
    oddPageList.add(Integer.valueOf(14));
    oddPageList.add(Integer.valueOf(15));
    oddPageList.add(Integer.valueOf(16));
    oddPageList.add(Integer.valueOf(17));

    evenPageList = new PaginatedArrayList(5);
    evenPageList.add(Integer.valueOf(0));
    evenPageList.add(Integer.valueOf(1));
    evenPageList.add(Integer.valueOf(2));
    evenPageList.add(Integer.valueOf(3));
    evenPageList.add(Integer.valueOf(4));
    evenPageList.add(Integer.valueOf(5));
    evenPageList.add(Integer.valueOf(6));
    evenPageList.add(Integer.valueOf(7));
    evenPageList.add(Integer.valueOf(8));
    evenPageList.add(Integer.valueOf(9));
    evenPageList.add(Integer.valueOf(10));
    evenPageList.add(Integer.valueOf(11));
    evenPageList.add(Integer.valueOf(12));
    evenPageList.add(Integer.valueOf(13));
    evenPageList.add(Integer.valueOf(14));

  }

  @Test
  public void testOddPaginatedIterator() {

    assertEquals(true, oddPageList.isFirstPage());
    assertEquals(false, oddPageList.isPreviousPageAvailable());

    int count = 0;
    Iterator i = oddPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    assertEquals(5, count);

    oddPageList.nextPage();

    count = 0;
    i = oddPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    assertEquals(5, count);

    oddPageList.nextPage();

    assertEquals(true, oddPageList.isMiddlePage());

    count = 0;
    i = oddPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    assertEquals(5, count);

    oddPageList.nextPage();

    count = 0;
    i = oddPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    assertEquals(3, count);

    assertEquals(true, oddPageList.isLastPage());
    assertEquals(false, oddPageList.isNextPageAvailable());

    oddPageList.nextPage();

    assertEquals(true, oddPageList.isLastPage());
    assertEquals(false, oddPageList.isNextPageAvailable());

    oddPageList.previousPage();

    assertEquals(Integer.valueOf(10), oddPageList.get(0));
    assertEquals(Integer.valueOf(12), oddPageList.get(2));

    oddPageList.gotoPage(500);

    assertEquals(Integer.valueOf(0), oddPageList.get(0));
    assertEquals(Integer.valueOf(4), oddPageList.get(4));

    oddPageList.gotoPage(-500);

    assertEquals(Integer.valueOf(15), oddPageList.get(0));
    assertEquals(Integer.valueOf(17), oddPageList.get(2));
  }

  @Test
  public void testEvenPaginatedIterator() {

    assertEquals(true, evenPageList.isFirstPage());
    assertEquals(false, evenPageList.isPreviousPageAvailable());

    int count = 0;
    Iterator i = evenPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    assertEquals(5, count);

    evenPageList.nextPage();

    assertEquals(true, evenPageList.isMiddlePage());

    count = 0;
    i = evenPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    assertEquals(5, count);

    evenPageList.nextPage();

    count = 0;
    i = evenPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    assertEquals(5, count);

    assertEquals(true, evenPageList.isLastPage());
    assertEquals(false, evenPageList.isNextPageAvailable());

    evenPageList.nextPage();

    assertEquals(Integer.valueOf(10), evenPageList.get(0));
    assertEquals(Integer.valueOf(14), evenPageList.get(4));

    evenPageList.previousPage();

    assertEquals(Integer.valueOf(5), evenPageList.get(0));
    assertEquals(Integer.valueOf(9), evenPageList.get(4));

    evenPageList.gotoPage(500);

    assertEquals(Integer.valueOf(0), evenPageList.get(0));
    assertEquals(Integer.valueOf(4), evenPageList.get(4));

    evenPageList.gotoPage(-500);

    assertEquals(Integer.valueOf(10), evenPageList.get(0));
    assertEquals(Integer.valueOf(14), evenPageList.get(4));
  }

  @Test
  public void testSmallPaginatedIterator() {

    assertEquals(true, smallPageList.isFirstPage());
    assertEquals(true, smallPageList.isLastPage());
    assertEquals(false, smallPageList.isMiddlePage());
    assertEquals(false, smallPageList.isPreviousPageAvailable());
    assertEquals(false, smallPageList.isNextPageAvailable());

    int count = 0;
    Iterator i = smallPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    assertEquals(3, count);

    smallPageList.nextPage();

    count = 0;
    i = smallPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    assertEquals(3, count);

    assertEquals(true, smallPageList.isFirstPage());
    assertEquals(true, smallPageList.isLastPage());
    assertEquals(false, smallPageList.isMiddlePage());
    assertEquals(false, smallPageList.isPreviousPageAvailable());
    assertEquals(false, smallPageList.isNextPageAvailable());

    smallPageList.nextPage();

    count = 0;
    i = smallPageList.iterator();
    while (i.hasNext()) {
      i.next();
      count++;
    }
    assertEquals(3, count);

    smallPageList.nextPage();

    assertEquals(Integer.valueOf(0), smallPageList.get(0));
    assertEquals(Integer.valueOf(2), smallPageList.get(2));

    smallPageList.previousPage();

    assertEquals(Integer.valueOf(0), smallPageList.get(0));
    assertEquals(Integer.valueOf(2), smallPageList.get(2));

    smallPageList.gotoPage(500);

    assertEquals(Integer.valueOf(0), smallPageList.get(0));
    assertEquals(Integer.valueOf(2), smallPageList.get(2));

    smallPageList.gotoPage(-500);

    assertEquals(Integer.valueOf(0), smallPageList.get(0));
    assertEquals(Integer.valueOf(2), smallPageList.get(2));

    assertEquals(true, smallPageList.isFirstPage());
    assertEquals(true, smallPageList.isLastPage());
    assertEquals(false, smallPageList.isMiddlePage());
    assertEquals(false, smallPageList.isPreviousPageAvailable());
    assertEquals(false, smallPageList.isNextPageAvailable());
  }
}
