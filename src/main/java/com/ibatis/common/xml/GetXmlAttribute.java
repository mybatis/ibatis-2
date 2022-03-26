/*
 * Copyright 2004-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibatis.common.xml;

import java.util.Properties;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

// extract class refactoring - ASDC - Assignment3
public class GetXmlAttribute {
  /**
   * Gets the boolean attribute.
   *
   * @param attribs
   *          the attribs
   * @param name
   *          the name
   * @param def
   *          the def
   * @return the boolean attribute
   */
  // move method refactoring - ASDC - Assignment3
  public static boolean getBooleanAttribute(Properties attribs, String name, boolean def) {
    String value = attribs.getProperty(name);
    if (value == null) {
      return def;
    } else {
      return "true".equals(value);
    }
  }

  /**
   * Gets the int attribute.
   *
   * @param attribs
   *          the attribs
   * @param name
   *          the name
   * @param def
   *          the def
   * @return the int attribute
   */
  // move method refactioring - ASDC - Assignment3
  public static int getIntAttribute(Properties attribs, String name, int def) {
    String value = attribs.getProperty(name);
    if (value == null) {
      return def;
    } else {
      return Integer.parseInt(value);
    }
  }

  /**
   * Parses the attributes.
   *
   * @param n
   *          the n
   * @return the properties
   */
  // move method refactoring - ASDC - Assignment3
  public static Properties parseAttributes(Node n) {
    return parseAttributes(n, null);
  }

  /**
   * Parses the attributes.
   *
   * @param n
   *          the n
   * @param variables
   *          the variables
   * @return the properties
   */
  // move method refactoring - ASDC - Assignment3
  public static Properties parseAttributes(Node n, Properties variables) {
    Properties attributes = new Properties();
    NamedNodeMap attributeNodes = n.getAttributes();
    for (int i = 0; i < attributeNodes.getLength(); i++) {
      Node attribute = attributeNodes.item(i);
      String value = NodeletUtils.parsePropertyTokens(attribute.getNodeValue(), variables);
      attributes.put(attribute.getNodeName(), value);
    }
    return attributes;
  }
}
