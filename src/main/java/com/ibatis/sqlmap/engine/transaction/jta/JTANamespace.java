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
package com.ibatis.sqlmap.engine.transaction.jta;

/**
 * The Class JTANamespace.
 * <p>
 * Utility class for detecting presence of Jakarta and Javax Transaction API classes.
 */
public final class JTANamespace {

  /** The Constant classloader. */
  private static final ClassLoader classloader = JTANamespace.class.getClassLoader();

  /** The Constant JAKARTA_TRANSACTION_CLASS. */
  public static final Class<?> JAKARTA_TRANSACTION_CLASS = JTANamespace
      .searchTypeInClasspath("jakarta.transaction.Transaction");

  /** The Constant JAVAX_TRANSACTION_CLASS. */
  public static final Class<?> JAVAX_TRANSACTION_CLASS = JTANamespace
      .searchTypeInClasspath("javax.transaction.Transaction");

  /**
   * Private constructor to prevent instantiation.
   */
  private JTANamespace() {
    // Utility class
  }

  /**
   * Attempts to load the class with the given name using this class's classloader. Returns null if the class is not
   * found.
   *
   * @param <T>
   *          the generic type
   * @param typeName
   *          the type name
   *
   * @return the class if found, or null if not found
   */
  @SuppressWarnings("unchecked")
  private static <T> Class<? extends T> searchTypeInClasspath(final String typeName) {
    try {
      return (Class<? extends T>) Class.forName(typeName, false, JTANamespace.classloader);
    } catch (final ClassNotFoundException e) {
      return null;
    }
  }

}
