package com.ibatis.sqlmap.engine.builder.xml;

import com.ibatis.common.xml.*;
import com.ibatis.common.resources.*;
import com.ibatis.sqlmap.engine.config.*;
import com.ibatis.sqlmap.engine.mapping.statement.*;
import com.ibatis.sqlmap.client.*;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;

import java.util.Properties;

public class SqlStatementParser {

  private XmlParserState state;

  public SqlStatementParser(XmlParserState config) {
    this.state = config;
  }

  public void parseGeneralStatement(Node node, MappedStatement statement) {

    // get attributes
    Properties attributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
    String id = attributes.getProperty("id");
    String parameterMapName = state.applyNamespace(attributes.getProperty("parameterMap"));
    String parameterClassName = attributes.getProperty("parameterClass");
    String resultMapName = attributes.getProperty("resultMap");
    String resultClassName = attributes.getProperty("resultClass");
    String cacheModelName = state.applyNamespace(attributes.getProperty("cacheModel"));
    String xmlResultName = attributes.getProperty("xmlResultName");
    String resultSetType = attributes.getProperty("resultSetType");
    String fetchSize = attributes.getProperty("fetchSize");
    String allowRemapping = attributes.getProperty("remapResults");
    String timeout = attributes.getProperty("timeout");

    if (state.isUseStatementNamespaces()) {
      id = state.applyNamespace(id);
    }
    String[] additionalResultMapNames = null;
    if (resultMapName != null) {
      additionalResultMapNames = state.getAllButFirstToken(resultMapName);
      resultMapName = state.getFirstToken(resultMapName);
      resultMapName = state.applyNamespace(resultMapName);
      for (int i = 0; i < additionalResultMapNames.length; i++) {
        additionalResultMapNames[i] = state.applyNamespace(additionalResultMapNames[i]);
      }
    }

    String[] additionalResultClassNames = null;
    if (resultClassName != null) {
      additionalResultClassNames = state.getAllButFirstToken(resultClassName);
      resultClassName = state.getFirstToken(resultClassName);
    }
    Class[] additionalResultClasses = null;
    if (additionalResultClassNames != null) {
      additionalResultClasses = new Class[additionalResultClassNames.length];
      for (int i = 0; i < additionalResultClassNames.length; i++) {
        additionalResultClasses[i] = resolveClass(additionalResultClassNames[i]);
      }
    }

    state.getConfig().getErrorContext().setMoreInfo("Check the parameter class.");
    Class parameterClass = resolveClass(parameterClassName);

    state.getConfig().getErrorContext().setMoreInfo("Check the result class.");
    Class resultClass = resolveClass(resultClassName);

    Integer timeoutInt = timeout == null ? null : new Integer(timeout);
    Integer fetchSizeInt = fetchSize == null ? null : new Integer(fetchSize);
    boolean allowRemappingBool = "true".equals(allowRemapping);

    MappedStatementConfig statementConf = state.getConfig().newMappedStatementConfig(id, statement,
        new XMLSqlSource(state, node), parameterMapName, parameterClass, resultMapName, additionalResultMapNames,
        resultClass, additionalResultClasses, resultSetType, fetchSizeInt, allowRemappingBool, timeoutInt, cacheModelName,
        xmlResultName);

    findAndParseSelectKey(node, statementConf);
  }

  private Class resolveClass(String resultClassName) {
    try {
      if (resultClassName != null) {
        return Resources.classForName(state.getConfig().getTypeHandlerFactory().resolveAlias(resultClassName));
      } else {
        return null;
      }
    } catch (ClassNotFoundException e) {
      throw new SqlMapException("Error.  Could not initialize class.  Cause: " + e, e);
    }
  }

  private void findAndParseSelectKey(Node node, MappedStatementConfig config) {
      state.getConfig().getErrorContext().setActivity("parsing select key tags");
      boolean foundSQLFirst = false;
      NodeList children = node.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        Node child = children.item(i);
        if (child.getNodeType() == Node.CDATA_SECTION_NODE
            || child.getNodeType() == Node.TEXT_NODE) {
          String data = ((CharacterData) child).getData();
          if (data.trim().length() > 0) {
            foundSQLFirst = true;
          }
        } else if (child.getNodeType() == Node.ELEMENT_NODE
            && "selectKey".equals(child.getNodeName())) {
          Properties attributes = NodeletUtils.parseAttributes(child, state.getGlobalProps());
          String keyPropName = attributes.getProperty("keyProperty");
          String resultClassName = attributes.getProperty("resultClass");
          String type = attributes.getProperty("type");
          config.setSelectKeyStatement(new XMLSqlSource(state, child), resultClassName, keyPropName, foundSQLFirst, type);
          break;
        }
      }
      state.getConfig().getErrorContext().setMoreInfo(null);
    
  }


}
