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
package com.ibatis.sqlmap.engine.datasource;

import com.ibatis.sqlmap.client.SqlMapException;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * DataSourceFactory implementation for JNDI.
 */
public class JndiDataSourceFactory implements DataSourceFactory {

  /** The data source. */
  private DataSource dataSource;

  public void initialize(Map properties) {
    try {
      InitialContext initCtx = null;
      Properties context = getContextProperties(properties);

      if (context == null) {
        initCtx = new InitialContext();
      } else {
        initCtx = new InitialContext(context);
      }

      if (properties.containsKey("DataSource")) {
        dataSource = (DataSource) initCtx.lookup((String) properties.get("DataSource"));
      } else if (properties.containsKey("DBJndiContext")) { // LEGACY --Backward compatibility
        dataSource = (DataSource) initCtx.lookup((String) properties.get("DBJndiContext"));
      } else if (properties.containsKey("DBFullJndiContext")) { // LEGACY --Backward
                                                                // compatibility
        dataSource = (DataSource) initCtx.lookup((String) properties.get("DBFullJndiContext"));
      } else if (properties.containsKey("DBInitialContext") && properties.containsKey("DBLookup")) {
        // LEGACY - Backward compatibility
        Context ctx = (Context) initCtx.lookup((String) properties.get("DBInitialContext"));
        dataSource = (DataSource) ctx.lookup((String) properties.get("DBLookup"));
      }

    } catch (NamingException e) {
      throw new SqlMapException("There was an error configuring JndiDataSourceTransactionPool. Cause: " + e, e);
    }
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  /**
   * Gets the context properties.
   *
   * @param allProps
   *          the all props
   *
   * @return the context properties
   */
  private static Properties getContextProperties(Map allProps) {
    final String PREFIX = "context.";
    Properties contextProperties = null;
    Iterator keys = allProps.keySet().iterator();
    while (keys.hasNext()) {
      String key = (String) keys.next();
      String value = (String) allProps.get(key);
      if (key.startsWith(PREFIX)) {
        if (contextProperties == null) {
          contextProperties = new Properties();
        }
        contextProperties.put(key.substring(PREFIX.length()), value);
      }
    }
    return contextProperties;
  }

}
