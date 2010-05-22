/*
 *  Copyright 2004 Clinton Begin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package xmltester;

import xmltester.MiniAttribute;

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

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    printElement(buffer, rootElement, 0, true);
    return buffer.toString();
  }

  private void printElement(StringBuffer buffer, MiniElement element, int tabLevel, boolean isRoot) {
    if (!isRoot || (isRoot && !ignoreRoot)) {
      printTabs(buffer, tabLevel);
      printElementOpen(buffer, element);
    }
    printElementBody(buffer, element);
    boolean hasElements = false;
    for (int i = 0, n = element.getElementCount(); i < n; i++) {
      hasElements = true;
      buffer.append("\r\n");
      printElement(buffer, element.getElement(i), tabLevel + 1, false);
    }
    if (hasElements) {
      buffer.append("\r\n");
      printTabs(buffer, tabLevel);
    }
    if (!isRoot || (isRoot && !ignoreRoot)) {
      printElementClose(buffer, element);
    }
  }

  private void printTabs(StringBuffer buffer, int tabLevel) {
    for (int i = 0; i < tabLevel; i++) {
      buffer.append(TAB);
    }
  }

  private void printElementOpen(StringBuffer buffer, MiniElement element) {
    buffer.append("<");
    buffer.append(element.getName());
    for (int i = 0, n = element.getAttributeCount(); i < n; i++) {
      buffer.append(" ");
      printAttribute(buffer, element.getAttribute(i));
    }
    buffer.append(">");
  }

  private void printElementBody(StringBuffer buffer, MiniElement element) {
    String bodyContent = element.getBodyContent();
    if (bodyContent != null) {
      buffer.append(bodyContent);
    }
  }

  private void printElementClose(StringBuffer buffer, MiniElement element) {
    buffer.append("</");
    buffer.append(element.getName());
    buffer.append(">");
  }

  private void printAttribute(StringBuffer buffer, MiniAttribute attribute) {
    buffer.append(attribute.getName());
    buffer.append("=\"");
    buffer.append(attribute.getValue());
    buffer.append("\"");
  }

}
