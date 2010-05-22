/*
 *  Copyright 2004 Clinton Begin
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
package xmltester;

import com.ibatis.common.io.ReaderInputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class MiniParser extends DefaultHandler {

  private MiniDom dom;
  private MiniElement currentElement;

  public MiniParser(String string) {
    try {
      StringReader reader = new StringReader(string);
      parse(reader);
    } catch (Exception e) {
      throw new RuntimeException("XmlDataExchange error parsing XML.  Cause: " + e, e);
    }
  }

  public MiniParser(Reader reader) {
    try {
      parse(reader);
    } catch (Exception e) {
      throw new RuntimeException("XmlDataExchange error parsing XML.  Cause: " + e, e);
    }
  }

  public MiniDom getDom() {
    return dom;
  }

  public void startDocument() {
    dom = new MiniDom();
  }

  public void endDocument() {
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    MiniElement element = new MiniElement(qName);
    if (attributes != null) {
      for (int i = 0, n = attributes.getLength(); i < n; i++) {
        element.addAttribute(new MiniAttribute(attributes.getQName(i), attributes.getValue(i)));
      }
    }
    if (currentElement == null && dom.getRootElement() == null) {
      dom.setRootElement(element);
    } else {
      currentElement.addElement(element);
    }
    currentElement = element;
  }

  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (currentElement != null && currentElement != dom.getRootElement()) {
      currentElement = currentElement.getParent();
    }
  }

  public void characters(char ch[], int start, int length) {
    if (currentElement != null) {
      StringBuffer buffer;
      String current = currentElement.getBodyContent();
      if (current == null) {
        buffer = new StringBuffer();
      } else {
        buffer = new StringBuffer(current);
      }
      buffer.append(ch, start, length);
      currentElement.setBodyContent(buffer.toString());
    }
  }


  public void fatalError(SAXParseException e) throws SAXException {
    throw new RuntimeException("MiniXmlParser error parsing XML.  Cause: " + e, e);
  }

  private void parse(Reader reader) throws ParserConfigurationException, SAXException, IOException {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating(false);
    factory.setNamespaceAware(false);
    SAXParser parser = factory.newSAXParser();
    parser.parse(new ReaderInputStream(reader), this);
  }

}
