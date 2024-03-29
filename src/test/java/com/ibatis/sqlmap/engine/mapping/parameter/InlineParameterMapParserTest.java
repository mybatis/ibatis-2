/*
 * Copyright 2004-2023 the original author or authors.
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
package com.ibatis.sqlmap.engine.mapping.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.ibatis.sqlmap.engine.mapping.sql.SqlText;
import com.ibatis.sqlmap.engine.type.TypeHandlerFactory;

import org.junit.jupiter.api.Test;

class InlineParameterMapParserTest {

  @Test
  void testParseInlineParameterMapTypeHandlerFactoryString() {
    InlineParameterMapParser parser = new InlineParameterMapParser();
    SqlText parseInlineParameterMap = parser.parseInlineParameterMap(new TypeHandlerFactory(),
        "insert into foo (myColumn) values (1)");
    assertEquals("insert into foo (myColumn) values (1)", parseInlineParameterMap.getText());

    parseInlineParameterMap = parser.parseInlineParameterMap(new TypeHandlerFactory(),
        "insert into foo (myColumn) values (#myVar#)");
    assertEquals("insert into foo (myColumn) values (?)", parseInlineParameterMap.getText());

    parseInlineParameterMap = parser.parseInlineParameterMap(new TypeHandlerFactory(),
        "insert into foo (myColumn) values (#myVar:javaType=int#)");
    assertEquals("insert into foo (myColumn) values (?)", parseInlineParameterMap.getText());

    try {
      parseInlineParameterMap = parser.parseInlineParameterMap(new TypeHandlerFactory(),
          "insert into foo (myColumn) values (#myVar)");
      fail();
    } catch (Exception e) {
      assertEquals("Unterminated inline parameter in mapped statement near 'insert into foo (myColumn) values (?'",
          e.getMessage());
    }

    try {
      parseInlineParameterMap = parser.parseInlineParameterMap(new TypeHandlerFactory(),
          "insert into foo (myColumn) values (#myVar:javaType=int)");
      fail();
    } catch (Exception e) {
      assertEquals("Unterminated inline parameter in mapped statement near 'insert into foo (myColumn) values (?'",
          e.getMessage());
    }

    try {
      parseInlineParameterMap = parser.parseInlineParameterMap(new TypeHandlerFactory(),
          "insert into foo (myColumn) values (myVar#)");
      fail();
    } catch (Exception e) {
      assertEquals("Unterminated inline parameter in mapped statement near 'insert into foo (myColumn) values (myVar?'",
          e.getMessage());
    }

    try {
      parseInlineParameterMap = parser.parseInlineParameterMap(new TypeHandlerFactory(),
          "insert into foo (myColumn) values (#myVar##)");
      fail();
    } catch (Exception e) {
      assertEquals("Unterminated inline parameter in mapped statement near 'insert into foo (myColumn) values (??'",
          e.getMessage());
    }
  }

}
