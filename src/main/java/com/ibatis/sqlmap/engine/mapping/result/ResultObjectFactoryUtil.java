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
package com.ibatis.sqlmap.engine.mapping.result;

import com.ibatis.common.resources.Resources;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is used to create instances of result objects. It will use the configured ResultObjectFactory if there is
 * one, otherwise it will use iBATIS' normal methods.
 * <p>
 * Note that this class is somewhat tightly coupled with SqlExecuter - SqlExecute must call the setStatementId() and
 * setResultObjectFactory() methods before executing a statement. This is a result of using a ThreadLocal to hold the
 * current configuration for the statement under execution. Using a ThreadLocal is a solution for IBATIS-366. Without a
 * ThreadLocal, the current factory and statement id would have to be added to many method signatures - often in
 * inappropriate places.
 *
 * @author Jeff Butler
 */
public class ResultObjectFactoryUtil {

  /**
   * Use a ThreadLocal to hold the current statementId and factory. This is much easier than passing these items all
   * over the place, and it has no measurable impact on performance (I did a test with 100000 result rows and found no
   * impact - Jeff Butler).
   */
  private static ThreadLocal<Deque<FactorySettings>> factorySettings = new ThreadLocal<>();

  /**
   * Utility class - no instances.
   */
  private ResultObjectFactoryUtil() {
    super();
  }

  /**
   * Algorithm:
   * <ul>
   * <li>If factory is null, then create object internally()</li>
   * <li>Otherwise try to create object through factory</li>
   * <li>If null returned from factory, then create object internally</li>
   * </ul>
   * This allows the factory to selectively create objects, also allows for the common possibility that a factory is not
   * configured.
   *
   * @param clazz
   *          the type of object to create
   *
   * @return a new instance of the specified class. The instance must be castable to the specified class.
   *
   * @throws InstantiationException
   *           if the instance cannot be created. If you throw this Exception, iBATIS will throw a runtime exception in
   *           response and will end.
   * @throws IllegalAccessException
   *           if the constructor cannot be accessed. If you throw this Exception, iBATIS will throw a runtime exception
   *           in response and will end.
   */
  public static Object createObjectThroughFactory(Class clazz) throws InstantiationException, IllegalAccessException {

    FactorySettings fs = getCurrentFactorySettings();

    Object obj;
    if (fs.getResultObjectFactory() == null) {
      obj = createObjectInternally(clazz);
    } else {
      obj = fs.getResultObjectFactory().createInstance(fs.getStatementId(), clazz);
      if (obj == null) {
        obj = createObjectInternally(clazz);
      }
    }

    return obj;
  }

  /**
   * This method creates object using iBATIS' normal mechanism. We translate List and Collection to ArrayList, and Set
   * to HashSet because these interfaces may be requested in nested resultMaps and we want to supply default
   * implementations.
   *
   * @param clazz
   *          the clazz
   *
   * @return the object
   *
   * @throws InstantiationException
   *           the instantiation exception
   * @throws IllegalAccessException
   *           the illegal access exception
   */
  private static Object createObjectInternally(Class clazz) throws InstantiationException, IllegalAccessException {
    Class classToCreate;
    if (clazz == List.class || clazz == Collection.class) {
      classToCreate = ArrayList.class;
    } else if (clazz == Set.class) {
      classToCreate = HashSet.class;
    } else {
      classToCreate = clazz;
    }

    return Resources.instantiate(classToCreate);
  }

  /**
   * This method pushes a new result object factory configuration onto the stack. We use a stack because the method can
   * be called in a "nested" fashion if there are sub-selects. Calls to this method should be equally balanced with
   * calls to cleanupResultObjectFactory().
   *
   * @param resultObjectFactory
   *          the result object factory
   * @param statementId
   *          the statement id
   */
  public static void setupResultObjectFactory(ResultObjectFactory resultObjectFactory, String statementId) {
    Deque<FactorySettings> fss = factorySettings.get();
    if (fss == null) {
      fss = new ArrayDeque<>();
      factorySettings.set(fss);
    }

    FactorySettings fs = new FactorySettings();
    fs.setResultObjectFactory(resultObjectFactory);
    fs.setStatementId(statementId);
    fss.push(fs);
  }

  /**
   * Removes the FactorySettings bound to the current thread to avoid classloader leak issues. This method pops the top
   * item off the stack, and kills the stack if there are no items left.
   */
  public static void cleanupResultObjectFactory() {
    Deque<FactorySettings> fss = factorySettings.get();
    if (!fss.isEmpty()) {
      fss.pop();
    }

    if (fss.isEmpty()) {
      factorySettings.remove();
    }
  }

  /**
   * Gets the current factory settings.
   *
   * @return the current factory settings
   */
  private static FactorySettings getCurrentFactorySettings() {
    Deque<FactorySettings> fss = factorySettings.get();
    FactorySettings fs;
    if (fss == null || fss.isEmpty()) {
      // this shouldn't happen if the SqlExecuter is behaving correctly
      fs = new FactorySettings();
    } else {
      fs = fss.peek();
    }

    return fs;
  }

  /**
   * The Class FactorySettings.
   */
  private static class FactorySettings {

    /** The result object factory. */
    private ResultObjectFactory resultObjectFactory;

    /** The statement id. */
    private String statementId;

    /**
     * Gets the result object factory.
     *
     * @return the result object factory
     */
    public ResultObjectFactory getResultObjectFactory() {
      return resultObjectFactory;
    }

    /**
     * Sets the result object factory.
     *
     * @param resultObjectFactory
     *          the new result object factory
     */
    public void setResultObjectFactory(ResultObjectFactory resultObjectFactory) {
      this.resultObjectFactory = resultObjectFactory;
    }

    /**
     * Gets the statement id.
     *
     * @return the statement id
     */
    public String getStatementId() {
      return statementId;
    }

    /**
     * Sets the statement id.
     *
     * @param statementId
     *          the new statement id
     */
    public void setStatementId(String statementId) {
      this.statementId = statementId;
    }
  }
}
