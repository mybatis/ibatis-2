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
package testdomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NestedIterateParameterObject {

  private final List<Integer> idList;
  private final List<String> lastNames;
  private final List<String> firstNames;
  private final List<AndCondition> orConditions;

  public NestedIterateParameterObject() {
    this.idList = new ArrayList<>();
    this.orConditions = new ArrayList<>();
    this.lastNames = new ArrayList<>();
    this.firstNames = new ArrayList<>();
  }

  public List<Integer> getIdList() {
    return this.idList;
  }

  public void addId(final Integer id) {
    this.idList.add(id);
  }

  public List<AndCondition> getOrConditions() {
    return this.orConditions;
  }

  public void addOrCondition(final AndCondition andCondition) {
    this.orConditions.add(andCondition);
  }

  public static class AndCondition {
    private final List<Map<String, Object>> conditions;

    public AndCondition() {
      this.conditions = new ArrayList<>();
    }

    public List<Map<String, Object>> getConditions() {
      return this.conditions;
    }

    public void addCondition(final String condition, final Object value, final Boolean include) {
      final Map<String, Object> map = new HashMap<>();
      map.put("condition", condition);
      map.put("value", value);
      map.put("include", include);
      this.conditions.add(map);
    }
  }

  public List<String> getFirstNames() {
    return this.firstNames;
  }

  public void addFirstName(final String firstName) {
    this.firstNames.add(firstName);
  }

  public List<String> getLastNames() {
    return this.lastNames;
  }

  public void addLastName(final String lastName) {
    this.lastNames.add(lastName);
  }
}
