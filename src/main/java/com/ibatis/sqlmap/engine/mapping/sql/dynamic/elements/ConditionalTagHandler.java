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
package com.ibatis.sqlmap.engine.mapping.sql.dynamic.elements;

import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;
import com.ibatis.sqlmap.engine.type.SimpleDateFormatter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * The Class ConditionalTagHandler.
 */
public abstract class ConditionalTagHandler extends BaseTagHandler {

  /** The Constant PROBE. */
  private static final Probe PROBE = ProbeFactory.getProbe();

  /** The Constant NOT_COMPARABLE. */
  public static final long NOT_COMPARABLE = Long.MIN_VALUE;

  /** The Constant DATE_MASK. */
  private static final String DATE_MASK = "yyyy/MM/dd hh:mm:ss";

  /** The Constant START_INDEX. */
  private static final String START_INDEX = "[";

  /**
   * Checks if is condition.
   *
   * @param ctx
   *          the ctx
   * @param tag
   *          the tag
   * @param parameterObject
   *          the parameter object
   *
   * @return true, if is condition
   */
  public abstract boolean isCondition(SqlTagContext ctx, SqlTag tag, Object parameterObject);

  @Override
  public int doStartFragment(SqlTagContext ctx, SqlTag tag, Object parameterObject) {

    ctx.pushRemoveFirstPrependMarker(tag);

    if (isCondition(ctx, tag, parameterObject)) {
      return SqlTagHandler.INCLUDE_BODY;
    }
    return SqlTagHandler.SKIP_BODY;
  }

  @Override
  public int doEndFragment(SqlTagContext ctx, SqlTag tag, Object parameterObject, StringBuilder bodyContent) {

    IterateContext iterate = ctx.peekIterateContext();

    if (null != iterate && iterate.isAllowNext()) {
      iterate.next();
      iterate.setAllowNext(false);
      if (!iterate.hasNext()) {
        iterate.setFinal(true);
      }
    }

    // iteratePropertyReplace(bodyContent,ctx.peekIterateContext());

    return super.doEndFragment(ctx, tag, parameterObject, bodyContent);
  }

  /**
   * Compare.
   *
   * @param ctx
   *          the ctx
   * @param tag
   *          the tag
   * @param parameterObject
   *          the parameter object
   *
   * @return the long
   */
  protected long compare(SqlTagContext ctx, SqlTag tag, Object parameterObject) {
    String comparePropertyName = tag.getComparePropertyAttr();
    String compareValue = tag.getCompareValueAttr();

    String prop = getResolvedProperty(ctx, tag);
    Object value1;
    Class type;

    if (prop != null) {
      value1 = PROBE.getObject(parameterObject, prop);
      type = PROBE.getPropertyTypeForGetter(parameterObject, prop);
    } else {
      value1 = parameterObject;
      if (value1 != null) {
        type = parameterObject.getClass();
      } else {
        type = Object.class;
      }
    }
    if (comparePropertyName != null) {
      Object value2 = PROBE.getObject(parameterObject, comparePropertyName);
      return compareValues(type, value1, value2);
    }
    if (compareValue != null) {
      return compareValues(type, value1, compareValue);
    }
    throw new RuntimeException("Error comparing in conditional fragment.  Uknown 'compare to' values.");
  }

  /**
   * Compare values.
   *
   * @param type
   *          the type
   * @param value1
   *          the value 1
   * @param value2
   *          the value 2
   *
   * @return the long
   */
  protected long compareValues(Class type, Object value1, Object value2) {
    long result;

    if (value1 == null || value2 == null) {
      result = value1 == value2 ? 0 : NOT_COMPARABLE;
    } else {
      if (value2.getClass() != type) {
        value2 = convertValue(type, value2.toString());
      }
      if (value2 instanceof String && type != String.class) {
        value1 = value1.toString();
      }
      if (!(value1 instanceof Comparable && value2 instanceof Comparable)) {
        value1 = value1.toString();
        value2 = value2.toString();
      }
      result = ((Comparable) value1).compareTo(value2);
    }

    return result;
  }

  /**
   * Convert value.
   *
   * @param type
   *          the type
   * @param value
   *          the value
   *
   * @return the object
   */
  protected Object convertValue(Class type, String value) {
    if (type == String.class) {
      return value;
    }
    if (type == Byte.class || type == byte.class) {
      return Byte.valueOf(value);
    }
    if (type == Short.class || type == short.class) {
      return Short.valueOf(value);
    }
    if (type == Character.class || type == char.class) {
      return Character.valueOf(value.charAt(0));
    }
    if (type == Integer.class || type == int.class) {
      return Integer.valueOf(value);
    }
    if (type == Long.class || type == long.class) {
      return Long.valueOf(value);
    }
    if (type == Float.class || type == float.class) {
      return Float.valueOf(value);
    }
    if (type == Double.class || type == double.class) {
      return Double.valueOf(value);
    }
    if (type == Boolean.class || type == boolean.class) {
      return Boolean.valueOf(value);
    }
    if (type == Date.class) {
      return SimpleDateFormatter.format(DATE_MASK, value);
    }
    if (type == BigInteger.class) {
      return new BigInteger(value);
    }
    if (type == BigDecimal.class) {
      return new BigDecimal(value);
    }
    return value;

  }

  /**
   * This method will add the proper index values to an indexed property string if we are inside an iterate tag.
   *
   * @param ctx
   *          the ctx
   * @param tag
   *          the tag
   *
   * @return the resolved property
   */
  protected String getResolvedProperty(SqlTagContext ctx, SqlTag tag) {
    String prop = tag.getPropertyAttr();
    IterateContext itCtx = ctx.peekIterateContext();

    if (prop != null) {

      if (null != itCtx && itCtx.isAllowNext()) {
        itCtx.next();
        itCtx.setAllowNext(false);
        if (!itCtx.hasNext()) {
          itCtx.setFinal(true);
        }
      }

      if (prop.indexOf(START_INDEX) > -1 && itCtx != null) {
        prop = itCtx.addIndexToTagProperty(prop);
      }
    }

    return prop;
  }
}
