/*
 * Copyright 2004-2021 the original author or authors.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniElement {

  private String name;
  private String bodyContent;
  private MiniElement parent;
  private List<MiniAttribute> attributeList = new ArrayList<MiniAttribute>(1);
  private List<MiniElement> elementList = new ArrayList<MiniElement>();

  private Map<String, MiniAttribute> attributeMap = new HashMap<String, MiniAttribute>(1);
  private Map<String, MiniElement> elementMap = new HashMap<String, MiniElement>();

  public MiniElement() {
  }

  public MiniElement(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBodyContent() {
    return bodyContent;
  }

  public void setBodyContent(String bodyContent) {
    this.bodyContent = bodyContent;
  }

  public MiniElement getParent() {
    return parent;
  }

  public void setParent(MiniElement parent) {
    this.parent = parent;
  }

  public void addAttribute(MiniAttribute attribute) {
    attributeList.add(attribute);
    attributeMap.put(attribute.getName(), attribute);
  }

  public MiniAttribute getAttribute(int i) {
    return attributeList.get(i);
  }

  public MiniAttribute getAttribute(String name) {
    return attributeMap.get(name);
  }

  public MiniAttribute removeAttribute(int i) {
    return attributeList.remove(i);
  }

  public int getAttributeCount() {
    return attributeList.size();
  }

  public void addElement(MiniElement element) {
    elementList.add(element);
    element.setParent(this);
    elementMap.put(element.getName(), element);
  }

  public MiniElement getElement(int i) {
    return elementList.get(i);
  }

  public MiniElement getElement(String name) {
    return elementMap.get(name);
  }

  public MiniElement removeElement(int i) {
    return elementList.remove(i);
  }

  public int getElementCount() {
    return elementList.size();
  }

}
