package com.ibatis.sqlmap.engine.builder.xml;

import com.ibatis.common.xml.*;
import com.ibatis.sqlmap.engine.config.*;
import com.ibatis.sqlmap.engine.mapping.parameter.*;
import com.ibatis.sqlmap.engine.mapping.sql.*;
import com.ibatis.sqlmap.engine.mapping.sql.dynamic.*;
import com.ibatis.sqlmap.engine.mapping.sql.dynamic.elements.*;
import com.ibatis.sqlmap.engine.mapping.sql.raw.*;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;

import java.util.Properties;

public class XMLSqlSource implements SqlSource {

  private static final InlineParameterMapParser PARAM_PARSER = new InlineParameterMapParser();

  private XmlParserState state;
  private Node parentNode;

  public XMLSqlSource(XmlParserState config, Node parentNode) {
    this.state = config;
    this.parentNode = parentNode;
  }

  public Sql getSql() {
    state.getConfig().getErrorContext().setActivity("processing an SQL statement");

    boolean isDynamic = false;
    StringBuffer sqlBuffer = new StringBuffer();
    DynamicSql dynamic = new DynamicSql(state.getConfig().getClient().getDelegate());
    isDynamic = parseDynamicTags(parentNode, dynamic, sqlBuffer, isDynamic, false);
    String sqlStatement = sqlBuffer.toString();
    if (isDynamic) {
      return dynamic;
    } else {
      return new RawSql(sqlStatement);
    }
  }

  private boolean parseDynamicTags(Node node, DynamicParent dynamic, StringBuffer sqlBuffer, boolean isDynamic, boolean postParseRequired) {
    state.getConfig().getErrorContext().setActivity("parsing dynamic SQL tags");

    NodeList children = node.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      String nodeName = child.getNodeName();
      if (child.getNodeType() == Node.CDATA_SECTION_NODE
          || child.getNodeType() == Node.TEXT_NODE) {

        String data = ((CharacterData) child).getData();
        data = NodeletUtils.parsePropertyTokens(data, state.getGlobalProps());

        SqlText sqlText;

        if (postParseRequired) {
          sqlText = new SqlText();
          sqlText.setPostParseRequired(postParseRequired);
          sqlText.setText(data);
        } else {
          sqlText = PARAM_PARSER.parseInlineParameterMap(state.getConfig().getClient().getDelegate().getTypeHandlerFactory(), data, null);
          sqlText.setPostParseRequired(postParseRequired);
        }

        dynamic.addChild(sqlText);

        sqlBuffer.append(data);
      } else if ("include".equals(nodeName)) {
        Properties attributes = NodeletUtils.parseAttributes(child, state.getGlobalProps());
        String refid = (String) attributes.get("refid");
        Node includeNode = (Node) state.getSqlIncludes().get(refid);
        if (includeNode == null) {
          String nsrefid = state.applyNamespace(refid);
          includeNode = (Node) state.getSqlIncludes().get(nsrefid);
          if (includeNode == null) {
            throw new RuntimeException("Could not find SQL statement to include with refid '" + refid + "'");
          }
        }
        isDynamic = parseDynamicTags(includeNode, dynamic, sqlBuffer, isDynamic, false);
      } else {
        state.getConfig().getErrorContext().setMoreInfo("Check the dynamic tags.");

        SqlTagHandler handler = SqlTagHandlerFactory.getSqlTagHandler(nodeName);
        if (handler != null) {
          isDynamic = true;

          SqlTag tag = new SqlTag();
          tag.setName(nodeName);
          tag.setHandler(handler);

          Properties attributes = NodeletUtils.parseAttributes(child, state.getGlobalProps());

          tag.setPrependAttr(attributes.getProperty("prepend"));
          tag.setPropertyAttr(attributes.getProperty("property"));
          tag.setRemoveFirstPrepend(attributes.getProperty("removeFirstPrepend"));

          tag.setOpenAttr(attributes.getProperty("open"));
          tag.setCloseAttr(attributes.getProperty("close"));

          tag.setComparePropertyAttr(attributes.getProperty("compareProperty"));
          tag.setCompareValueAttr(attributes.getProperty("compareValue"));
          tag.setConjunctionAttr(attributes.getProperty("conjunction"));

          // an iterate ancestor requires a post parse

          if (dynamic instanceof SqlTag) {
            SqlTag parentSqlTag = (SqlTag) dynamic;
            if (parentSqlTag.isPostParseRequired() ||
                tag.getHandler() instanceof IterateTagHandler) {
              tag.setPostParseRequired(true);
            }
          } else if (dynamic instanceof DynamicSql) {
            if (tag.getHandler() instanceof IterateTagHandler) {
              tag.setPostParseRequired(true);
            }
          }

          dynamic.addChild(tag);

          if (child.hasChildNodes()) {
            isDynamic = parseDynamicTags(child, tag, sqlBuffer, isDynamic, tag.isPostParseRequired());
          }
        }
      }
    }
    state.getConfig().getErrorContext().setMoreInfo(null);
    return isDynamic;
  }

}
