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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Offline entity resolver for the iBATIS DTDs.
 */
public class SqlMapClasspathEntityResolver implements EntityResolver {

  /** The Constant SQL_MAP_CONFIG_DTD. */
  private static final String SQL_MAP_CONFIG_DTD = "com/ibatis/sqlmap/engine/builder/xml/sql-map-config-2.dtd";

  /** The Constant SQL_MAP_DTD. */
  private static final String SQL_MAP_DTD = "com/ibatis/sqlmap/engine/builder/xml/sql-map-2.dtd";

  /** The Constant doctypeMap. */
  private static final Map doctypeMap = new HashMap<>();

  static {
    doctypeMap.put("https://ibatis.apache.org/dtd/sql-map-config-2.dtd".toUpperCase(Locale.ROOT), SQL_MAP_CONFIG_DTD);
    doctypeMap.put("-//ibatis.apache.org//DTD SQL Map Config 2.0//EN".toUpperCase(Locale.ROOT), SQL_MAP_CONFIG_DTD);

    doctypeMap.put("https://ibatis.apache.org/dtd/sql-map-2.dtd".toUpperCase(Locale.ROOT), SQL_MAP_DTD);
    doctypeMap.put("-//ibatis.apache.org//DTD SQL Map 2.0//EN".toUpperCase(Locale.ROOT), SQL_MAP_DTD);
  }

  /**
   * Converts a public DTD into a local one
   *
   * @param publicId
   *          Unused but required by EntityResolver interface
   * @param systemId
   *          The DTD that is being requested
   *
   * @return The InputSource for the DTD
   *
   * @throws SAXException
   *           If anything goes wrong
   */
  public InputSource resolveEntity(String publicId, String systemId) throws SAXException {

    if (publicId != null)
      publicId = publicId.toUpperCase(Locale.ROOT);
    if (systemId != null)
      systemId = systemId.toUpperCase(Locale.ROOT);

    InputSource source = null;
    try {
      String path = (String) doctypeMap.get(publicId);
      source = getInputSource(path, source);
      if (source == null) {
        path = (String) doctypeMap.get(systemId);
        source = getInputSource(path, source);
      }
    } catch (Exception e) {
      throw new SAXException(e.toString());
    }
    return source;
  }

  /**
   * Gets the input source.
   *
   * @param path
   *          the path
   * @param source
   *          the source
   *
   * @return the input source
   */
  private InputSource getInputSource(String path, InputSource source) {
    if (path != null) {
      InputStream in = null;
      try {
        in = Resources.getResourceAsStream(path);
        source = new InputSource(in);
      } catch (IOException e) {
        // ignore, null is ok
      }
    }
    return source;
  }

}
