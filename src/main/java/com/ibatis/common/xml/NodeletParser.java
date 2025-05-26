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
package com.ibatis.common.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The NodeletParser is a callback based parser similar to SAX. The big difference is that rather than having a single
 * callback for all nodes, the NodeletParser has a number of callbacks mapped to various nodes. The callback is called a
 * Nodelet and it is registered with the NodeletParser against a specific XPath.
 */
public class NodeletParser {

  /** The let map. */
  private Map letMap = new HashMap<>();

  /** The validation. */
  private boolean validation;

  /** The entity resolver. */
  private EntityResolver entityResolver;

  /**
   * Registers a nodelet for the specified XPath. Current XPaths supported are:
   * <ul>
   * <li>Text Path - /rootElement/childElement/text()
   * <li>Attribute Path - /rootElement/childElement/@theAttribute
   * <li>Element Path - /rootElement/childElement/theElement
   * <li>All Elements Named - //theElement
   * </ul>
   *
   * @param xpath
   *          the xpath
   * @param nodelet
   *          the nodelet
   */
  public void addNodelet(String xpath, Nodelet nodelet) {
    letMap.put(xpath, nodelet);
  }

  /**
   * Begins parsing from the provided Reader.
   *
   * @param reader
   *          the reader
   *
   * @throws NodeletException
   *           the nodelet exception
   */
  public void parse(Reader reader) throws NodeletException {
    try {
      Document doc = createDocument(reader);
      parse(doc.getLastChild());
    } catch (Exception e) {
      throw new NodeletException("Error parsing XML.  Cause: " + e, e);
    }
  }

  /**
   * Parses the.
   *
   * @param inputStream
   *          the input stream
   *
   * @throws NodeletException
   *           the nodelet exception
   */
  public void parse(InputStream inputStream) throws NodeletException {
    try {
      Document doc = createDocument(inputStream);
      parse(doc.getLastChild());
    } catch (Exception e) {
      throw new NodeletException("Error parsing XML.  Cause: " + e, e);
    }
  }

  /**
   * Begins parsing from the provided Node.
   *
   * @param node
   *          the node
   */
  public void parse(Node node) {
    Path path = new Path();
    processNodelet(node, "/");
    process(node, path);
  }

  /**
   * A recursive method that walkes the DOM tree, registers XPaths and calls Nodelets registered under those XPaths.
   *
   * @param node
   *          the node
   * @param path
   *          the path
   */
  private void process(Node node, Path path) {
    if (node instanceof Element) {
      // Element
      String elementName = node.getNodeName();
      path.add(elementName);
      processNodelet(node, path.toString());
      processNodelet(node, new StringBuilder("//").append(elementName).toString());

      // Attribute
      NamedNodeMap attributes = node.getAttributes();
      int n = attributes.getLength();
      for (int i = 0; i < n; i++) {
        Node att = attributes.item(i);
        String attrName = att.getNodeName();
        path.add("@" + attrName);
        processNodelet(att, path.toString());
        processNodelet(node, new StringBuilder("//@").append(attrName).toString());
        path.remove();
      }

      // Children
      NodeList children = node.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        process(children.item(i), path);
      }
      path.add("end()");
      processNodelet(node, path.toString());
      path.remove();
      path.remove();
    } else if (node instanceof Text) {
      // Text
      path.add("text()");
      processNodelet(node, path.toString());
      processNodelet(node, "//text()");
      path.remove();
    }
  }

  /**
   * Process nodelet.
   *
   * @param node
   *          the node
   * @param pathString
   *          the path string
   */
  private void processNodelet(Node node, String pathString) {
    Nodelet nodelet = (Nodelet) letMap.get(pathString);
    if (nodelet != null) {
      try {
        nodelet.process(node);
      } catch (Exception e) {
        throw new RuntimeException("Error parsing XPath '" + pathString + "'.  Cause: " + e, e);
      }
    }
  }

  /**
   * Creates a JAXP Document from a reader.
   *
   * @param reader
   *          the reader
   *
   * @return the document
   *
   * @throws ParserConfigurationException
   *           the parser configuration exception
   * @throws FactoryConfigurationError
   *           the factory configuration error
   * @throws SAXException
   *           the SAX exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private Document createDocument(Reader reader)
      throws ParserConfigurationException, FactoryConfigurationError, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
    factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    factory.setValidating(validation);

    factory.setNamespaceAware(false);
    factory.setIgnoringComments(true);
    factory.setIgnoringElementContentWhitespace(false);
    factory.setCoalescing(false);
    factory.setExpandEntityReferences(false);

    DocumentBuilder builder = factory.newDocumentBuilder();
    builder.setEntityResolver(entityResolver);
    builder.setErrorHandler(new ErrorHandler() {
      @Override
      public void error(SAXParseException exception) throws SAXException {
        throw exception;
      }

      @Override
      public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
      }

      @Override
      public void warning(SAXParseException exception) throws SAXException {
      }
    });

    return builder.parse(new InputSource(reader));
  }

  /**
   * Creates a JAXP Document from an InoutStream.
   *
   * @param inputStream
   *          the input stream
   *
   * @return the document
   *
   * @throws ParserConfigurationException
   *           the parser configuration exception
   * @throws FactoryConfigurationError
   *           the factory configuration error
   * @throws SAXException
   *           the SAX exception
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private Document createDocument(InputStream inputStream)
      throws ParserConfigurationException, FactoryConfigurationError, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
    factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    factory.setValidating(validation);

    factory.setNamespaceAware(false);
    factory.setIgnoringComments(true);
    factory.setIgnoringElementContentWhitespace(false);
    factory.setCoalescing(false);
    factory.setExpandEntityReferences(false);

    DocumentBuilder builder = factory.newDocumentBuilder();
    builder.setEntityResolver(entityResolver);
    builder.setErrorHandler(new ErrorHandler() {
      @Override
      public void error(SAXParseException exception) throws SAXException {
        throw exception;
      }

      @Override
      public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
      }

      @Override
      public void warning(SAXParseException exception) throws SAXException {
      }
    });

    return builder.parse(new InputSource(inputStream));
  }

  /**
   * Sets the validation.
   *
   * @param validation
   *          the new validation
   */
  public void setValidation(boolean validation) {
    this.validation = validation;
  }

  /**
   * Sets the entity resolver.
   *
   * @param resolver
   *          the new entity resolver
   */
  public void setEntityResolver(EntityResolver resolver) {
    this.entityResolver = resolver;
  }

  /**
   * Inner helper class that assists with building XPath paths.
   * <p>
   * Note: Currently this is a bit slow and could be optimized.
   */
  private static class Path {

    /** The node list. */
    private List nodeList = new ArrayList<>();

    /**
     * Instantiates a new path.
     */
    public Path() {
    }

    /**
     * Instantiates a new path.
     *
     * @param path
     *          the path
     */
    public Path(String path) {
      StringTokenizer parser = new StringTokenizer(path, "/", false);
      while (parser.hasMoreTokens()) {
        nodeList.add(parser.nextToken());
      }
    }

    /**
     * Adds the.
     *
     * @param node
     *          the node
     */
    public void add(String node) {
      nodeList.add(node);
    }

    /**
     * Removes the.
     */
    public void remove() {
      nodeList.remove(nodeList.size() - 1);
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("/");
      for (int i = 0; i < nodeList.size(); i++) {
        builder.append(nodeList.get(i));
        if (i < nodeList.size() - 1) {
          builder.append("/");
        }
      }
      return builder.toString();
    }
  }

}
