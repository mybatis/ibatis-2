/**
 * Copyright 2004-2015 the original author or authors.
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
package com.ibatis.sqlmap.engine.mapping.result;

import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA. User: cbegin Date: May 13, 2005 Time: 11:11:05 PM To change this template use File |
 * Settings | File Templates.
 */
public class Discriminator {

  private SqlMapExecutorDelegate delegate;
  private ResultMapping resultMapping;
  private Map subMaps;

  public Discriminator(SqlMapExecutorDelegate delegate, ResultMapping resultMapping) {
    this.delegate = delegate;
    this.resultMapping = resultMapping;
  }

  public void setResultMapping(ResultMapping resultMapping) {
    this.resultMapping = resultMapping;
  }

  public ResultMapping getResultMapping() {
    return resultMapping;
  }

  public void addSubMap(String discriminatorValue, String resultMapName) {
    if (subMaps == null) {
      subMaps = new HashMap();
    }
    subMaps.put(discriminatorValue, resultMapName);
  }

  public ResultMap getSubMap(String s) {
    return (ResultMap) subMaps.get(s);
  }

  public void bindSubMaps() {
    if (subMaps != null) {
      Iterator keys = subMaps.keySet().iterator();
      while (keys.hasNext()) {
        Object key = keys.next();
        Object id = subMaps.get(key);
        if (id instanceof String) {
          subMaps.put(key, delegate.getResultMap((String) id));
        }
      }
    }
  }

}
