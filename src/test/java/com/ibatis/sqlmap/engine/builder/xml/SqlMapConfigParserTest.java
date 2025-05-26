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
package com.ibatis.sqlmap.engine.builder.xml;

import com.ibatis.common.resources.Resources;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SqlMapConfigParserTest {
  @Test
  void parseStream() throws IOException {
    SqlMapConfigParser parser = new SqlMapConfigParser();
    var stream = Resources.getResourceAsStream("com/ibatis/sqlmap/maps/SqlMapConfig.xml");
    final var sqlMapClient = parser.parse(stream); // fails

    Assertions.assertNotNull(sqlMapClient);
  }

  @Test
  void parseStreamToReader() throws IOException {
    SqlMapConfigParser parser = new SqlMapConfigParser();
    var stream = Resources.getResourceAsStream("com/ibatis/sqlmap/maps/SqlMapConfig.xml");
    var reader = new InputStreamReader(stream);
    final var sqlMapClient = parser.parse(reader);

    Assertions.assertNotNull(sqlMapClient);
  }

  @Test
  void parseReader() throws IOException {
    SqlMapConfigParser parser = new SqlMapConfigParser();
    var reader = Resources.getResourceAsReader("com/ibatis/sqlmap/maps/SqlMapConfig.xml");
    final var sqlMapClient = parser.parse(reader);

    Assertions.assertNotNull(sqlMapClient);
  }
}
