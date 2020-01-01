/**
 * Copyright 2004-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibatis.sqlmap.engine.mapping.statement;

/**
 * The listener interface for receiving execute events. The class that is interested in processing a execute event
 * implements this interface, and the object created with that class is registered with a component using the
 * component's <code>addExecuteListener</code> method. When the execute event occurs, that object's appropriate method
 * is invoked.
 */
public interface ExecuteListener {

  /**
   * On execute statement.
   *
   * @param statement
   *          the statement
   */
  public void onExecuteStatement(MappedStatement statement);

}
