/*
 * Copyright 2004-2024 the original author or authors.
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
package com.ibatis.sqlmap.engine.mapping.statement;

import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.ibatis.sqlmap.engine.type.XmlTypeMarker;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * Class to manager row handler access.
 */
public class RowHandlerCallback {

  /** The row handler. */
  private RowHandler rowHandler;

  /** The result map. */
  private ResultMap resultMap;

  /** The result object. */
  private Object resultObject;

  /**
   * Constructor.
   *
   * @param resultMap
   *          - the result map
   * @param resultObject
   *          - the result object
   * @param rowHandler
   *          - the row handler object
   */
  public RowHandlerCallback(ResultMap resultMap, Object resultObject, RowHandler rowHandler) {
    this.rowHandler = rowHandler;
    this.resultMap = resultMap;
    this.resultObject = resultObject;
  }

  /**
   * Prepares the row object, and passes it to the row handler.
   *
   * @param statementScope
   *          - the request scope
   * @param results
   *          - the result data
   * @param rs
   *          the rs
   *
   * @throws SQLException
   *           the SQL exception
   */
  public void handleResultObject(StatementScope statementScope, Object[] results, ResultSet rs) throws SQLException {
    Object object;

    statementScope.setCurrentNestedKey(null);
    object = resultMap.resolveSubMap(statementScope, rs).setResultObjectValues(statementScope, resultObject, results);

    if (object != ResultMap.NO_VALUE) {
      // XML Only special processing. (converts elements to string for easy insertion).
      int stackDepth = statementScope.getSession().getRequestStackDepth();
      if (stackDepth == 1) {
        Class targetType = statementScope.getResultMap().getResultClass();
        if (XmlTypeMarker.class.isAssignableFrom(targetType) && object instanceof Document) {
          object = documentToString((Document) object);
        }
      }

      rowHandler.handleRow(object);
    }
  }

  /**
   * Document to string.
   *
   * @param document
   *          the document
   *
   * @return the string
   */
  private String documentToString(Document document) {
    String s = null;

    try {
      TransformerFactory tFactory = TransformerFactory.newInstance();
      tFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      tFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
      Transformer transformer = tFactory.newTransformer();

      DOMSource source = new DOMSource(document);
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);
      transformer.transform(source, result);
      s = writer.getBuffer().toString();

    } catch (TransformerException e) {
      throw new RuntimeException("Error occurred.  Cause: " + e, e);
    }

    return s;
  }

  /**
   * Gets the row handler.
   *
   * @return the row handler
   */
  public RowHandler getRowHandler() {
    return rowHandler;
  }

}
