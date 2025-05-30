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
package com.ibatis.common.jdbc;

import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Wrapper class to simplify use of DBCP.
 */
public class DbcpConfiguration {

  /** The Constant PROBE. */
  private static final Probe PROBE = ProbeFactory.getProbe();

  /** The Constant ADD_DRIVER_PROPS_PREFIX. */
  private static final String ADD_DRIVER_PROPS_PREFIX = "Driver.";

  /** The Constant ADD_DRIVER_PROPS_PREFIX_LENGTH. */
  private static final int ADD_DRIVER_PROPS_PREFIX_LENGTH = ADD_DRIVER_PROPS_PREFIX.length();

  /** The data source. */
  private DataSource dataSource;

  /**
   * Constructor to supply a map of properties.
   *
   * @param properties
   *          - the map of configuration properties
   */
  public DbcpConfiguration(Map properties) {
    try {

      dataSource = legacyDbcpConfiguration(properties);
      if (dataSource == null) {
        dataSource = newDbcpConfiguration(properties);
      }

    } catch (Exception e) {
      throw new RuntimeException("Error initializing DbcpDataSourceFactory.  Cause: " + e, e);
    }
  }

  /**
   * Getter for DataSource.
   *
   * @return The DataSource
   */
  public DataSource getDataSource() {
    return dataSource;
  }

  /**
   * New dbcp configuration.
   *
   * @param map
   *          the map
   *
   * @return the basic data source
   */
  private BasicDataSource newDbcpConfiguration(Map map) {
    BasicDataSource basicDataSource = new BasicDataSource();
    Iterator props = map.keySet().iterator();
    while (props.hasNext()) {
      String propertyName = (String) props.next();
      if (propertyName.startsWith(ADD_DRIVER_PROPS_PREFIX)) {
        String value = (String) map.get(propertyName);
        basicDataSource.addConnectionProperty(propertyName.substring(ADD_DRIVER_PROPS_PREFIX_LENGTH), value);
      } else if (PROBE.hasWritableProperty(basicDataSource, propertyName)) {
        String value = (String) map.get(propertyName);
        Object convertedValue = convertValue(basicDataSource, propertyName, value);
        PROBE.setObject(basicDataSource, propertyName, convertedValue);
      }
    }
    return basicDataSource;
  }

  /**
   * Convert value.
   *
   * @param object
   *          the object
   * @param propertyName
   *          the property name
   * @param value
   *          the value
   *
   * @return the object
   */
  private Object convertValue(Object object, String propertyName, String value) {
    Object convertedValue = value;
    Class targetType = PROBE.getPropertyTypeForSetter(object, propertyName);
    if (targetType == Integer.class || targetType == int.class) {
      convertedValue = Integer.valueOf(value);
    } else if (targetType == Long.class || targetType == long.class) {
      convertedValue = Long.valueOf(value);
    } else if (targetType == Boolean.class || targetType == boolean.class) {
      convertedValue = Boolean.valueOf(value);
    }
    return convertedValue;
  }

  /**
   * Legacy dbcp configuration.
   *
   * @param map
   *          the map
   *
   * @return the basic data source
   */
  private BasicDataSource legacyDbcpConfiguration(Map map) {
    BasicDataSource basicDataSource = null;
    if (map.containsKey("JDBC.Driver")) {
      basicDataSource = new BasicDataSource();
      String driver = (String) map.get("JDBC.Driver");
      String url = (String) map.get("JDBC.ConnectionURL");
      String username = (String) map.get("JDBC.Username");
      String password = (String) map.get("JDBC.Password");
      String validationQuery = (String) map.get("Pool.ValidationQuery");
      String maxActive = (String) map.get("Pool.MaximumActiveConnections");
      String maxIdle = (String) map.get("Pool.MaximumIdleConnections");
      String maxWait = (String) map.get("Pool.MaximumWait");

      basicDataSource.setUrl(url);
      basicDataSource.setDriverClassName(driver);
      basicDataSource.setUsername(username);
      basicDataSource.setPassword(password);

      if (notEmpty(validationQuery)) {
        basicDataSource.setValidationQuery(validationQuery);
      }

      if (notEmpty(maxActive)) {
        basicDataSource.setMaxTotal(Integer.parseInt(maxActive));
      }

      if (notEmpty(maxIdle)) {
        basicDataSource.setMaxIdle(Integer.parseInt(maxIdle));
      }

      if (notEmpty(maxWait)) {
        basicDataSource.setMaxWait(Duration.ofMillis(Integer.parseInt(maxWait)));
      }

      Iterator props = map.keySet().iterator();
      while (props.hasNext()) {
        String propertyName = (String) props.next();
        if (propertyName.startsWith(ADD_DRIVER_PROPS_PREFIX)) {
          String value = (String) map.get(propertyName);
          basicDataSource.addConnectionProperty(propertyName.substring(ADD_DRIVER_PROPS_PREFIX_LENGTH), value);
        }
      }
    }
    return basicDataSource;
  }

  /**
   * Not empty.
   *
   * @param s
   *          the s
   *
   * @return true, if successful
   */
  private boolean notEmpty(String s) {
    return s != null && !s.isEmpty();
  }

}
