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

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SimpleDataSource} configuration, getters, connection lifecycle and pool behaviour using the
 * in-memory HSQLDB driver that is already on the test classpath.
 */
class SimpleDataSourceTest {

  private static final String DRIVER = "org.hsqldb.jdbcDriver";
  private static final String URL = "jdbc:hsqldb:mem:simpledstestdb_" + System.currentTimeMillis();
  private static final String USER = "sa";
  private static final String PASS = "";

  private static Map<String, String> baseProps() {
    Map<String, String> props = new HashMap<>();
    props.put("JDBC.Driver", DRIVER);
    props.put("JDBC.ConnectionURL", URL);
    props.put("JDBC.Username", USER);
    props.put("JDBC.Password", PASS);
    return props;
  }

  // -------------------------------------------------------------------------
  // Construction validation
  // -------------------------------------------------------------------------

  @Test
  void shouldThrowWhenPropsAreNull() {
    Assertions.assertThrows(RuntimeException.class, () -> new SimpleDataSource(null));
  }

  @Test
  void shouldThrowWhenRequiredPropsAreMissing() {
    Map<String, String> props = new HashMap<>();
    props.put("JDBC.Driver", DRIVER);
    // URL, Username and Password are missing
    Assertions.assertThrows(RuntimeException.class, () -> new SimpleDataSource(props));
  }

  @Test
  void shouldThrowWhenPingEnabledWithoutQuery() {
    Map<String, String> props = baseProps();
    props.put("Pool.PingEnabled", "true");
    // PingQuery deliberately omitted
    Assertions.assertThrows(RuntimeException.class, () -> new SimpleDataSource(props));
  }

  @Test
  void shouldThrowWhenDriverClassNotFound() {
    Map<String, String> props = baseProps();
    props.put("JDBC.Driver", "no.such.Driver");
    Assertions.assertThrows(RuntimeException.class, () -> new SimpleDataSource(props));
  }

  // -------------------------------------------------------------------------
  // Property getters after correct initialisation
  // -------------------------------------------------------------------------

  @Test
  void shouldReadBackAllConfiguredProperties() {
    Map<String, String> props = baseProps();
    props.put("Pool.MaximumActiveConnections", "3");
    props.put("Pool.MaximumIdleConnections", "2");
    props.put("Pool.MaximumCheckoutTime", "1000");
    props.put("Pool.TimeToWait", "500");
    props.put("Pool.PingEnabled", "false");
    props.put("Pool.PingQuery", "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS");
    props.put("Pool.PingConnectionsOlderThan", "60000");
    props.put("Pool.PingConnectionsNotUsedFor", "30000");
    props.put("JDBC.DefaultAutoCommit", "false");

    SimpleDataSource ds = new SimpleDataSource(props);

    Assertions.assertEquals(DRIVER, ds.getJdbcDriver());
    Assertions.assertEquals(URL, ds.getJdbcUrl());
    Assertions.assertEquals(USER, ds.getJdbcUsername());
    Assertions.assertEquals(PASS, ds.getJdbcPassword());
    Assertions.assertEquals(3, ds.getPoolMaximumActiveConnections());
    Assertions.assertEquals(2, ds.getPoolMaximumIdleConnections());
    Assertions.assertEquals(1000, ds.getPoolMaximumCheckoutTime());
    Assertions.assertEquals(500, ds.getPoolTimeToWait());
    Assertions.assertFalse(ds.isPoolPingEnabled());
    Assertions.assertEquals("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS", ds.getPoolPingQuery());
    Assertions.assertEquals(60000, ds.getPoolPingConnectionsOlderThan());
    Assertions.assertEquals(30000, ds.getPoolPingConnectionsNotUsedFor());
  }

  @Test
  void shouldUseDefaultValuesWhenOptionalPropsAbsent() {
    SimpleDataSource ds = new SimpleDataSource(baseProps());

    Assertions.assertEquals(10, ds.getPoolMaximumActiveConnections());
    Assertions.assertEquals(5, ds.getPoolMaximumIdleConnections());
    Assertions.assertEquals(20000, ds.getPoolMaximumCheckoutTime());
    Assertions.assertEquals(20000, ds.getPoolTimeToWait());
    Assertions.assertFalse(ds.isPoolPingEnabled());
    Assertions.assertEquals(0, ds.getPoolPingConnectionsOlderThan());
    Assertions.assertEquals(0, ds.getPoolPingConnectionsNotUsedFor());
    Assertions.assertEquals("NO PING QUERY SET", ds.getPoolPingQuery());
  }

  @Test
  void shouldInitialiseDriverPropertiesWhenDriverPrefixedPropsPresent() {
    Map<String, String> props = baseProps();
    props.put("Driver.serverTimezone", "UTC");
    // Should not throw
    SimpleDataSource ds = new SimpleDataSource(props);
    Assertions.assertNotNull(ds);
  }

  // -------------------------------------------------------------------------
  // Status string
  // -------------------------------------------------------------------------

  @Test
  void shouldReturnNonEmptyStatusString() {
    SimpleDataSource ds = new SimpleDataSource(baseProps());
    String status = ds.getStatus();
    Assertions.assertTrue(status.contains("jdbcDriver"));
    Assertions.assertTrue(status.contains("activeConnections"));
    Assertions.assertTrue(status.contains("idleConnections"));
  }

  // -------------------------------------------------------------------------
  // Statistic counters before any request
  // -------------------------------------------------------------------------

  @Test
  void shouldReturnZeroStatisticsBeforeAnyRequest() {
    SimpleDataSource ds = new SimpleDataSource(baseProps());

    Assertions.assertEquals(0, ds.getRequestCount());
    Assertions.assertEquals(0, ds.getAverageRequestTime());
    Assertions.assertEquals(0, ds.getAverageCheckoutTime());
    Assertions.assertEquals(0, ds.getAverageWaitTime());
    Assertions.assertEquals(0, ds.getHadToWaitCount());
    Assertions.assertEquals(0, ds.getBadConnectionCount());
    Assertions.assertEquals(0, ds.getClaimedOverdueConnectionCount());
    Assertions.assertEquals(0, ds.getAverageOverdueCheckoutTime());
  }

  // -------------------------------------------------------------------------
  // getConnection and return cycle
  // -------------------------------------------------------------------------

  @Test
  void shouldObtainAndReturnConnection() throws Exception {
    SimpleDataSource ds = new SimpleDataSource(baseProps());

    Connection conn = ds.getConnection();
    Assertions.assertNotNull(conn);
    Assertions.assertEquals(1, ds.getRequestCount());

    conn.close(); // returns connection to pool
    Assertions.assertEquals(1, ds.getRequestCount());
  }

  @Test
  void shouldReuseIdleConnection() throws Exception {
    SimpleDataSource ds = new SimpleDataSource(baseProps());

    Connection conn1 = ds.getConnection();
    conn1.close();

    Connection conn2 = ds.getConnection();
    Assertions.assertNotNull(conn2);
    conn2.close();

    Assertions.assertEquals(2, ds.getRequestCount());
  }

  @Test
  void shouldGetConnectionWithExplicitCredentials() throws Exception {
    SimpleDataSource ds = new SimpleDataSource(baseProps());

    Connection conn = ds.getConnection(USER, PASS);
    Assertions.assertNotNull(conn);
    conn.close();
  }

  // -------------------------------------------------------------------------
  // setLoginTimeout / getLoginTimeout / logWriter
  // -------------------------------------------------------------------------

  @Test
  void shouldDelegateLoginTimeoutToDriverManager() throws Exception {
    SimpleDataSource ds = new SimpleDataSource(baseProps());
    ds.setLoginTimeout(5);
    Assertions.assertEquals(5, ds.getLoginTimeout());
  }

  @Test
  void shouldDelegateLogWriterToDriverManager() throws Exception {
    SimpleDataSource ds = new SimpleDataSource(baseProps());
    java.io.PrintWriter writer = new java.io.PrintWriter(System.out);
    ds.setLogWriter(writer);
    Assertions.assertNotNull(ds.getLogWriter());
  }

  // -------------------------------------------------------------------------
  // forceCloseAll
  // -------------------------------------------------------------------------

  @Test
  void shouldForceCloseAllConnectionsWithoutError() throws Exception {
    SimpleDataSource ds = new SimpleDataSource(baseProps());
    Connection conn = ds.getConnection();
    conn.close();
    // Should not throw
    ds.forceCloseAll();
  }

  // -------------------------------------------------------------------------
  // unwrapConnection
  // -------------------------------------------------------------------------

  @Test
  void shouldUnwrapPooledConnection() throws Exception {
    SimpleDataSource ds = new SimpleDataSource(baseProps());
    Connection proxy = ds.getConnection();
    // The proxy is a JDK Proxy (implements Connection) wrapping a SimplePooledConnection handler.
    // unwrapConnection checks instanceof SimplePooledConnection which is false for the JDK proxy,
    // so it returns the same object unchanged.
    Connection result = SimpleDataSource.unwrapConnection(proxy);
    Assertions.assertSame(proxy, result);

    // Verify the actual unwrap path: if we pass a real connection it comes back unchanged.
    SimpleDataSource.SimplePooledConnection spc = (SimpleDataSource.SimplePooledConnection) java.lang.reflect.Proxy
        .getInvocationHandler(proxy);
    Connection realConn = spc.getRealConnection();
    Connection realResult = SimpleDataSource.unwrapConnection(realConn);
    Assertions.assertSame(realConn, realResult);

    proxy.close();
  }

  @Test
  void shouldReturnSameConnectionWhenAlreadyReal() throws Exception {
    SimpleDataSource ds = new SimpleDataSource(baseProps());
    Connection proxy = ds.getConnection();
    SimpleDataSource.SimplePooledConnection spc = (SimpleDataSource.SimplePooledConnection) java.lang.reflect.Proxy
        .getInvocationHandler(proxy);
    Connection real = spc.getRealConnection();
    Connection real2 = SimpleDataSource.unwrapConnection(real);
    Assertions.assertSame(real, real2);
    proxy.close();
  }

  // -------------------------------------------------------------------------
  // SimplePooledConnection accessors
  // -------------------------------------------------------------------------

  @Test
  void shouldRecordTimestampsOnPooledConnection() throws Exception {
    SimpleDataSource ds = new SimpleDataSource(baseProps());
    Connection proxy = ds.getConnection();

    // Reach the internal SimplePooledConnection via unwrapConnection
    // (proxy implements Connection but its handler is SimplePooledConnection)
    java.lang.reflect.InvocationHandler handler = java.lang.reflect.Proxy.getInvocationHandler(proxy);
    Assertions.assertInstanceOf(SimpleDataSource.SimplePooledConnection.class, handler);

    SimpleDataSource.SimplePooledConnection spc = (SimpleDataSource.SimplePooledConnection) handler;

    Assertions.assertTrue(spc.getCreatedTimestamp() > 0);
    Assertions.assertTrue(spc.getCheckoutTimestamp() > 0);
    Assertions.assertTrue(spc.getLastUsedTimestamp() > 0);
    Assertions.assertTrue(spc.getAge() >= 0);
    Assertions.assertTrue(spc.getTimeElapsedSinceLastUse() >= 0);
    Assertions.assertTrue(spc.getCheckoutTime() >= 0);
    Assertions.assertNotNull(spc.getRealConnection());
    Assertions.assertNotNull(spc.getProxyConnection());
    Assertions.assertTrue(spc.getRealHashCode() != 0);

    // setter round-trips
    spc.setCreatedTimestamp(1000L);
    Assertions.assertEquals(1000L, spc.getCreatedTimestamp());
    spc.setLastUsedTimestamp(2000L);
    Assertions.assertEquals(2000L, spc.getLastUsedTimestamp());
    spc.setCheckoutTimestamp(3000L);
    Assertions.assertEquals(3000L, spc.getCheckoutTimestamp());
    spc.setConnectionTypeCode(42);
    Assertions.assertEquals(42, spc.getConnectionTypeCode());

    // hashCode / equals contracts
    Assertions.assertEquals(spc.hashCode(), spc.hashCode());
    Assertions.assertTrue(spc.equals(spc.getRealConnection()));

    proxy.close();
  }

  @Test
  void shouldThrowWhenAccessingInvalidPooledConnection() throws Exception {
    SimpleDataSource ds = new SimpleDataSource(baseProps());
    Connection proxy = ds.getConnection();
    SimpleDataSource.SimplePooledConnection spc = (SimpleDataSource.SimplePooledConnection) java.lang.reflect.Proxy
        .getInvocationHandler(proxy);
    spc.invalidate();

    Assertions.assertThrows(Exception.class, () -> proxy.createStatement());
  }

  // -------------------------------------------------------------------------
  // Ping path - ping enabled with fast-query
  // -------------------------------------------------------------------------

  @Test
  void shouldConfigurePingEnabled() {
    Map<String, String> props = baseProps();
    props.put("Pool.PingEnabled", "true");
    props.put("Pool.PingQuery", "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS");
    props.put("Pool.PingConnectionsNotUsedFor", "1");
    props.put("Pool.MaximumIdleConnections", "1");

    SimpleDataSource ds = new SimpleDataSource(props);
    Assertions.assertTrue(ds.isPoolPingEnabled());
    Assertions.assertEquals(1, ds.getPoolPingConnectionsNotUsedFor());
  }
}
