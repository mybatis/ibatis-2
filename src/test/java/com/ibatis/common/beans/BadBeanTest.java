/*
 * Copyright 2004-2021 the original author or authors.
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
package com.ibatis.common.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import badbeans.*;

class BadBeanTest {

  private static final String PROPNAME = "value";
  private static final Object STRING_VALUE = "1";
  private static final Object INT_VALUE = Integer.valueOf(1);
  private static final Object[] NO_VALUE = new Object[] {};
  private static final Object[] STRING_PARAMS = new Object[] { STRING_VALUE };
  private static final Object[] INT_PARAMS = new Object[] { INT_VALUE };

  @Test
  void testShouldSuccessfullyGetAndSetValueOnGoodBean() throws Exception {
    GoodBean bean = new GoodBean();
    ClassInfo info = ClassInfo.getInstance(GoodBean.class);
    info.getSetter(PROPNAME).invoke(bean, STRING_PARAMS);
    assertEquals(STRING_VALUE, info.getGetter(PROPNAME).invoke(bean, NO_VALUE));
    assertEquals(String.class, info.getSetterType(PROPNAME));
    assertEquals(String.class, info.getGetterType(PROPNAME));
  }

  @Test
  void testShouldSuccessfullyGetAndSetValueOnBeanWithDifferentTypeGetterSetter() throws Exception {
    BeanWithDifferentTypeGetterSetter bean = new BeanWithDifferentTypeGetterSetter();
    ClassInfo info = ClassInfo.getInstance(BeanWithDifferentTypeGetterSetter.class);
    info.getSetter(PROPNAME).invoke(bean, INT_PARAMS);
    assertEquals(INT_VALUE.toString(), info.getGetter(PROPNAME).invoke(bean, NO_VALUE));
    assertEquals(Integer.class, info.getSetterType(PROPNAME));
    assertEquals(String.class, info.getGetterType(PROPNAME));
  }

  @Test
  void testShouldSuccessfullyGetAndSetValueOnBeanWithOverloadedSetter() throws Exception {
    BeanWithOverloadedSetter bean = new BeanWithOverloadedSetter();
    ClassInfo info = ClassInfo.getInstance(BeanWithOverloadedSetter.class);
    info.getSetter(PROPNAME).invoke(bean, STRING_PARAMS);
    assertEquals(STRING_VALUE, info.getGetter(PROPNAME).invoke(bean, NO_VALUE));
    assertEquals(String.class, info.getSetterType(PROPNAME));
    assertEquals(String.class, info.getGetterType(PROPNAME));
  }

  @Test
  void testShouldFailInitializingClassInfoForBeanWithNoGetterOverloadedSetter() {
    try {
      try {
        ClassInfo.getInstance(BeanWithNoGetterOverloadedSetters.class);
      } catch (Exception e) {
        assertTrue(e.getMessage().indexOf("Illegal overloaded setter method with ambiguous type for property") == 0);
        throw e;
      }
      fail("Expected exception.");
    } catch (Exception e) {
      // ignore
    }
  }

  @Test
  void testShouldFailInitializingClassInfoForBeanWithDifferentTypeOverloadedSetter() {
    try {
      try {
        ClassInfo.getInstance(BeanWithDifferentTypeOverloadedSetter.class);
      } catch (Exception e) {
        assertTrue(e.getMessage().indexOf("Illegal overloaded setter method with ambiguous type for property") == 0);
        throw e;
      }
      fail("Expected exception.");
    } catch (Exception e) {
      // ignore
    }
  }

  @Test
  void testUnwrapThrowable() {
    SQLException cause = new SQLException("test");
    UndeclaredThrowableException e = new UndeclaredThrowableException(cause);
    assertEquals(cause, ClassInfo.unwrapThrowable(e));
  }

}
