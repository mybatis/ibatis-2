/*
 * Copyright 2004-2022 the original author or authors.
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
package com.ibatis.sqlmap.engine.mapping.sql;

import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMapping;

/**
 * The Class SqlText.
 */
public class SqlText implements SqlChild {

  /** The text. */
  private String text;

  /** The is white space. */
  private boolean isWhiteSpace;

  /** The post parse required. */
  private boolean postParseRequired;

  /** The parameter mappings. */
  private ParameterMapping[] parameterMappings;

  /**
   * Gets the text.
   *
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the text.
   *
   * @param text
   *          the new text
   */
  public void setText(String text) {
    this.text = text.replace('\r', ' ').replace('\n', ' ').replace('\t', ' ');
    this.isWhiteSpace = text.trim().length() == 0;
  }

  /**
   * Checks if is white space.
   *
   * @return true, if is white space
   */
  public boolean isWhiteSpace() {
    return isWhiteSpace;
  }

  /**
   * Gets the parameter mappings.
   *
   * @return the parameter mappings
   */
  public ParameterMapping[] getParameterMappings() {
    return parameterMappings;
  }

  /**
   * Sets the parameter mappings.
   *
   * @param parameterMappings
   *          the new parameter mappings
   */
  public void setParameterMappings(ParameterMapping[] parameterMappings) {
    this.parameterMappings = parameterMappings;
  }

  /**
   * Checks if is post parse required.
   *
   * @return true, if is post parse required
   */
  public boolean isPostParseRequired() {
    return postParseRequired;
  }

  /**
   * Sets the post parse required.
   *
   * @param postParseRequired
   *          the new post parse required
   */
  public void setPostParseRequired(boolean postParseRequired) {
    this.postParseRequired = postParseRequired;
  }

}
