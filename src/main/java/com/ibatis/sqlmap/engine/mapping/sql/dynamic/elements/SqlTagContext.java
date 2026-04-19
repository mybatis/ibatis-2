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

import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMapping;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class SqlTagContext.
 */
public class SqlTagContext {

  /** The sw. */
  private StringWriter sw;

  /** The out. */
  private PrintWriter out;

  /** The attributes. */
  private HashMap attributes;

  /** The remove first prepend stack. */
  private LinkedList removeFirstPrependStack;

  /** The iterate context stack. */
  private LinkedList iterateContextStack;

  /** The parameter mappings. */
  private ArrayList parameterMappings = new ArrayList<>();

  /**
   * Instantiates a new sql tag context.
   */
  public SqlTagContext() {
    sw = new StringWriter();
    out = new PrintWriter(sw);
    attributes = new HashMap<>();
    removeFirstPrependStack = new LinkedList();
    iterateContextStack = new LinkedList();
  }

  /**
   * Gets the writer.
   *
   * @return the writer
   */
  public PrintWriter getWriter() {
    return out;
  }

  /**
   * Gets the body text.
   *
   * @return the body text
   */
  public String getBodyText() {
    out.flush();
    return sw.getBuffer().toString();
  }

  /**
   * Sets the attribute.
   *
   * @param key
   *          the key
   * @param value
   *          the value
   */
  public void setAttribute(Object key, Object value) {
    attributes.put(key, value);
  }

  /**
   * Gets the attribute.
   *
   * @param key
   *          the key
   *
   * @return the attribute
   */
  public Object getAttribute(Object key) {
    return attributes.get(key);
  }

  /**
   * Adds the parameter mapping.
   *
   * @param mapping
   *          the mapping
   */
  public void addParameterMapping(ParameterMapping mapping) {
    parameterMappings.add(mapping);
  }

  /**
   * Gets the parameter mappings.
   *
   * @return the parameter mappings
   */
  public List getParameterMappings() {
    return parameterMappings;
  }

  /**
   * Checks if is empty remove firt prepend.
   *
   * @return true, if is empty remove firt prepend
   */
  public boolean isEmptyRemoveFirtPrepend() {
    return removeFirstPrependStack.size() <= 0;
  }

  /**
   * examine the value of the top RemoveFirstPrependMarker object on the stack.
   *
   * @param sqlTag
   *          the sql tag
   *
   * @return was the first prepend removed
   */
  public boolean peekRemoveFirstPrependMarker(SqlTag sqlTag) {

    RemoveFirstPrependMarker removeFirstPrepend = (RemoveFirstPrependMarker) removeFirstPrependStack.get(1);

    return removeFirstPrepend.isRemoveFirstPrepend();
  }

  /**
   * pop the first RemoveFirstPrependMarker once the recursion is on it's way out of the recursion loop and return it's
   * internal value.
   *
   * @param tag
   *          the tag
   */
  public void popRemoveFirstPrependMarker(SqlTag tag) {

    RemoveFirstPrependMarker removeFirstPrepend = (RemoveFirstPrependMarker) removeFirstPrependStack.getFirst();

    if (tag == removeFirstPrepend.getSqlTag()) {
      removeFirstPrependStack.removeFirst();
    }
  }

  /**
   * push a new RemoveFirstPrependMarker object with the specified internal state.
   *
   * @param tag
   *          the tag
   */
  public void pushRemoveFirstPrependMarker(SqlTag tag) {

    if (tag.getHandler() instanceof DynamicTagHandler) {
      // this was added to retain default behavior
      if (tag.isPrependAvailable()) {
        removeFirstPrependStack.addFirst(new RemoveFirstPrependMarker(tag, true));
      } else {
        removeFirstPrependStack.addFirst(new RemoveFirstPrependMarker(tag, false));
      }
    } else if ("true".equals(tag.getRemoveFirstPrepend()) || "iterate".equals(tag.getRemoveFirstPrepend())) {
      // you must be specific about the removal otherwise it
      // will function as ibatis has always functioned and add
      // the prepend
      removeFirstPrependStack.addFirst(new RemoveFirstPrependMarker(tag, true));
    } else if (!tag.isPrependAvailable() && !"true".equals(tag.getRemoveFirstPrepend())
        && !"iterate".equals(tag.getRemoveFirstPrepend()) && tag.getParent() != null) {
      // if no prepend or removeFirstPrepend is specified
      // we need to look to the parent tag for default values
      if ("true".equals(tag.getParent().getRemoveFirstPrepend())
          || "iterate".equals(tag.getParent().getRemoveFirstPrepend())) {
        removeFirstPrependStack.addFirst(new RemoveFirstPrependMarker(tag, true));
      }
    } else {
      removeFirstPrependStack.addFirst(new RemoveFirstPrependMarker(tag, false));
    }

  }

  /**
   * set a new internal state for top RemoveFirstPrependMarker object.
   */
  public void disableRemoveFirstPrependMarker() {
    ((RemoveFirstPrependMarker) removeFirstPrependStack.get(1)).setRemoveFirstPrepend(false);
  }

  /**
   * Re enable remove first prepend marker.
   */
  public void reEnableRemoveFirstPrependMarker() {
    ((RemoveFirstPrependMarker) removeFirstPrependStack.get(0)).setRemoveFirstPrepend(true);
  }

  /**
   * iterate context is stored here for nested dynamic tags in the body of the iterate tag.
   *
   * @param iterateContext
   *          the iterate context
   */
  public void pushIterateContext(IterateContext iterateContext) {
    iterateContextStack.addFirst(iterateContext);
  }

  /**
   * iterate context is removed here from the stack when iterate tag is finished being processed.
   *
   * @return the top element of the context stack
   */
  public IterateContext popIterateContext() {
    IterateContext retVal = null;
    if (!iterateContextStack.isEmpty()) {
      retVal = (IterateContext) iterateContextStack.removeFirst();
    }
    return retVal;
  }

  /**
   * iterate context is removed here from the stack when iterate tag is finished being processed.
   *
   * @return the top element on the context stack
   */
  public IterateContext peekIterateContext() {
    IterateContext retVal = null;
    if (!iterateContextStack.isEmpty()) {
      retVal = (IterateContext) iterateContextStack.getFirst();
    }
    return retVal;
  }

}

/**
 * This inner class i used strictly to house whether the removeFirstPrepend has been used in a particular nested
 * situation.
 *
 * @author Brandon Goodin
 */
class RemoveFirstPrependMarker {

  private boolean removeFirstPrepend;
  private SqlTag tag;

  /**
   *
   */
  public RemoveFirstPrependMarker(SqlTag tag, boolean removeFirstPrepend) {
    this.removeFirstPrepend = removeFirstPrepend;
    this.tag = tag;
  }

  /**
   * @return Returns the removeFirstPrepend.
   */
  public boolean isRemoveFirstPrepend() {
    return removeFirstPrepend;
  }

  /**
   * @param removeFirstPrepend
   *          The removeFirstPrepend to set.
   */
  public void setRemoveFirstPrepend(boolean removeFirstPrepend) {
    this.removeFirstPrepend = removeFirstPrepend;
  }

  /**
   * @return Returns the sqlTag.
   */
  public SqlTag getSqlTag() {
    return tag;
  }

}
