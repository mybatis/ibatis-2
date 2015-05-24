/**
 *    Copyright 2004-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.ibatis.sqlmap.engine.mapping.statement;

public final class StatementType {

  public static final StatementType UNKNOWN = new StatementType();
  public static final StatementType INSERT = new StatementType();
  public static final StatementType UPDATE = new StatementType();
  public static final StatementType DELETE = new StatementType();
  public static final StatementType SELECT = new StatementType();
  public static final StatementType PROCEDURE = new StatementType();

  private StatementType() {
  }

}
