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
package com.ibatis.sqlmap.engine.mapping.statement;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapExecutor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * The Class PaginatedDataList.
 *
 * @deprecated All paginated list features have been deprecated
 */
@Deprecated
public class PaginatedDataList implements PaginatedList {

  /** The sql map executor. */
  private SqlMapExecutor sqlMapExecutor;

  /** The statement name. */
  private String statementName;

  /** The parameter object. */
  private Object parameterObject;

  /** The page size. */
  private int pageSize;

  /** The index. */
  private int index;

  /** The prev page list. */
  private List prevPageList;

  /** The current page list. */
  private List currentPageList;

  /** The next page list. */
  private List nextPageList;

  /**
   * Instantiates a new paginated data list.
   *
   * @param sqlMapExecutor
   *          the sql map executor
   * @param statementName
   *          the statement name
   * @param parameterObject
   *          the parameter object
   * @param pageSize
   *          the page size
   *
   * @throws SQLException
   *           the SQL exception
   */
  public PaginatedDataList(SqlMapExecutor sqlMapExecutor, String statementName, Object parameterObject, int pageSize)
      throws SQLException {
    this.sqlMapExecutor = sqlMapExecutor;
    this.statementName = statementName;
    this.parameterObject = parameterObject;
    this.pageSize = pageSize;
    this.index = 0;
    pageTo(0);
  }

  /**
   * Page forward.
   */
  private void pageForward() {
    try {
      prevPageList = currentPageList;
      currentPageList = nextPageList;
      nextPageList = getList(index + 1, pageSize);
    } catch (SQLException e) {
      throw new RuntimeException("Unexpected error while repaginating paged list.  Cause: " + e, e);
    }
  }

  /**
   * Page back.
   */
  private void pageBack() {
    try {
      nextPageList = currentPageList;
      currentPageList = prevPageList;
      if (index > 0) {
        prevPageList = getList(index - 1, pageSize);
      } else {
        prevPageList = new ArrayList<>();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Unexpected error while repaginating paged list.  Cause: " + e, e);
    }
  }

  /**
   * Safe page to.
   *
   * @param idx
   *          the idx
   */
  private void safePageTo(int idx) {
    try {
      pageTo(idx);
    } catch (SQLException e) {
      throw new RuntimeException("Unexpected error while repaginating paged list.  Cause: " + e, e);
    }
  }

  /**
   * Page to.
   *
   * @param idx
   *          the idx
   *
   * @throws SQLException
   *           the SQL exception
   */
  public void pageTo(int idx) throws SQLException {
    index = idx;

    List list;

    if (idx < 1) {
      list = getList(idx, pageSize * 2);
    } else {
      list = getList(idx - 1, pageSize * 3);
    }

    if (list.size() < 1) {
      prevPageList = new ArrayList<>(0);
      currentPageList = new ArrayList<>(0);
      nextPageList = new ArrayList<>(0);
    } else if (idx < 1) {
      prevPageList = new ArrayList<>(0);
      if (list.size() <= pageSize) {
        currentPageList = list.subList(0, list.size());
        nextPageList = new ArrayList<>(0);
      } else {
        currentPageList = list.subList(0, pageSize);
        nextPageList = list.subList(pageSize, list.size());
      }
    } else if (list.size() <= pageSize) {
      prevPageList = list.subList(0, list.size());
      currentPageList = new ArrayList<>(0);
      nextPageList = new ArrayList<>(0);
    } else if (list.size() <= pageSize * 2) {
      prevPageList = list.subList(0, pageSize);
      currentPageList = list.subList(pageSize, list.size());
      nextPageList = new ArrayList<>(0);
    } else {
      prevPageList = list.subList(0, pageSize);
      currentPageList = list.subList(pageSize, pageSize * 2);
      nextPageList = list.subList(pageSize * 2, list.size());
    }

  }

  /**
   * Gets the list.
   *
   * @param idx
   *          the idx
   * @param localPageSize
   *          the local page size
   *
   * @return the list
   *
   * @throws SQLException
   *           the SQL exception
   */
  private List getList(int idx, int localPageSize) throws SQLException {
    return sqlMapExecutor.queryForList(statementName, parameterObject, idx * pageSize, localPageSize);
  }

  @Override
  public boolean nextPage() {
    if (isNextPageAvailable()) {
      index++;
      pageForward();
      return true;
    }
    return false;
  }

  @Override
  public boolean previousPage() {
    if (isPreviousPageAvailable()) {
      index--;
      pageBack();
      return true;
    }
    return false;
  }

  @Override
  public void gotoPage(int pageNumber) {
    safePageTo(pageNumber);
  }

  @Override
  public int getPageSize() {
    return pageSize;
  }

  @Override
  public boolean isFirstPage() {
    return index == 0;
  }

  @Override
  public boolean isMiddlePage() {
    return !isFirstPage() && !isLastPage();
  }

  @Override
  public boolean isLastPage() {
    return nextPageList.size() < 1;
  }

  @Override
  public boolean isNextPageAvailable() {
    return nextPageList.size() > 0;
  }

  @Override
  public boolean isPreviousPageAvailable() {
    return prevPageList.size() > 0;
  }

  @Override
  public int size() {
    return currentPageList.size();
  }

  @Override
  public boolean isEmpty() {
    return currentPageList.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return currentPageList.contains(o);
  }

  @Override
  public Iterator iterator() {
    return currentPageList.iterator();
  }

  @Override
  public Object[] toArray() {
    return currentPageList.toArray();
  }

  @Override
  public Object[] toArray(Object a[]) {
    return currentPageList.toArray(a);
  }

  @Override
  public boolean containsAll(Collection c) {
    return currentPageList.containsAll(c);
  }

  @Override
  public Object get(int index) {
    return currentPageList.get(index);
  }

  @Override
  public int indexOf(Object o) {
    return currentPageList.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return currentPageList.lastIndexOf(o);
  }

  @Override
  public ListIterator listIterator() {
    return currentPageList.listIterator();
  }

  @Override
  public ListIterator listIterator(int index) {
    return currentPageList.listIterator(index);
  }

  @Override
  public List subList(int fromIndex, int toIndex) {
    return currentPageList.subList(fromIndex, toIndex);
  }

  @Override
  public boolean add(Object o) {
    return currentPageList.add(o);
  }

  @Override
  public boolean remove(Object o) {
    return currentPageList.remove(o);
  }

  @Override
  public boolean addAll(Collection c) {
    return currentPageList.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection c) {
    return currentPageList.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection c) {
    return currentPageList.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection c) {
    return currentPageList.retainAll(c);
  }

  @Override
  public void clear() {
    currentPageList.clear();
  }

  @Override
  public Object set(int index, Object element) {
    return currentPageList.set(index, element);
  }

  @Override
  public void add(int index, Object element) {
    currentPageList.add(index, element);
  }

  @Override
  public Object remove(int index) {
    return currentPageList.remove(index);
  }

  @Override
  public int getPageIndex() {
    return index;
  }

}
