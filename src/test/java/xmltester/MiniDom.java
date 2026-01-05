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
package xmltester;

import java.util.StringTokenizer;

public class MiniDom {

  private static final String TAB = "  ";

  private MiniElement rootElement;
  private boolean ignoreRoot;

  public MiniDom() {
  }

  public MiniDom(final String name) {
    this.rootElement = new MiniElement(name);
  }

  public MiniElement getRootElement() {
    return this.rootElement;
  }

  public void setRootElement(final MiniElement rootElement) {
    this.rootElement = rootElement;
  }

  public boolean isIgnoreRoot() {
    return this.ignoreRoot;
  }

  public void setIgnoreRoot(final boolean ignoreRoot) {
    this.ignoreRoot = ignoreRoot;
  }

  public String getAttribute(final String name) {
    final StringTokenizer parser = new StringTokenizer(name, ".");
    MiniElement element = this.rootElement;
    String token = name;
    while (parser.countTokens() > 1) {
      token = parser.nextToken();
      final MiniElement child = element.getElement(token);
      if (child == null) {
        return null;
      }
      element = child;
    }
    token = parser.nextToken();
    final MiniAttribute attribute = element.getAttribute(token);
    if (attribute == null) {
      return null;
    }
    return attribute.getValue();
  }

  public void setAttribute(final String name, final String value) {
    final StringTokenizer parser = new StringTokenizer(name, ".");
    MiniElement element = this.rootElement;
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

  public String getValue(final String name) {
    final StringTokenizer parser = new StringTokenizer(name, ".");
    MiniElement element = this.rootElement;
    String token = name;
    while (parser.countTokens() > 0) {
      token = parser.nextToken();
      final MiniElement child = element.getElement(token);
      if (child == null) {
        return null;
      }
      element = child;
    }
    return element.getBodyContent();
  }

  public void setValue(final String name, final String value) {
    final StringTokenizer parser = new StringTokenizer(name, ".");
    MiniElement element = this.rootElement;
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
    final StringBuilder builder = new StringBuilder();
    this.printElement(builder, this.rootElement, 0, true);
    return builder.toString();
  }

  private void printElement(final StringBuilder builder, final MiniElement element, final int tabLevel,
      final boolean isRoot) {
    if (!isRoot || isRoot && !this.ignoreRoot) {
      this.printTabs(builder, tabLevel);
      this.printElementOpen(builder, element);
    }
    this.printElementBody(builder, element);
    boolean hasElements = false;
    for (int i = 0, n = element.getElementCount(); i < n; i++) {
      hasElements = true;
      builder.append("\r\n");
      this.printElement(builder, element.getElement(i), tabLevel + 1, false);
    }
    if (hasElements) {
      builder.append("\r\n");
      this.printTabs(builder, tabLevel);
    }
    if (!isRoot || isRoot && !this.ignoreRoot) {
      this.printElementClose(builder, element);
    }
  }

  private void printTabs(final StringBuilder builder, final int tabLevel) {
    for (int i = 0; i < tabLevel; i++) {
      builder.append(MiniDom.TAB);
    }
  }

  private void printElementOpen(final StringBuilder builder, final MiniElement element) {
    builder.append("<");
    builder.append(element.getName());
    for (int i = 0, n = element.getAttributeCount(); i < n; i++) {
      builder.append(" ");
      this.printAttribute(builder, element.getAttribute(i));
    }
    builder.append(">");
  }

  private void printElementBody(final StringBuilder builder, final MiniElement element) {
    final String bodyContent = element.getBodyContent();
    if (bodyContent != null) {
      builder.append(bodyContent);
    }
  }

  private void printElementClose(final StringBuilder builder, final MiniElement element) {
    builder.append("</");
    builder.append(element.getName());
    builder.append(">");
  }

  private void printAttribute(final StringBuilder builder, final MiniAttribute attribute) {
    builder.append(attribute.getName());
    builder.append("=\"");
    builder.append(attribute.getValue());
    builder.append("\"");
  }

}
