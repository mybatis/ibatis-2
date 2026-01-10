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
package com.ibatis.common.beans;

import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import badbeans.BeanWithDifferentTypeGetterSetter;
import badbeans.BeanWithDifferentTypeOverloadedSetter;
import badbeans.BeanWithNoGetterOverloadedSetters;
import badbeans.BeanWithOverloadedSetter;
import badbeans.GoodBean;

class BadBeanTest {

  private static final String PROPNAME = "value";
  private static final Object STRING_VALUE = "1";
  private static final Object INT_VALUE = Integer.valueOf(1);
  private static final Object[] NO_VALUE = {};
  private static final Object[] STRING_PARAMS = { BadBeanTest.STRING_VALUE };
  private static final Object[] INT_PARAMS = { BadBeanTest.INT_VALUE };

  @Test
  void testShouldSuccessfullyGetAndSetValueOnGoodBean() throws Exception {
    final GoodBean bean = new GoodBean();
    final ClassInfo info = ClassInfo.getInstance(GoodBean.class);
    info.getSetter(BadBeanTest.PROPNAME).invoke(bean, BadBeanTest.STRING_PARAMS);
    Assertions.assertEquals(BadBeanTest.STRING_VALUE,
        info.getGetter(BadBeanTest.PROPNAME).invoke(bean, BadBeanTest.NO_VALUE));
    Assertions.assertEquals(String.class, info.getSetterType(BadBeanTest.PROPNAME));
    Assertions.assertEquals(String.class, info.getGetterType(BadBeanTest.PROPNAME));
  }

  @Test
  void testShouldSuccessfullyGetAndSetValueOnBeanWithDifferentTypeGetterSetter() throws Exception {
    final BeanWithDifferentTypeGetterSetter bean = new BeanWithDifferentTypeGetterSetter();
    final ClassInfo info = ClassInfo.getInstance(BeanWithDifferentTypeGetterSetter.class);
    info.getSetter(BadBeanTest.PROPNAME).invoke(bean, BadBeanTest.INT_PARAMS);
    Assertions.assertEquals(BadBeanTest.INT_VALUE.toString(),
        info.getGetter(BadBeanTest.PROPNAME).invoke(bean, BadBeanTest.NO_VALUE));
    Assertions.assertEquals(Integer.class, info.getSetterType(BadBeanTest.PROPNAME));
    Assertions.assertEquals(String.class, info.getGetterType(BadBeanTest.PROPNAME));
  }

  @Test
  void testShouldSuccessfullyGetAndSetValueOnBeanWithOverloadedSetter() throws Exception {
    final BeanWithOverloadedSetter bean = new BeanWithOverloadedSetter();
    final ClassInfo info = ClassInfo.getInstance(BeanWithOverloadedSetter.class);
    info.getSetter(BadBeanTest.PROPNAME).invoke(bean, BadBeanTest.STRING_PARAMS);
    Assertions.assertEquals(BadBeanTest.STRING_VALUE,
        info.getGetter(BadBeanTest.PROPNAME).invoke(bean, BadBeanTest.NO_VALUE));
    Assertions.assertEquals(String.class, info.getSetterType(BadBeanTest.PROPNAME));
    Assertions.assertEquals(String.class, info.getGetterType(BadBeanTest.PROPNAME));
  }

  @Test
  void testShouldFailInitializingClassInfoForBeanWithNoGetterOverloadedSetter() {
    try {
      try {
        ClassInfo.getInstance(BeanWithNoGetterOverloadedSetters.class);
      } catch (final Exception e) {
        Assertions.assertTrue(
            e.getMessage().indexOf("Illegal overloaded setter method with ambiguous type for property") == 0);
        throw e;
      }
      Assertions.fail("Expected exception.");
    } catch (final Exception e) {
      // ignore
    }
  }

  @Test
  void testShouldFailInitializingClassInfoForBeanWithDifferentTypeOverloadedSetter() {
    try {
      try {
        ClassInfo.getInstance(BeanWithDifferentTypeOverloadedSetter.class);
      } catch (final Exception e) {
        Assertions.assertTrue(
            e.getMessage().indexOf("Illegal overloaded setter method with ambiguous type for property") == 0);
        throw e;
      }
      Assertions.fail("Expected exception.");
    } catch (final Exception e) {
      // ignore
    }
  }

  @Test
  void testUnwrapThrowable() {
    final SQLException cause = new SQLException("test");
    final UndeclaredThrowableException e = new UndeclaredThrowableException(cause);
    Assertions.assertEquals(cause, ClassInfo.unwrapThrowable(e));
  }

}
