package com.ibatis.sqlmap.engine.mapping.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapException;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;
import com.ibatis.sqlmap.engine.mapping.sql.SqlText;
import com.ibatis.sqlmap.engine.type.CustomTypeHandler;
import com.ibatis.sqlmap.engine.type.DomTypeMarker;
import com.ibatis.sqlmap.engine.type.TypeHandler;
import com.ibatis.sqlmap.engine.type.TypeHandlerFactory;

public class InlineParameterMapParser {

  private static final Probe PROBE = ProbeFactory.getProbe();
  private static final String PARAMETER_TOKEN = "#";
  private static final String PARAM_DELIM = ":";

  public SqlText parseInlineParameterMap(TypeHandlerFactory typeHandlerFactory, String sqlStatement) {
    return parseInlineParameterMap(typeHandlerFactory, sqlStatement, null);
  }

  public SqlText parseInlineParameterMap(TypeHandlerFactory typeHandlerFactory, String sqlStatement, Class parameterClass) {

    String newSql = sqlStatement;

    List mappingList = new ArrayList();

    StringTokenizer parser = new StringTokenizer(sqlStatement, PARAMETER_TOKEN, true);
    StringBuffer newSqlBuffer = new StringBuffer();

    String token = null;
    String lastToken = null;
    while (parser.hasMoreTokens()) {
      token = parser.nextToken();
      if (PARAMETER_TOKEN.equals(lastToken)) {
        if (PARAMETER_TOKEN.equals(token)) {
          newSqlBuffer.append(PARAMETER_TOKEN);
          token = null;
        } else {
          ParameterMapping mapping = null;
          if (token.indexOf(PARAM_DELIM) > -1) {
            mapping = oldParseMapping(token, parameterClass, typeHandlerFactory);
          } else {
            mapping = newParseMapping(token, parameterClass, typeHandlerFactory);
          }

          mappingList.add(mapping);
          newSqlBuffer.append("?");
          boolean hasMoreTokens = parser.hasMoreTokens();
          if (hasMoreTokens)
              token = parser.nextToken();
          if (!hasMoreTokens || !PARAMETER_TOKEN.equals(token)) {
              throw new SqlMapException(
        	      "Unterminated inline parameter in mapped statement near '"
        	      + newSqlBuffer.toString() + "'");
          }
          token = null;
        }
      } else {
        if (!PARAMETER_TOKEN.equals(token)) {
          newSqlBuffer.append(token);
        }
      }

      lastToken = token;
    }

    newSql = newSqlBuffer.toString();

    ParameterMapping[] mappingArray = (ParameterMapping[]) mappingList.toArray(new ParameterMapping[mappingList.size()]);

    SqlText sqlText = new SqlText();
    sqlText.setText(newSql);
    sqlText.setParameterMappings(mappingArray);
    return sqlText;
  }

  private ParameterMapping newParseMapping(String token, Class parameterClass, TypeHandlerFactory typeHandlerFactory) {
    ParameterMapping mapping = new ParameterMapping();

    // #propertyName,javaType=string,jdbcType=VARCHAR,mode=IN,nullValue=N/A,handler=string,numericScale=2#

    StringTokenizer paramParser = new StringTokenizer(token, "=,", false);
    mapping.setPropertyName(paramParser.nextToken());

    while (paramParser.hasMoreTokens()) {
      String field = paramParser.nextToken();
      if (paramParser.hasMoreTokens()) {
        String value = paramParser.nextToken();
        if ("javaType".equals(field)) {
          value = typeHandlerFactory.resolveAlias(value);
          mapping.setJavaTypeName(value);
        } else if ("jdbcType".equals(field)) {
          mapping.setJdbcTypeName(value);
        } else if ("mode".equals(field)) {
          mapping.setMode(value);
        } else if ("nullValue".equals(field)) {
          mapping.setNullValue(value);
        } else if ("handler".equals(field)) {
          try {
            value = typeHandlerFactory.resolveAlias(value);
            Object impl = Resources.instantiate(value);
            if (impl instanceof TypeHandlerCallback) {
              mapping.setTypeHandler(new CustomTypeHandler((TypeHandlerCallback) impl));
            } else if (impl instanceof TypeHandler) {
              mapping.setTypeHandler((TypeHandler) impl);
            } else {
              throw new SqlMapException ("The class " + value + " is not a valid implementation of TypeHandler or TypeHandlerCallback");
            }
          } catch (Exception e) {
            throw new SqlMapException("Error loading class specified by handler field in " + token + ".  Cause: " + e, e);
          }
        } else if ("numericScale".equals(field)) {
          try {
            Integer numericScale = Integer.valueOf(value);
            if (numericScale.intValue() < 0) {
              throw new SqlMapException("Value specified for numericScale must be greater than or equal to zero");
            }
            mapping.setNumericScale(numericScale);
          } catch (NumberFormatException e) {
            throw new SqlMapException("Value specified for numericScale is not a valid Integer");
          }
        } else {
          throw new SqlMapException("Unrecognized parameter mapping field: '" + field + "' in " + token);
        }
      } else {
        throw new SqlMapException("Incorrect inline parameter map format (missmatched name=value pairs): " + token);
      }
    }

    if (mapping.getTypeHandler() == null) {
      TypeHandler handler;
      if (parameterClass == null) {
        handler = typeHandlerFactory.getUnkownTypeHandler();
      } else {
        handler = resolveTypeHandler(typeHandlerFactory, parameterClass, mapping.getPropertyName(), mapping.getJavaTypeName(), mapping.getJdbcTypeName());
      }
      mapping.setTypeHandler(handler);
    }

    return mapping;
  }

  private ParameterMapping oldParseMapping(String token, Class parameterClass, TypeHandlerFactory typeHandlerFactory) {
    ParameterMapping mapping = new ParameterMapping();
    if (token.indexOf(PARAM_DELIM) > -1) {
      StringTokenizer paramParser = new StringTokenizer(token, PARAM_DELIM, true);
      int n1 = paramParser.countTokens();
      if (n1 == 3) {
        String name = paramParser.nextToken();
        paramParser.nextToken(); //ignore ":"
        String type = paramParser.nextToken();
        mapping.setPropertyName(name);
        mapping.setJdbcTypeName(type);
        TypeHandler handler;
        if (parameterClass == null) {
          handler = typeHandlerFactory.getUnkownTypeHandler();
        } else {
          handler = resolveTypeHandler(typeHandlerFactory, parameterClass, name, null, type);
        }
        mapping.setTypeHandler(handler);
        return mapping;
      } else if (n1 >= 5) {
        String name = paramParser.nextToken();
        paramParser.nextToken(); //ignore ":"
        String type = paramParser.nextToken();
        paramParser.nextToken(); //ignore ":"
        String nullValue = paramParser.nextToken();
        while (paramParser.hasMoreTokens()) {
          nullValue = nullValue + paramParser.nextToken();
        }
        mapping.setPropertyName(name);
        mapping.setJdbcTypeName(type);
        mapping.setNullValue(nullValue);
        TypeHandler handler;
        if (parameterClass == null) {
          handler = typeHandlerFactory.getUnkownTypeHandler();
        } else {
          handler = resolveTypeHandler(typeHandlerFactory, parameterClass, name, null, type);
        }
        mapping.setTypeHandler(handler);
        return mapping;
      } else {
        throw new SqlMapException("Incorrect inline parameter map format: " + token);
      }
    } else {
      mapping.setPropertyName(token);
      TypeHandler handler;
      if (parameterClass == null) {
        handler = typeHandlerFactory.getUnkownTypeHandler();
      } else {
        handler = resolveTypeHandler(typeHandlerFactory, parameterClass, token, null, null);
      }
      mapping.setTypeHandler(handler);
      return mapping;
    }
  }

  private TypeHandler resolveTypeHandler(TypeHandlerFactory typeHandlerFactory, Class clazz, String propertyName, String javaType, String jdbcType) {
    TypeHandler handler = null;
    if (clazz == null) {
      // Unknown
      handler = typeHandlerFactory.getUnkownTypeHandler();
    } else if (DomTypeMarker.class.isAssignableFrom(clazz)) {
      // DOM
      handler = typeHandlerFactory.getTypeHandler(String.class, jdbcType);
    } else if (java.util.Map.class.isAssignableFrom(clazz)) {
      // Map
      if (javaType == null) {
        handler = typeHandlerFactory.getUnkownTypeHandler(); //BUG 1012591 - typeHandlerFactory.getTypeHandler(java.lang.Object.class, jdbcType);
      } else {
        try {
          javaType = typeHandlerFactory.resolveAlias(javaType);
          Class javaClass = Resources.classForName(javaType);
          handler = typeHandlerFactory.getTypeHandler(javaClass, jdbcType);
        } catch (Exception e) {
          throw new SqlMapException("Error.  Could not set TypeHandler.  Cause: " + e, e);
        }
      }
    } else if (typeHandlerFactory.getTypeHandler(clazz, jdbcType) != null) {
      // Primitive
      handler = typeHandlerFactory.getTypeHandler(clazz, jdbcType);
    } else {
      // JavaBean
      if (javaType == null) {

        Class type = PROBE.getPropertyTypeForGetter(clazz, propertyName);
        handler = typeHandlerFactory.getTypeHandler(type, jdbcType);

      } else {
        try {
          javaType = typeHandlerFactory.resolveAlias(javaType);
          Class javaClass = Resources.classForName(javaType);
          handler = typeHandlerFactory.getTypeHandler(javaClass, jdbcType);
        } catch (Exception e) {
          throw new SqlMapException("Error.  Could not set TypeHandler.  Cause: " + e, e);
        }
      }
    }
    return handler;
  }


}
