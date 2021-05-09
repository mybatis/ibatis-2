/*
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
package com.ibatis.sqlmap.engine.transaction;

/**
 * The Class TransactionState.
 */
public class TransactionState {

  /** The Constant STATE_STARTED. */
  public static final TransactionState STATE_STARTED = new TransactionState();

  /** The Constant STATE_COMMITTED. */
  public static final TransactionState STATE_COMMITTED = new TransactionState();

  /** The Constant STATE_ENDED. */
  public static final TransactionState STATE_ENDED = new TransactionState();

  /** The Constant STATE_USER_PROVIDED. */
  public static final TransactionState STATE_USER_PROVIDED = new TransactionState();

  /**
   * Instantiates a new transaction state.
   */
  private TransactionState() {
  }

}
