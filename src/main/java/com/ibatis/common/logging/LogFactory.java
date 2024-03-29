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
package com.ibatis.common.logging;

import com.ibatis.common.resources.Resources;

import java.lang.reflect.Constructor;

/**
 * A factory for creating Log objects.
 */
public class LogFactory {

  /** The log constructor. */
  private static Constructor logConstructor;

  static {
    tryImplementation("org.apache.commons.logging.LogFactory",
        "com.ibatis.common.logging.jakarta.JakartaCommonsLoggingImpl");
    tryImplementation("org.apache.log4j.Logger", "com.ibatis.common.logging.log4j.Log4jImpl");
    tryImplementation("java.util.logging.Logger", "com.ibatis.common.logging.jdk14.Jdk14LoggingImpl");
    tryImplementation("java.lang.Object", "com.ibatis.common.logging.nologging.NoLoggingImpl");
  }

  /**
   * Try implementation.
   *
   * @param testClassName
   *          the test class name
   * @param implClassName
   *          the impl class name
   */
  private static void tryImplementation(String testClassName, String implClassName) {
    if (logConstructor == null) {
      try {
        Resources.classForName(testClassName);
        Class implClass = Resources.classForName(implClassName);
        logConstructor = implClass.getConstructor(new Class[] { Class.class });
      } catch (Throwable t) {
      }
    }
  }

  /**
   * Gets the log.
   *
   * @param aClass
   *          the a class
   *
   * @return the log
   */
  public static Log getLog(Class aClass) {
    try {
      return (Log) logConstructor.newInstance(new Object[] { aClass });
    } catch (Throwable t) {
      throw new RuntimeException("Error creating logger for class " + aClass + ".  Cause: " + t, t);
    }
  }

  /**
   * This method will switch the logging implementation to Log4J if Log4J is available on the classpath. This is useful
   * in situations where you want to use Log4J to log iBATIS activity but commons logging is on the classpath. Note that
   * this method is only effective for log classes obtained after calling this method. If you intend to use this method
   * you should call it before calling any other iBATIS method.
   */
  public static synchronized void selectLog4JLogging() {
    try {
      Resources.classForName("org.apache.log4j.Logger");
      Class implClass = Resources.classForName("com.ibatis.common.logging.log4j.Log4jImpl");
      logConstructor = implClass.getConstructor(new Class[] { Class.class });
    } catch (Throwable t) {
    }
  }

  /**
   * This method will switch the logging implementation to Java native logging if you are running in JRE 1.4 or above.
   * This is useful in situations where you want to use Java native logging to log iBATIS activity but commons logging
   * or Log4J is on the classpath. Note that this method is only effective for log classes obtained after calling this
   * method. If you intend to use this method you should call it before calling any other iBATIS method.
   */
  public static synchronized void selectJavaLogging() {
    try {
      Resources.classForName("java.util.logging.Logger");
      Class implClass = Resources.classForName("com.ibatis.common.logging.jdk14.Jdk14LoggingImpl");
      logConstructor = implClass.getConstructor(new Class[] { Class.class });
    } catch (Throwable t) {
    }
  }
}
