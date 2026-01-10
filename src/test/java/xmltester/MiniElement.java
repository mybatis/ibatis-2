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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniElement {

  private String name;
  private String bodyContent;
  private MiniElement parent;
  private final List<MiniAttribute> attributeList = new ArrayList<>(1);
  private final List<MiniElement> elementList = new ArrayList<>();

  private final Map<String, MiniAttribute> attributeMap = new HashMap<>(1);
  private final Map<String, MiniElement> elementMap = new HashMap<>();

  public MiniElement() {
  }

  public MiniElement(final String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getBodyContent() {
    return this.bodyContent;
  }

  public void setBodyContent(final String bodyContent) {
    this.bodyContent = bodyContent;
  }

  public MiniElement getParent() {
    return this.parent;
  }

  public void setParent(final MiniElement parent) {
    this.parent = parent;
  }

  public void addAttribute(final MiniAttribute attribute) {
    this.attributeList.add(attribute);
    this.attributeMap.put(attribute.getName(), attribute);
  }

  public MiniAttribute getAttribute(final int i) {
    return this.attributeList.get(i);
  }

  public MiniAttribute getAttribute(final String name) {
    return this.attributeMap.get(name);
  }

  public MiniAttribute removeAttribute(final int i) {
    return this.attributeList.remove(i);
  }

  public int getAttributeCount() {
    return this.attributeList.size();
  }

  public void addElement(final MiniElement element) {
    this.elementList.add(element);
    element.setParent(this);
    this.elementMap.put(element.getName(), element);
  }

  public MiniElement getElement(final int i) {
    return this.elementList.get(i);
  }

  public MiniElement getElement(final String name) {
    return this.elementMap.get(name);
  }

  public MiniElement removeElement(final int i) {
    return this.elementList.remove(i);
  }

  public int getElementCount() {
    return this.elementList.size();
  }

}
