/*
 * Copyright 2004-2021 the original author or authors.
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
package com.ibatis.common.xml;

import org.w3c.dom.Node;

/**
 * A nodelet is a sort of callback or event handler that can be registered to handle an XPath event registered with the
 * NodeParser.
 */
public interface Nodelet {

  /**
   * For a registered XPath, the NodeletParser will call the Nodelet's process method for processing.
   *
   * @param node
   *          The node represents any XML node that can be registered under an XPath supported by the NodeletParser.
   *          Possible nodes are:
   *          <ul>
   *          <li>Text - Use node.getNodeValue() for the text value.
   *          <li>Attribute - Use node.getNodeValue() for the attribute value.
   *          <li>Element - This is the most flexible type. You can get the node content and iterate over the node's
   *          child nodes if neccessary. This is useful where a single XPath registration cannot describe the complex
   *          structure for a given XML stanza.
   *          </ul>
   * @throws Exception
   *           the exception
   */
  void process(Node node) throws Exception;

}
