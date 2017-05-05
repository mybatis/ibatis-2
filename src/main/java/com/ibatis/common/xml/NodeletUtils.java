/**
 * Copyright 2004-2017 the original author or authors.
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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Properties;

public class NodeletUtils {

  public static boolean getBooleanAttribute(Properties attribs, String name, boolean def) {
    String value = attribs.getProperty(name);
    if (value == null) {
      return def;
    } else {
      return "true".equals(value);
    }
  }

  public static int getIntAttribute(Properties attribs, String name, int def) {
    String value = attribs.getProperty(name);
    if (value == null) {
      return def;
    } else {
      return Integer.parseInt(value);
    }
  }

  public static Properties parseAttributes(Node n) {
    return parseAttributes(n, null);
  }

  public static Properties parseAttributes(Node n, Properties variables) {
    Properties attributes = new Properties();
    NamedNodeMap attributeNodes = n.getAttributes();
    for (int i = 0; i < attributeNodes.getLength(); i++) {
      Node attribute = attributeNodes.item(i);
      String value = parsePropertyTokens(attribute.getNodeValue(), variables);
      attributes.put(attribute.getNodeName(), value);
    }
    return attributes;
  }

  public static String parsePropertyTokens(String string, Properties variables) {
    final String OPEN = "${";
    final String CLOSE = "}";

    String newString = string;
    if (newString != null && variables != null) {
      int start = newString.indexOf(OPEN);
      int end = newString.indexOf(CLOSE);

      while (start > -1 && end > start) {
        String prepend = newString.substring(0, start);
        String append = newString.substring(end + CLOSE.length());
        String propName = newString.substring(start + OPEN.length(), end);
        String propValue = variables.getProperty(propName);
        if (propValue == null) {
          newString = prepend + propName + append;
        } else {
          newString = prepend + propValue + append;
        }
        start = newString.indexOf(OPEN);
        end = newString.indexOf(CLOSE);
      }
    }
    return newString;
  }

}
