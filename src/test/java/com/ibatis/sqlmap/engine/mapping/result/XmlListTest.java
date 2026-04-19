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
package com.ibatis.sqlmap.engine.mapping.result;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class XmlListTest {

  @Test
  void shouldDelegateListOperations() {
    List<Object> base = new ArrayList<>();
    base.add("a");
    base.add("b");

    XmlList list = new XmlList(base);

    Assertions.assertEquals(2, list.size());
    Assertions.assertFalse(list.isEmpty());
    Assertions.assertTrue(list.contains("a"));
    Assertions.assertEquals("a", list.get(0));
    Assertions.assertEquals(0, list.indexOf("a"));
    Assertions.assertEquals(1, list.lastIndexOf("b"));

    list.add("c");
    list.add(1, "x");
    list.set(0, "z");
    list.remove("b");
    list.remove(1);

    Assertions.assertTrue(list.containsAll(List.of("z", "c")));
    list.addAll(List.of("d", "e"));
    list.addAll(1, List.of("m"));
    list.removeAll(List.of("e"));
    list.retainAll(List.of("z", "m", "c", "d"));

    Assertions.assertNotNull(list.iterator());
    Assertions.assertNotNull(list.listIterator());
    Assertions.assertNotNull(list.listIterator(1));
    Assertions.assertNotNull(list.subList(0, 1));
    Assertions.assertEquals(list.toArray().length, list.toArray(new Object[0]).length);
    Assertions.assertTrue(list.toString().contains("\r\n"));

    list.clear();
    Assertions.assertTrue(list.isEmpty());
  }
}
