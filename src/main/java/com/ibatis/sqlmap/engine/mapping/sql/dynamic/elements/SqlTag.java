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

import com.ibatis.sqlmap.engine.mapping.sql.SqlChild;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The Class SqlTag.
 */
public class SqlTag implements SqlChild, DynamicParent {

  /** The name. */
  private String name;

  /** The handler. */
  private SqlTagHandler handler;

  /** The prepend attr. */
  // general attributes
  private String prependAttr;

  /** The property attr. */
  private String propertyAttr;

  /** The remove first prepend. */
  private String removeFirstPrepend;

  /** The compare property attr. */
  // conditional attributes
  private String comparePropertyAttr;

  /** The compare value attr. */
  private String compareValueAttr;

  /** The open attr. */
  // iterate attributes
  private String openAttr;

  /** The close attr. */
  private String closeAttr;

  /** The conjunction attr. */
  private String conjunctionAttr;

  /** The parent. */
  private SqlTag parent;

  /** The children. */
  private List children = new ArrayList<>();

  /** The post parse required. */
  private boolean postParseRequired = false;

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the handler.
   *
   * @return the handler
   */
  public SqlTagHandler getHandler() {
    return handler;
  }

  /**
   * Sets the handler.
   *
   * @param handler
   *          the new handler
   */
  public void setHandler(SqlTagHandler handler) {
    this.handler = handler;
  }

  /**
   * Checks if is prepend available.
   *
   * @return true, if is prepend available
   */
  public boolean isPrependAvailable() {
    return prependAttr != null && !prependAttr.isEmpty();
  }

  /**
   * Checks if is close available.
   *
   * @return true, if is close available
   */
  public boolean isCloseAvailable() {
    return closeAttr != null && !closeAttr.isEmpty();
  }

  /**
   * Checks if is open available.
   *
   * @return true, if is open available
   */
  public boolean isOpenAvailable() {
    return openAttr != null && !openAttr.isEmpty();
  }

  /**
   * Checks if is conjunction available.
   *
   * @return true, if is conjunction available
   */
  public boolean isConjunctionAvailable() {
    return conjunctionAttr != null && !conjunctionAttr.isEmpty();
  }

  /**
   * Gets the prepend attr.
   *
   * @return the prepend attr
   */
  public String getPrependAttr() {
    return prependAttr;
  }

  /**
   * Sets the prepend attr.
   *
   * @param prependAttr
   *          the new prepend attr
   */
  public void setPrependAttr(String prependAttr) {
    this.prependAttr = prependAttr;
  }

  /**
   * Gets the property attr.
   *
   * @return the property attr
   */
  public String getPropertyAttr() {
    return propertyAttr;
  }

  /**
   * Sets the property attr.
   *
   * @param propertyAttr
   *          the new property attr
   */
  public void setPropertyAttr(String propertyAttr) {
    this.propertyAttr = propertyAttr;
  }

  /**
   * Gets the compare property attr.
   *
   * @return the compare property attr
   */
  public String getComparePropertyAttr() {
    return comparePropertyAttr;
  }

  /**
   * Sets the compare property attr.
   *
   * @param comparePropertyAttr
   *          the new compare property attr
   */
  public void setComparePropertyAttr(String comparePropertyAttr) {
    this.comparePropertyAttr = comparePropertyAttr;
  }

  /**
   * Gets the compare value attr.
   *
   * @return the compare value attr
   */
  public String getCompareValueAttr() {
    return compareValueAttr;
  }

  /**
   * Sets the compare value attr.
   *
   * @param compareValueAttr
   *          the new compare value attr
   */
  public void setCompareValueAttr(String compareValueAttr) {
    this.compareValueAttr = compareValueAttr;
  }

  /**
   * Gets the open attr.
   *
   * @return the open attr
   */
  public String getOpenAttr() {
    return openAttr;
  }

  /**
   * Sets the open attr.
   *
   * @param openAttr
   *          the new open attr
   */
  public void setOpenAttr(String openAttr) {
    this.openAttr = openAttr;
  }

  /**
   * Gets the close attr.
   *
   * @return the close attr
   */
  public String getCloseAttr() {
    return closeAttr;
  }

  /**
   * Sets the close attr.
   *
   * @param closeAttr
   *          the new close attr
   */
  public void setCloseAttr(String closeAttr) {
    this.closeAttr = closeAttr;
  }

  /**
   * Gets the conjunction attr.
   *
   * @return the conjunction attr
   */
  public String getConjunctionAttr() {
    return conjunctionAttr;
  }

  /**
   * Sets the conjunction attr.
   *
   * @param conjunctionAttr
   *          the new conjunction attr
   */
  public void setConjunctionAttr(String conjunctionAttr) {
    this.conjunctionAttr = conjunctionAttr;
  }

  public void addChild(SqlChild child) {
    if (child instanceof SqlTag) {
      ((SqlTag) child).parent = this;
    }
    children.add(child);
  }

  /**
   * Gets the children.
   *
   * @return the children
   */
  public Iterator getChildren() {
    return children.iterator();
  }

  /**
   * Gets the parent.
   *
   * @return the parent
   */
  public SqlTag getParent() {
    return parent;
  }

  /**
   * Gets the removes the first prepend.
   *
   * @return the removes the first prepend
   */
  public String getRemoveFirstPrepend() {
    return removeFirstPrepend;
  }

  /**
   * Sets the removes the first prepend.
   *
   * @param removeFirstPrepend
   *          the new removes the first prepend
   */
  public void setRemoveFirstPrepend(String removeFirstPrepend) {
    this.removeFirstPrepend = removeFirstPrepend;
  }

  /**
   * Checks if is post parse required.
   *
   * @return Returns the postParseRequired.
   */
  public boolean isPostParseRequired() {
    return postParseRequired;
  }

  /**
   * Sets the post parse required.
   *
   * @param iterateAncestor
   *          The postParseRequired to set.
   */
  public void setPostParseRequired(boolean iterateAncestor) {
    this.postParseRequired = iterateAncestor;
  }
}
