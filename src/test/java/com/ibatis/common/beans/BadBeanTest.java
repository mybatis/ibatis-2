package com.ibatis.common.beans;

import junit.framework.TestCase;
import badbeans.*;

public class BadBeanTest extends TestCase {

  private static final String PROPNAME = "value";
  private static final Object STRING_VALUE = "1";
  private static final Object INT_VALUE = new Integer(1);
  private static final Object[] NO_VALUE = new Object[]{};
  private static final Object[] STRING_PARAMS = new Object[]{STRING_VALUE};
  private static final Object[] INT_PARAMS = new Object[]{INT_VALUE};

  public void testShouldSuccessfullyGetAndSetValueOnGoodBean() throws Exception {
    GoodBean bean = new GoodBean();
    ClassInfo info = ClassInfo.getInstance(GoodBean.class);
    info.getSetter(PROPNAME).invoke(bean, STRING_PARAMS);
    assertEquals(STRING_VALUE, info.getGetter(PROPNAME).invoke(bean, NO_VALUE));
    assertEquals(String.class, info.getSetterType(PROPNAME));
    assertEquals(String.class, info.getGetterType(PROPNAME));
  }

  public void testShouldSuccessfullyGetAndSetValueOnBeanWithDifferentTypeGetterSetter() throws Exception {
    BeanWithDifferentTypeGetterSetter bean = new BeanWithDifferentTypeGetterSetter();
    ClassInfo info = ClassInfo.getInstance(BeanWithDifferentTypeGetterSetter.class);
    info.getSetter(PROPNAME).invoke(bean, INT_PARAMS);
    assertEquals(INT_VALUE.toString(), info.getGetter(PROPNAME).invoke(bean, NO_VALUE));
    assertEquals(Integer.class, info.getSetterType(PROPNAME));
    assertEquals(String.class, info.getGetterType(PROPNAME));
  }

  public void testShouldSuccessfullyGetAndSetValueOnBeanWithOverloadedSetter() throws Exception {
    BeanWithOverloadedSetter bean = new BeanWithOverloadedSetter();
    ClassInfo info = ClassInfo.getInstance(BeanWithOverloadedSetter.class);
    info.getSetter(PROPNAME).invoke(bean, STRING_PARAMS);
    assertEquals(STRING_VALUE, info.getGetter(PROPNAME).invoke(bean, NO_VALUE));
    assertEquals(String.class, info.getSetterType(PROPNAME));
    assertEquals(String.class, info.getGetterType(PROPNAME));
  }

  public void testShouldFailInitializingClassInfoForBeanWithNoGetterOverloadedSetter() {
    try {
      try {
        ClassInfo.getInstance(BeanWithNoGetterOverloadedSetters.class);
      } catch (Exception e) {
        assertTrue(e.getMessage().indexOf("Illegal overloaded setter method with ambiguous type for property") == 0);
        throw e;
      }
      fail("Expected exception.");
    } catch (Exception e) {
      //ignore
    }
  }

  public void testShouldFailInitializingClassInfoForBeanWithDifferentTypeOverloadedSetter() {
    try {
      try {
        ClassInfo.getInstance(BeanWithDifferentTypeOverloadedSetter.class);
      } catch (Exception e) {
        assertTrue(e.getMessage().indexOf("Illegal overloaded setter method with ambiguous type for property") == 0);
        throw e;
      }
      fail("Expected exception.");
    } catch (Exception e) {
      //ignore
    }
  }

}
