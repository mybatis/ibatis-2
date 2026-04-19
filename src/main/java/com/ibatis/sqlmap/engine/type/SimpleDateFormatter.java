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
package com.ibatis.sqlmap.engine.type;

import com.ibatis.sqlmap.client.SqlMapException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class SimpleDateFormatter.
 */
public class SimpleDateFormatter {

  /**
   * Prevent instantiation.
   */
  private SimpleDateFormatter() {
    // Prevent instantiation
  }

  /**
   * Format.
   *
   * @param format
   *          the format
   * @param datetime
   *          the datetime
   *
   * @return the date
   */
  public static Date format(String format, String datetime) {
    try {
      return new SimpleDateFormat(format).parse(datetime);
    } catch (ParseException e) {
      throw new SqlMapException("Error parsing default null value date.  Format must be '" + format + "'. Cause: " + e);
    }
  }

}
