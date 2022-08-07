/*
 * Copyright 2004-2021 the original author or authors.
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
package testdomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NestedIterateParameterObject {

  private List<Integer> idList;
  private List<String> lastNames;
  private List<String> firstNames;
  private List<AndCondition> orConditions;

  public NestedIterateParameterObject() {
    super();
    idList = new ArrayList<Integer>();
    orConditions = new ArrayList<AndCondition>();
    lastNames = new ArrayList<String>();
    firstNames = new ArrayList<String>();
  }

  public List<Integer> getIdList() {
    return idList;
  }

  public void addId(Integer id) {
    idList.add(id);
  }

  public List<AndCondition> getOrConditions() {
    return orConditions;
  }

  public void addOrCondition(AndCondition andCondition) {
    orConditions.add(andCondition);
  }

  public static class AndCondition {
    private List<Map<String, Object>> conditions;

    public AndCondition() {
      super();
      conditions = new ArrayList<Map<String, Object>>();
    }

    public List<Map<String, Object>> getConditions() {
      return conditions;
    }

    public void addCondition(String condition, Object value, Boolean include) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("condition", condition);
      map.put("value", value);
      map.put("include", include);
      conditions.add(map);
    }
  }

  public List<String> getFirstNames() {
    return firstNames;
  }

  public void addFirstName(String firstName) {
    firstNames.add(firstName);
  }

  public List<String> getLastNames() {
    return lastNames;
  }

  public void addLastName(String lastName) {
    lastNames.add(lastName);
  }
}
