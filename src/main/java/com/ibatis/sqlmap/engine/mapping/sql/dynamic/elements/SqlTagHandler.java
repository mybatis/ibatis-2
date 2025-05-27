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

/**
 * The Interface SqlTagHandler.
 */
public interface SqlTagHandler {

  /** The Constant SKIP_BODY. */
  int SKIP_BODY = 0;

  /** The Constant INCLUDE_BODY. */
  int INCLUDE_BODY = 1;

  /** The Constant REPEAT_BODY. */
  int REPEAT_BODY = 2;

  /**
   * Do start fragment.
   *
   * @param ctx
   *          the ctx
   * @param tag
   *          the tag
   * @param parameterObject
   *          the parameter object
   *
   * @return the int
   */
  int doStartFragment(SqlTagContext ctx, SqlTag tag, Object parameterObject);

  /**
   * Do end fragment.
   *
   * @param ctx
   *          the ctx
   * @param tag
   *          the tag
   * @param parameterObject
   *          the parameter object
   * @param bodyContent
   *          the body content
   *
   * @return the int
   */
  int doEndFragment(SqlTagContext ctx, SqlTag tag, Object parameterObject, StringBuilder bodyContent);

  /**
   * Do prepend.
   *
   * @param ctx
   *          the ctx
   * @param tag
   *          the tag
   * @param parameterObject
   *          the parameter object
   * @param bodyContent
   *          the body content
   */
  void doPrepend(SqlTagContext ctx, SqlTag tag, Object parameterObject, StringBuilder bodyContent);

}
