package com.ibatis.sqlmap.engine.mapping.parameter;

import junit.framework.TestCase;

import com.ibatis.sqlmap.engine.mapping.sql.SqlText;
import com.ibatis.sqlmap.engine.type.TypeHandlerFactory;

public class InlineParameterMapParserTest extends TestCase {

    public void testParseInlineParameterMapTypeHandlerFactoryString() {
	InlineParameterMapParser parser = new InlineParameterMapParser();
	SqlText parseInlineParameterMap = parser.parseInlineParameterMap(
		new TypeHandlerFactory(),
		"insert into foo (myColumn) values (1)");
	assertEquals("insert into foo (myColumn) values (1)",
		parseInlineParameterMap.getText());

	parseInlineParameterMap = parser.parseInlineParameterMap(
		new TypeHandlerFactory(),
		"insert into foo (myColumn) values (#myVar#)");
	assertEquals("insert into foo (myColumn) values (?)",
		parseInlineParameterMap.getText());

	parseInlineParameterMap = parser.parseInlineParameterMap(
		new TypeHandlerFactory(),
		"insert into foo (myColumn) values (#myVar:javaType=int#)");
	assertEquals("insert into foo (myColumn) values (?)",
		parseInlineParameterMap.getText());
	
	try {
	    parseInlineParameterMap = parser.parseInlineParameterMap(
		    new TypeHandlerFactory(),
		    "insert into foo (myColumn) values (#myVar)");
	    fail();
	} catch (Exception e) {
	    assertEquals(
		    "Unterminated inline parameter in mapped statement near 'insert into foo (myColumn) values (?'",
		    e.getMessage());
	}
	
	try {
	    parseInlineParameterMap = parser.parseInlineParameterMap(
		    new TypeHandlerFactory(),
		    "insert into foo (myColumn) values (#myVar:javaType=int)");
	    fail();
	} catch (Exception e) {
	    assertEquals(
		    "Unterminated inline parameter in mapped statement near 'insert into foo (myColumn) values (?'",
		    e.getMessage());
	}

	try {
	    parseInlineParameterMap = parser.parseInlineParameterMap(
		    new TypeHandlerFactory(),
		    "insert into foo (myColumn) values (myVar#)");
	    fail();
	} catch (Exception e) {
	    assertEquals(
		    "Unterminated inline parameter in mapped statement near 'insert into foo (myColumn) values (myVar?'",
		    e.getMessage());
	}

	try {
	    parseInlineParameterMap = parser.parseInlineParameterMap(
		    new TypeHandlerFactory(),
		    "insert into foo (myColumn) values (#myVar##)");
	    fail();
	} catch (Exception e) {
	    assertEquals(
		    "Unterminated inline parameter in mapped statement near 'insert into foo (myColumn) values (??'",
		    e.getMessage());
	}
    }

}
