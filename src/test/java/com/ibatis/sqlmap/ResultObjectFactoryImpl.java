/*
 *  Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ibatis.sqlmap;

import testdomain.IItem;
import testdomain.IItemImpl;
import testdomain.ISupplier;
import testdomain.ISupplierImpl;
import testdomain.ISupplierKey;
import testdomain.ISupplierKeyImpl;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;
import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory;

/**
 * @author Jeff Butler
 *
 */
public class ResultObjectFactoryImpl implements ResultObjectFactory {

  /**
   * 
   */
  public ResultObjectFactoryImpl() {
    super();
  }

  /* (non-Javadoc)
   * @see com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory#createInstance(java.lang.String, java.lang.Class)
   */
  public Object createInstance(String statementId, Class clazz)
      throws InstantiationException, IllegalAccessException {
    
    Object obj = null;
    
    if (clazz.equals(IItem.class)) {
      obj = new IItemImpl();
    } else if (clazz.equals((ISupplier.class))) {
      obj = new ISupplierImpl();
    } else if (clazz.equals((ISupplierKey.class))) {
      obj = new ISupplierKeyImpl();
    }
    
    return obj;
  }

  /* (non-Javadoc)
   * @see com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory#setProperty(java.lang.String, java.lang.String)
   */
  public void setProperty(String name, String value) {
    Log log = LogFactory.getLog(this.getClass());
    
    log.debug("Property set.  name" + name + ", value: " + value);
  }
}
