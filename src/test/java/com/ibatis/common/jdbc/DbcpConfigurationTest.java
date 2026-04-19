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
package com.ibatis.common.jdbc;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DbcpConfigurationTest {

  @Test
  void shouldBuildLegacyConfiguration() {
    Map<String, String> properties = new HashMap<>();
    properties.put("JDBC.Driver", "org.hsqldb.jdbcDriver");
    properties.put("JDBC.ConnectionURL", "jdbc:hsqldb:mem:testdb");
    properties.put("JDBC.Username", "sa");
    properties.put("JDBC.Password", "pwd");
    properties.put("Pool.ValidationQuery", "SELECT 1");
    properties.put("Pool.MaximumActiveConnections", "12");
    properties.put("Pool.MaximumIdleConnections", "7");
    properties.put("Pool.MaximumWait", "2000");
    properties.put("Driver.applicationName", "ibatis-tests");

    DbcpConfiguration configuration = new DbcpConfiguration(properties);

    BasicDataSource dataSource = (BasicDataSource) configuration.getDataSource();
    Assertions.assertEquals("org.hsqldb.jdbcDriver", dataSource.getDriverClassName());
    Assertions.assertEquals("jdbc:hsqldb:mem:testdb", dataSource.getUrl());
    Assertions.assertEquals("sa", dataSource.getUsername());
    Assertions.assertEquals("pwd", dataSource.getPassword());
    Assertions.assertEquals("SELECT 1", dataSource.getValidationQuery());
    Assertions.assertEquals(12, dataSource.getMaxTotal());
    Assertions.assertEquals(7, dataSource.getMaxIdle());
    Assertions.assertEquals(Duration.ofMillis(2000), dataSource.getMaxWaitDuration());
  }

  @Test
  void shouldBuildPropertyDrivenConfigurationAndConvertTypes() {
    Map<String, String> properties = new HashMap<>();
    properties.put("driverClassName", "org.hsqldb.jdbcDriver");
    properties.put("url", "jdbc:hsqldb:mem:testdb2");
    properties.put("username", "user");
    properties.put("password", "secret");
    properties.put("maxTotal", "9");
    properties.put("maxIdle", "4");
    properties.put("defaultAutoCommit", "false");
    properties.put("Driver.loginTimeout", "13");

    DbcpConfiguration configuration = new DbcpConfiguration(properties);

    BasicDataSource dataSource = (BasicDataSource) configuration.getDataSource();
    Assertions.assertEquals("org.hsqldb.jdbcDriver", dataSource.getDriverClassName());
    Assertions.assertEquals("jdbc:hsqldb:mem:testdb2", dataSource.getUrl());
    Assertions.assertEquals("user", dataSource.getUsername());
    Assertions.assertEquals("secret", dataSource.getPassword());
    Assertions.assertEquals(9, dataSource.getMaxTotal());
    Assertions.assertEquals(4, dataSource.getMaxIdle());
    Assertions.assertFalse(dataSource.getDefaultAutoCommit());
  }

  @Test
  void shouldWrapInitializationErrors() {
    Map<String, String> properties = new HashMap<>();
    properties.put("maxTotal", "not-a-number");

    RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
        () -> new DbcpConfiguration(properties));
    Assertions.assertTrue(exception.getMessage().startsWith("Error initializing DbcpDataSourceFactory."));
  }
}
