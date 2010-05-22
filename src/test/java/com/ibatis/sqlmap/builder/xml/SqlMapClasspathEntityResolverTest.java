package com.ibatis.sqlmap.builder.xml;

import junit.framework.TestCase;
import com.ibatis.sqlmap.engine.builder.xml.SqlMapClasspathEntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SqlMapClasspathEntityResolverTest extends TestCase {

  // ibatis.com

  public void testComConfigSystemId() {
    String id = "http://www.ibatis.com/dtd/sql-map-config-2.dtd";
    assertSystemIdCanBeResolved(id);
  }

  public void testComConfigPublicId() {
    String id = "-//iBATIS.com//DTD SQL Map Config 2.0//EN";
    assertPublicIdCanBeResolved(id);
  }

  public void testComMapSystemId() {
    String id = "http://www.ibatis.com/dtd/sql-map-2.dtd";
    assertSystemIdCanBeResolved(id);
  }

  public void testComMapPublicId() {
    String id = "-//iBATIS.com//DTD SQL Map 2.0//EN";
    assertPublicIdCanBeResolved(id);
  }

  // ibatis.apache.org

  public void testOrgConfigSystemId() {
    String id = "http://ibatis.apache.org/dtd/sql-map-config-2.dtd";
    assertSystemIdCanBeResolved(id);
  }

  public void testOrgConfigPublicId() {
    String id = "-//ibatis.apache.org//DTD SQL Map Config 2.0//EN";
    assertPublicIdCanBeResolved(id);
  }

  public void testOrgMapSystemId() {
    String id = "http://ibatis.apache.org/dtd/sql-map-2.dtd";
    assertSystemIdCanBeResolved(id);
  }

  public void testOrgMapPublicId() {
    String id = "-//ibatis.apache.org//DTD SQL Map 2.0//EN";
    assertPublicIdCanBeResolved(id);
  }

  public void testOddCase() {
    String id = "-//iBATIS.apache.org//DTD SQL Map 2.0//EN";
    assertPublicIdCanBeResolved(id);
  }

  private void assertSystemIdCanBeResolved(String id) {
    SqlMapClasspathEntityResolver resolver = new SqlMapClasspathEntityResolver();
    try {
      InputSource source = resolver.resolveEntity(null, id);
      if (source == null) {
        throw new RuntimeException("Could not resolve System ID  " + id);
      }
    } catch (SAXException e) {
      throw new RuntimeException("Error.  Cause: " + e, e);
    }
  }

  private void assertPublicIdCanBeResolved(String id) {
    SqlMapClasspathEntityResolver resolver = new SqlMapClasspathEntityResolver();
    try {
      InputSource source = resolver.resolveEntity(id, null);
      if (source == null) {
        throw new RuntimeException("Could not resolve System ID  " + id);
      }
    } catch (SAXException e) {
      throw new RuntimeException("Error.  Cause: " + e, e);
    }
  }

}
