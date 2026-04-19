/*
 * Copyright 2004-2025 the original author or authors.
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
package com.ibatis.sqlmap.engine.mapping.result;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Not really sure what this is...it is not used internally
 */
public class XmlList implements List {

  /** The list. */
  private List list;

  /**
   * Build a list from another list.
   *
   * @param list
   *          - a base list
   */
  public XmlList(List list) {
    this.list = list;
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return list.contains(o);
  }

  @Override
  public Iterator iterator() {
    return list.iterator();
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  @Override
  public Object[] toArray(Object a[]) {
    return list.toArray(a);
  }

  @Override
  public boolean add(Object o) {
    return list.add(o);
  }

  @Override
  public boolean remove(Object o) {
    return list.remove(o);
  }

  @Override
  public boolean containsAll(Collection c) {
    return list.containsAll(c);
  }

  @Override
  public boolean addAll(Collection c) {
    return list.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection c) {
    return list.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection c) {
    return list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection c) {
    return list.retainAll(c);
  }

  @Override
  public void clear() {
    list.clear();
  }

  @Override
  public Object get(int index) {
    return list.get(index);
  }

  @Override
  public Object set(int index, Object element) {
    return list.set(index, element);
  }

  @Override
  public void add(int index, Object element) {
    list.add(index, element);
  }

  @Override
  public Object remove(int index) {
    return list.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return list.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return list.lastIndexOf(o);
  }

  @Override
  public ListIterator listIterator() {
    return list.listIterator();
  }

  @Override
  public ListIterator listIterator(int index) {
    return list.listIterator(index);
  }

  @Override
  public List subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (Object element : list) {
      builder.append(element);
      builder.append("\r\n");
    }
    return builder.toString();
  }

}
