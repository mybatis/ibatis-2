/**
 * Copyright 2004-2020 the original author or authors.
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
package xmltester;

import java.util.StringTokenizer;

public class MiniDom {

  private static final String TAB = "  ";

  private MiniElement rootElement;
  private boolean ignoreRoot;

  public MiniDom() {
  }

  public MiniDom(String name) {
    rootElement = new MiniElement(name);
  }

  public MiniElement getRootElement() {
    return rootElement;
  }

  public void setRootElement(MiniElement rootElement) {
    this.rootElement = rootElement;
  }

  public boolean isIgnoreRoot() {
    return ignoreRoot;
  }

  public void setIgnoreRoot(boolean ignoreRoot) {
    this.ignoreRoot = ignoreRoot;
  }

  public String getAttribute(String name) {
    StringTokenizer parser = new StringTokenizer(name, ".");
    MiniElement element = rootElement;
    String token = name;
    while (parser.countTokens() > 1) {
      token = parser.nextToken();
      MiniElement child = element.getElement(token);
      if (child == null) {
        return null;
      }
      element = child;
    }
    token = parser.nextToken();
    MiniAttribute attribute = element.getAttribute(token);
    if (attribute == null) {
      return null;
    } else {
      return attribute.getValue();
    }
  }

  public void setAttribute(String name, String value) {
    StringTokenizer parser = new StringTokenizer(name, ".");
    MiniElement element = rootElement;
    String token = name;
    while (parser.countTokens() > 1) {
      token = parser.nextToken();
      MiniElement child = element.getElement(token);
      if (child == null) {
        child = new MiniElement(token);
        element.addElement(child);
      }
      element = child;
    }
    token = parser.nextToken();
    MiniAttribute attribute = element.getAttribute(token);
    if (attribute == null) {
      attribute = new MiniAttribute(token, value);
      element.addAttribute(attribute);
    } else {
      attribute.setValue(value);
    }
  }

  public String getValue(String name) {
    StringTokenizer parser = new StringTokenizer(name, ".");
    MiniElement element = rootElement;
    String token = name;
    while (parser.countTokens() > 0) {
      token = parser.nextToken();
      MiniElement child = element.getElement(token);
      if (child == null) {
        return null;
      }
      element = child;
    }
    return element.getBodyContent();
  }

  public void setValue(String name, String value) {
    StringTokenizer parser = new StringTokenizer(name, ".");
    MiniElement element = rootElement;
    String token = name;
    while (parser.countTokens() > 0) {
      token = parser.nextToken();
      MiniElement child = element.getElement(token);
      if (child == null) {
        child = new MiniElement(token);
        element.addElement(child);
      }
      element = child;
    }
    element.setBodyContent(value);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    printElement(builder, rootElement, 0, true);
    return builder.toString();
  }

  private void printElement(StringBuilder builder, MiniElement element, int tabLevel, boolean isRoot) {
    if (!isRoot || (isRoot && !ignoreRoot)) {
      printTabs(builder, tabLevel);
      printElementOpen(builder, element);
    }
    printElementBody(builder, element);
    boolean hasElements = false;
    for (int i = 0, n = element.getElementCount(); i < n; i++) {
      hasElements = true;
      builder.append("\r\n");
      printElement(builder, element.getElement(i), tabLevel + 1, false);
    }
    if (hasElements) {
      builder.append("\r\n");
      printTabs(builder, tabLevel);
    }
    if (!isRoot || (isRoot && !ignoreRoot)) {
      printElementClose(builder, element);
    }
  }

  private void printTabs(StringBuilder builder, int tabLevel) {
    for (int i = 0; i < tabLevel; i++) {
      builder.append(TAB);
    }
  }

  private void printElementOpen(StringBuilder builder, MiniElement element) {
    builder.append("<");
    builder.append(element.getName());
    for (int i = 0, n = element.getAttributeCount(); i < n; i++) {
      builder.append(" ");
      printAttribute(builder, element.getAttribute(i));
    }
    builder.append(">");
  }

  private void printElementBody(StringBuilder builder, MiniElement element) {
    String bodyContent = element.getBodyContent();
    if (bodyContent != null) {
      builder.append(bodyContent);
    }
  }

  private void printElementClose(StringBuilder builder, MiniElement element) {
    builder.append("</");
    builder.append(element.getName());
    builder.append(">");
  }

  private void printAttribute(StringBuilder builder, MiniAttribute attribute) {
    builder.append(attribute.getName());
    builder.append("=\"");
    builder.append(attribute.getValue());
    builder.append("\"");
  }

}
