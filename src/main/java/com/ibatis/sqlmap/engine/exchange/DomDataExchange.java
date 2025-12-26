/*
 * Copyright 2004-2025 the original author or authors.
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
package com.ibatis.sqlmap.engine.exchange;

import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;
import com.ibatis.sqlmap.client.SqlMapException;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMapping;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMapping;
import com.ibatis.sqlmap.engine.scope.StatementScope;

import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

/**
 * A DataExchange implemtation for working with DOM objects.
 */
public class DomDataExchange extends BaseDataExchange implements DataExchange {

  /**
   * Constructor for the factory.
   *
   * @param dataExchangeFactory
   *          - the factory
   */
  public DomDataExchange(DataExchangeFactory dataExchangeFactory) {
    super(dataExchangeFactory);
  }

  @Override
  public void initialize(Map properties) {
  }

  @Override
  public Object[] getData(StatementScope statementScope, ParameterMap parameterMap, Object parameterObject) {
    Probe probe = ProbeFactory.getProbe(parameterObject);

    ParameterMapping[] mappings = parameterMap.getParameterMappings();
    Object[] values = new Object[mappings.length];

    for (int i = 0; i < mappings.length; i++) {
      values[i] = probe.getObject(parameterObject, mappings[i].getPropertyName());
    }

    return values;
  }

  @Override
  public Object setData(StatementScope statementScope, ResultMap resultMap, Object resultObject, Object[] values) {

    String name = resultMap.getXmlName();
    if (name == null) {
      name = "result";
    }

    if (resultObject == null) {
      try {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        doc.appendChild(doc.createElement(name));
        resultObject = doc;
      } catch (ParserConfigurationException e) {
        throw new SqlMapException("Error creating new Document for DOM result.  Cause: " + e, e);
      }
    }

    Probe probe = ProbeFactory.getProbe(resultObject);

    ResultMapping[] mappings = resultMap.getResultMappings();

    for (int i = 0; i < mappings.length; i++) {
      if (values[i] != null) {
        probe.setObject(resultObject, mappings[i].getPropertyName(), values[i]);
      }
    }

    return resultObject;
  }

  @Override
  public Object setData(StatementScope statementScope, ParameterMap parameterMap, Object parameterObject,
      Object[] values) {
    Probe probe = ProbeFactory.getProbe(parameterObject);

    ParameterMapping[] mappings = parameterMap.getParameterMappings();

    for (int i = 0; i < mappings.length; i++) {
      if (values[i] != null && mappings[i].isOutputAllowed()) {
        probe.setObject(parameterObject, mappings[i].getPropertyName(), values[i]);
      }
    }

    return parameterObject;
  }

}
