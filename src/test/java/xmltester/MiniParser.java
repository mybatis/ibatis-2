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
package xmltester;

import com.ibatis.common.io.ReaderInputStream;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class MiniParser extends DefaultHandler {

  private MiniDom dom;
  private MiniElement currentElement;

  public MiniParser(final String string) {
    try {
      final StringReader reader = new StringReader(string);
      this.parse(reader);
    } catch (final Exception e) {
      throw new RuntimeException("XmlDataExchange error parsing XML.  Cause: " + e, e);
    }
  }

  public MiniParser(final Reader reader) {
    try {
      this.parse(reader);
    } catch (final Exception e) {
      throw new RuntimeException("XmlDataExchange error parsing XML.  Cause: " + e, e);
    }
  }

  public MiniDom getDom() {
    return this.dom;
  }

  @Override
  public void startDocument() {
    this.dom = new MiniDom();
  }

  @Override
  public void endDocument() {
  }

  @Override
  public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
      throws SAXException {
    final MiniElement element = new MiniElement(qName);
    if (attributes != null) {
      for (int i = 0, n = attributes.getLength(); i < n; i++) {
        element.addAttribute(new MiniAttribute(attributes.getQName(i), attributes.getValue(i)));
      }
    }
    if (this.currentElement == null && this.dom.getRootElement() == null) {
      this.dom.setRootElement(element);
    } else {
      this.currentElement.addElement(element);
    }
    this.currentElement = element;
  }

  @Override
  public void endElement(final String uri, final String localName, final String qName) throws SAXException {
    if (this.currentElement != null && this.currentElement != this.dom.getRootElement()) {
      this.currentElement = this.currentElement.getParent();
    }
  }

  @Override
  public void characters(final char ch[], final int start, final int length) {
    if (this.currentElement != null) {
      StringBuilder builder;
      final String current = this.currentElement.getBodyContent();
      if (current == null) {
        builder = new StringBuilder();
      } else {
        builder = new StringBuilder(current);
      }
      builder.append(ch, start, length);
      this.currentElement.setBodyContent(builder.toString());
    }
  }

  @Override
  public void fatalError(final SAXParseException e) throws SAXException {
    throw new RuntimeException("MiniXmlParser error parsing XML.  Cause: " + e, e);
  }

  private void parse(final Reader reader) throws ParserConfigurationException, SAXException, IOException {
    final SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating(false);
    factory.setNamespaceAware(false);
    final SAXParser parser = factory.newSAXParser();
    parser.parse(new ReaderInputStream(reader), this);
  }

}
