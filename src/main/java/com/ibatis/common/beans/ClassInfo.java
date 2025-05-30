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
package com.ibatis.common.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a cached set of class definition information that allows for easy mapping between property
 * names and getter/setter methods.
 */
public class ClassInfo {

  /** The cache enabled. */
  private static boolean cacheEnabled = true;

  /** The Constant EMPTY_STRING_ARRAY. */
  private static final String[] EMPTY_STRING_ARRAY = new String[0];

  /** The Constant SIMPLE_TYPE_SET. */
  private static final Set SIMPLE_TYPE_SET = new HashSet<>();

  /** The Constant CLASS_INFO_MAP. */
  private static final Map<Class, ClassInfo> CLASS_INFO_MAP = new ConcurrentHashMap<>();

  /** The class name. */
  private String className;

  /** The readable property names. */
  private String[] readablePropertyNames = EMPTY_STRING_ARRAY;

  /** The writeable property names. */
  private String[] writeablePropertyNames = EMPTY_STRING_ARRAY;

  /** The set methods. */
  private HashMap setMethods = new HashMap<>();

  /** The get methods. */
  private HashMap getMethods = new HashMap<>();

  /** The set types. */
  private HashMap setTypes = new HashMap<>();

  /** The get types. */
  private HashMap getTypes = new HashMap<>();

  /** The default constructor. */
  private Constructor defaultConstructor;

  static {
    SIMPLE_TYPE_SET.add(String.class);
    SIMPLE_TYPE_SET.add(Byte.class);
    SIMPLE_TYPE_SET.add(Short.class);
    SIMPLE_TYPE_SET.add(Character.class);
    SIMPLE_TYPE_SET.add(Integer.class);
    SIMPLE_TYPE_SET.add(Long.class);
    SIMPLE_TYPE_SET.add(Float.class);
    SIMPLE_TYPE_SET.add(Double.class);
    SIMPLE_TYPE_SET.add(Boolean.class);
    SIMPLE_TYPE_SET.add(Date.class);
    SIMPLE_TYPE_SET.add(Class.class);
    SIMPLE_TYPE_SET.add(BigInteger.class);
    SIMPLE_TYPE_SET.add(BigDecimal.class);

    SIMPLE_TYPE_SET.add(Collection.class);
    SIMPLE_TYPE_SET.add(Set.class);
    SIMPLE_TYPE_SET.add(Map.class);
    SIMPLE_TYPE_SET.add(List.class);
    SIMPLE_TYPE_SET.add(HashMap.class);
    SIMPLE_TYPE_SET.add(TreeMap.class);
    SIMPLE_TYPE_SET.add(ArrayList.class);
    SIMPLE_TYPE_SET.add(LinkedList.class);
    SIMPLE_TYPE_SET.add(HashSet.class);
    SIMPLE_TYPE_SET.add(TreeSet.class);
    SIMPLE_TYPE_SET.add(Vector.class);
    SIMPLE_TYPE_SET.add(Hashtable.class);
    SIMPLE_TYPE_SET.add(Enumeration.class);
  }

  /**
   * Instantiates a new class info.
   *
   * @param clazz
   *          the clazz
   */
  private ClassInfo(Class clazz) {
    className = clazz.getName();
    addDefaultConstructor(clazz);
    addGetMethods(clazz);
    addSetMethods(clazz);
    addFields(clazz);
    readablePropertyNames = (String[]) getMethods.keySet().toArray(new String[getMethods.keySet().size()]);
    writeablePropertyNames = (String[]) setMethods.keySet().toArray(new String[setMethods.keySet().size()]);
  }

  /**
   * Adds the default constructor.
   *
   * @param clazz
   *          the clazz
   */
  private void addDefaultConstructor(Class clazz) {
    Constructor[] consts = clazz.getDeclaredConstructors();
    for (int i = 0; i < consts.length; i++) {
      Constructor constructor = consts[i];
      if (constructor.getParameterTypes().length == 0) {
        if (canAccessPrivateMethods()) {
          try {
            constructor.setAccessible(true);
          } catch (Exception e) {
            // Ignored. This is only a final precaution, nothing we can do.
          }
        }
        if (constructor.isAccessible()) {
          this.defaultConstructor = constructor;
        }
      }
    }
  }

  /**
   * Adds the get methods.
   *
   * @param cls
   *          the cls
   */
  private void addGetMethods(Class cls) {
    Method[] methods = getClassMethods(cls);
    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      String name = method.getName();
      if ((name.startsWith("get") && name.length() > 3) || (name.startsWith("is") && name.length() > 2)) {
        if (method.getParameterTypes().length == 0) {
          name = dropCase(name);
          addGetMethod(name, method);
        }
      }
    }
  }

  /**
   * Adds the get method.
   *
   * @param name
   *          the name
   * @param method
   *          the method
   */
  private void addGetMethod(String name, Method method) {
    getMethods.put(name, new MethodInvoker(method));
    getTypes.put(name, method.getReturnType());
  }

  /**
   * Adds the set methods.
   *
   * @param cls
   *          the cls
   */
  private void addSetMethods(Class cls) {
    Map conflictingSetters = new HashMap<>();
    Method[] methods = getClassMethods(cls);
    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      String name = method.getName();
      if (name.startsWith("set") && name.length() > 3) {
        if (method.getParameterTypes().length == 1) {
          name = dropCase(name);
          // /------------
          addSetterConflict(conflictingSetters, name, method);
          // addSetMethod(name, method);
          // /------------
        }
      }
    }
    resolveSetterConflicts(conflictingSetters);
  }

  /**
   * Adds the setter conflict.
   *
   * @param conflictingSetters
   *          the conflicting setters
   * @param name
   *          the name
   * @param method
   *          the method
   */
  private void addSetterConflict(Map conflictingSetters, String name, Method method) {
    List list = (List) conflictingSetters.get(name);
    if (list == null) {
      list = new ArrayList<>();
      conflictingSetters.put(name, list);
    }
    list.add(method);
  }

  /**
   * Resolve setter conflicts.
   *
   * @param conflictingSetters
   *          the conflicting setters
   */
  private void resolveSetterConflicts(Map conflictingSetters) {
    for (Iterator propNames = conflictingSetters.keySet().iterator(); propNames.hasNext();) {
      String propName = (String) propNames.next();
      List setters = (List) conflictingSetters.get(propName);
      Method firstMethod = (Method) setters.get(0);
      if (setters.size() == 1) {
        addSetMethod(propName, firstMethod);
      } else {
        Class expectedType = (Class) getTypes.get(propName);
        if (expectedType == null) {
          throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property " + propName
              + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans "
              + "specification and can cause unpredicatble results.");
        } else {
          Iterator methods = setters.iterator();
          Method setter = null;
          while (methods.hasNext()) {
            Method method = (Method) methods.next();
            if (method.getParameterTypes().length == 1 && expectedType.equals(method.getParameterTypes()[0])) {
              setter = method;
              break;
            }
          }
          if (setter == null) {
            throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property " + propName
                + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans "
                + "specification and can cause unpredicatble results.");
          }
          addSetMethod(propName, setter);
        }
      }
    }
  }

  /**
   * Adds the set method.
   *
   * @param name
   *          the name
   * @param method
   *          the method
   */
  private void addSetMethod(String name, Method method) {
    setMethods.put(name, new MethodInvoker(method));
    setTypes.put(name, method.getParameterTypes()[0]);
  }

  /**
   * Adds the fields.
   *
   * @param clazz
   *          the clazz
   */
  private void addFields(Class clazz) {
    Field[] fields = clazz.getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      if (canAccessPrivateMethods()) {
        try {
          field.setAccessible(true);
        } catch (Exception e) {
          // Ignored. This is only a final precaution, nothing we can do.
        }
      }
      if (field.isAccessible()) {
        if (!setMethods.containsKey(field.getName())) {
          addSetField(field);
        }
        if (!getMethods.containsKey(field.getName())) {
          addGetField(field);
        }
      }
    }
    if (clazz.getSuperclass() != null) {
      addFields(clazz.getSuperclass());
    }
  }

  /**
   * Adds the set field.
   *
   * @param field
   *          the field
   */
  private void addSetField(Field field) {
    setMethods.put(field.getName(), new SetFieldInvoker(field));
    setTypes.put(field.getName(), field.getType());
  }

  /**
   * Adds the get field.
   *
   * @param field
   *          the field
   */
  private void addGetField(Field field) {
    getMethods.put(field.getName(), new GetFieldInvoker(field));
    getTypes.put(field.getName(), field.getType());
  }

  /**
   * This method returns an array containing all methods declared in this class and any superclass. We use this method,
   * instead of the simpler Class.getMethods(), because we want to look for private methods as well.
   *
   * @param cls
   *          The class
   *
   * @return An array containing all methods in this class
   */
  private Method[] getClassMethods(Class cls) {
    HashMap uniqueMethods = new HashMap<>();
    Class currentClass = cls;
    while (currentClass != null) {
      addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());

      // we also need to look for interface methods -
      // because the class may be abstract
      Class[] interfaces = currentClass.getInterfaces();
      for (int i = 0; i < interfaces.length; i++) {
        addUniqueMethods(uniqueMethods, interfaces[i].getMethods());
      }

      currentClass = currentClass.getSuperclass();
    }

    Collection methods = uniqueMethods.values();

    return (Method[]) methods.toArray(new Method[methods.size()]);
  }

  /**
   * Adds the unique methods.
   *
   * @param uniqueMethods
   *          the unique methods
   * @param methods
   *          the methods
   */
  private void addUniqueMethods(HashMap uniqueMethods, Method[] methods) {
    for (Method currentMethod : methods) {
      if (!currentMethod.isBridge()) {
        String signature = getSignature(currentMethod);
        // check to see if the method is already known
        // if it is known, then an extended class must have
        // overridden a method
        if (!uniqueMethods.containsKey(signature)) {
          if (canAccessPrivateMethods()) {
            try {
              currentMethod.setAccessible(true);
            } catch (Exception e) {
              // Ignored. This is only a final precaution, nothing we can do.
            }
          }

          uniqueMethods.put(signature, currentMethod);
        }
      }
    }
  }

  /**
   * Gets the signature.
   *
   * @param method
   *          the method
   *
   * @return the signature
   */
  private String getSignature(Method method) {
    StringBuilder sb = new StringBuilder();
    sb.append(method.getName());
    Class[] parameters = method.getParameterTypes();

    for (int i = 0; i < parameters.length; i++) {
      if (i == 0) {
        sb.append(':');
      } else {
        sb.append(',');
      }
      sb.append(parameters[i].getName());
    }

    return sb.toString();
  }

  /**
   * Drop case.
   *
   * @param name
   *          the name
   *
   * @return the string
   */
  private static String dropCase(String name) {
    if (name.startsWith("is")) {
      name = name.substring(2);
    } else if (name.startsWith("get") || name.startsWith("set")) {
      name = name.substring(3);
    } else {
      throw new ProbeException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
    }

    if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
      name = name.substring(0, 1).toLowerCase(Locale.US) + name.substring(1);
    }

    return name;
  }

  /**
   * Can access private methods.
   *
   * @return true, if successful
   */
  private static boolean canAccessPrivateMethods() {
    try {
      SecurityManager securityManager = System.getSecurityManager();
      if (null != securityManager) {
        securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
      }
    } catch (SecurityException e) {
      return false;
    }
    return true;
  }

  /**
   * Gets the name of the class the instance provides information for.
   *
   * @return The class name
   */
  public String getClassName() {
    return className;
  }

  /**
   * Instantiate class.
   *
   * @return the object
   */
  public Object instantiateClass() {
    if (defaultConstructor != null) {
      try {
        return defaultConstructor.newInstance();
      } catch (Exception e) {
        throw new RuntimeException("Error instantiating class. Cause: " + e, e);
      }
    } else {
      throw new RuntimeException("Error instantiating class.  There is no default constructor for class " + className);
    }
  }

  /**
   * Gets the setter for a property as a Method object.
   *
   * @param propertyName
   *          - the property
   *
   * @return The Method
   */
  public Method getSetter(String propertyName) {
    Invoker method = (Invoker) setMethods.get(propertyName);
    if (method == null) {
      throw new ProbeException(
          "There is no WRITEABLE property named '" + propertyName + "' in class '" + className + "'");
    }
    if (!(method instanceof MethodInvoker)) {
      throw new ProbeException(
          "Can't get setter method because '" + propertyName + "' is a field in class '" + className + "'");
    }
    return ((MethodInvoker) method).getMethod();
  }

  /**
   * Gets the getter for a property as a Method object.
   *
   * @param propertyName
   *          - the property
   *
   * @return The Method
   */
  public Method getGetter(String propertyName) {
    Invoker method = (Invoker) getMethods.get(propertyName);
    if (method == null) {
      throw new ProbeException(
          "There is no READABLE property named '" + propertyName + "' in class '" + className + "'");
    }
    if (!(method instanceof MethodInvoker)) {
      throw new ProbeException(
          "Can't get getter method because '" + propertyName + "' is a field in class '" + className + "'");
    }
    return ((MethodInvoker) method).getMethod();
  }

  /**
   * Gets the sets the invoker.
   *
   * @param propertyName
   *          the property name
   *
   * @return the sets the invoker
   */
  public Invoker getSetInvoker(String propertyName) {
    Invoker method = (Invoker) setMethods.get(propertyName);
    if (method == null) {
      throw new ProbeException(
          "There is no WRITEABLE property named '" + propertyName + "' in class '" + className + "'");
    }
    return method;
  }

  /**
   * Gets the gets the invoker.
   *
   * @param propertyName
   *          the property name
   *
   * @return the gets the invoker
   */
  public Invoker getGetInvoker(String propertyName) {
    Invoker method = (Invoker) getMethods.get(propertyName);
    if (method == null) {
      throw new ProbeException(
          "There is no READABLE property named '" + propertyName + "' in class '" + className + "'");
    }
    return method;
  }

  /**
   * Gets the type for a property setter.
   *
   * @param propertyName
   *          - the name of the property
   *
   * @return The Class of the propery setter
   */
  public Class getSetterType(String propertyName) {
    Class clazz = (Class) setTypes.get(propertyName);
    if (clazz == null) {
      throw new ProbeException(
          "There is no WRITEABLE property named '" + propertyName + "' in class '" + className + "'");
    }
    return clazz;
  }

  /**
   * Gets the type for a property getter.
   *
   * @param propertyName
   *          - the name of the property
   *
   * @return The Class of the propery getter
   */
  public Class getGetterType(String propertyName) {
    Class clazz = (Class) getTypes.get(propertyName);
    if (clazz == null) {
      throw new ProbeException(
          "There is no READABLE property named '" + propertyName + "' in class '" + className + "'");
    }
    return clazz;
  }

  /**
   * Gets an array of the readable properties for an object.
   *
   * @return The array
   */
  public String[] getReadablePropertyNames() {
    return readablePropertyNames;
  }

  /**
   * Gets an array of the writeable properties for an object.
   *
   * @return The array
   */
  public String[] getWriteablePropertyNames() {
    return writeablePropertyNames;
  }

  /**
   * Check to see if a class has a writeable property by name.
   *
   * @param propertyName
   *          - the name of the property to check
   *
   * @return True if the object has a writeable property by the name
   */
  public boolean hasWritableProperty(String propertyName) {
    return setMethods.keySet().contains(propertyName);
  }

  /**
   * Check to see if a class has a readable property by name.
   *
   * @param propertyName
   *          - the name of the property to check
   *
   * @return True if the object has a readable property by the name
   */
  public boolean hasReadableProperty(String propertyName) {
    return getMethods.keySet().contains(propertyName);
  }

  /**
   * Tells us if the class passed in is a knwon common type.
   *
   * @param clazz
   *          The class to check
   *
   * @return True if the class is known
   */
  public static boolean isKnownType(Class clazz) {
    if (SIMPLE_TYPE_SET.contains(clazz)) {
      return true;
    } else if (Collection.class.isAssignableFrom(clazz)) {
      return true;
    } else if (Map.class.isAssignableFrom(clazz)) {
      return true;
    } else if (List.class.isAssignableFrom(clazz)) {
      return true;
    } else if (Set.class.isAssignableFrom(clazz)) {
      return true;
    } else if (Iterator.class.isAssignableFrom(clazz)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets an instance of ClassInfo for the specified class.
   *
   * @param clazz
   *          The class for which to lookup the method cache.
   *
   * @return The method cache for the class
   */
  public static ClassInfo getInstance(Class clazz) {
    if (cacheEnabled) {
      ClassInfo cached = (ClassInfo) CLASS_INFO_MAP.get(clazz);
      if (cached == null) {
        cached = new ClassInfo(clazz);
        CLASS_INFO_MAP.put(clazz, cached);
      }
      return cached;
    } else {
      return new ClassInfo(clazz);
    }
  }

  /**
   * Sets the cache enabled.
   *
   * @param cacheEnabled
   *          the new cache enabled
   */
  public static void setCacheEnabled(boolean cacheEnabled) {
    ClassInfo.cacheEnabled = cacheEnabled;
  }

  /**
   * Examines a Throwable object and gets it's root cause.
   *
   * @param t
   *          - the exception to examine
   *
   * @return The root cause
   */
  public static Throwable unwrapThrowable(Throwable t) {
    Throwable t2 = t;
    while (true) {
      if (t2 instanceof InvocationTargetException) {
        t2 = ((InvocationTargetException) t).getTargetException();
      } else if (t2 instanceof UndeclaredThrowableException) {
        t2 = ((UndeclaredThrowableException) t).getUndeclaredThrowable();
      } else {
        return t2;
      }
    }
  }

}
