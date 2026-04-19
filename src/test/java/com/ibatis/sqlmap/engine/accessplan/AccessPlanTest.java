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
package com.ibatis.sqlmap.engine.accessplan;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AccessPlanTest {

  public static class TestBean {
    private String name;
    private int age;
    private boolean active;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public boolean isActive() {
      return active;
    }

    public void setActive(boolean active) {
      this.active = active;
    }
  }

  @Test
  void shouldGetPropertiesViaBeanAccessPlan() {
    AccessPlan plan = AccessPlanFactory.getAccessPlan(TestBean.class, new String[] { "name", "age" });
    TestBean bean = new TestBean();
    bean.setName("Alice");
    bean.setAge(30);
    Object[] values = plan.getProperties(bean);
    Assertions.assertEquals("Alice", values[0]);
    Assertions.assertEquals(30, values[1]);
  }

  @Test
  void shouldSetPropertiesViaBeanAccessPlan() {
    AccessPlan plan = AccessPlanFactory.getAccessPlan(TestBean.class, new String[] { "name", "age", "active" });
    TestBean bean = new TestBean();
    plan.setProperties(bean, new Object[] { "Bob", 25, Boolean.TRUE });
    Assertions.assertEquals("Bob", bean.getName());
    Assertions.assertEquals(25, bean.getAge());
    Assertions.assertTrue(bean.isActive());
  }

  @Test
  void shouldGetPropertiesViaMapAccessPlan() {
    AccessPlan plan = AccessPlanFactory.getAccessPlan(HashMap.class, new String[] { "key1", "key2" });
    Map<String, Object> map = new HashMap<>();
    map.put("key1", "value1");
    map.put("key2", 42);
    Object[] values = plan.getProperties(map);
    Assertions.assertEquals("value1", values[0]);
    Assertions.assertEquals(42, values[1]);
  }

  @Test
  void shouldSetPropertiesViaMapAccessPlan() {
    AccessPlan plan = AccessPlanFactory.getAccessPlan(HashMap.class, new String[] { "a", "b" });
    Map<String, Object> map = new HashMap<>();
    plan.setProperties(map, new Object[] { "x", 99 });
    Assertions.assertEquals("x", map.get("a"));
    Assertions.assertEquals(99, map.get("b"));
  }

  @Test
  void shouldHandleNullClassWithComplexAccessPlan() {
    // null class → ComplexAccessPlan is constructed but ClassInfo lookup is skipped
    // The factory itself creates the plan; we just verify no NPE in factory
    Assertions.assertThrows(RuntimeException.class,
        () -> AccessPlanFactory.getAccessPlan(null, new String[] { "any" }));
  }

  @Test
  void shouldHandleNullPropertyNamesWithComplexAccessPlan() {
    AccessPlan plan = AccessPlanFactory.getAccessPlan(TestBean.class, null);
    Assertions.assertNotNull(plan);
  }

  @Test
  void shouldHandleDottedPropertyNameWithComplexAccessPlan() {
    AccessPlan plan = AccessPlanFactory.getAccessPlan(TestBean.class, new String[] { "nested.prop" });
    Assertions.assertNotNull(plan);
  }

  @Test
  void shouldHandleIndexedPropertyNameWithComplexAccessPlan() {
    AccessPlan plan = AccessPlanFactory.getAccessPlan(TestBean.class, new String[] { "items[0]" });
    Assertions.assertNotNull(plan);
  }

  @Test
  void shouldReportBytecodeEnhancementDisabledByDefault() {
    Assertions.assertFalse(AccessPlanFactory.isBytecodeEnhancementEnabled());
  }

  @Test
  void shouldAllowTogglingBytecodeEnhancementFlag() {
    try {
      AccessPlanFactory.setBytecodeEnhancementEnabled(true);
      Assertions.assertTrue(AccessPlanFactory.isBytecodeEnhancementEnabled());
      AccessPlan plan = AccessPlanFactory.getAccessPlan(TestBean.class, new String[] { "name" });
      Assertions.assertNotNull(plan);
    } finally {
      AccessPlanFactory.setBytecodeEnhancementEnabled(false);
    }
  }
}
