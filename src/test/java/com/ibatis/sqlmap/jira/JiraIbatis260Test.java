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
package com.ibatis.sqlmap.jira;

import com.ibatis.sqlmap.BaseSqlMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.ArticleIndex;
import testdomain.ArticleIndexDenorm;
import testdomain.Topic;
import testdomain.TopicDescription;

/**
 * Regression test for JIRA IBATIS-260: "Hash conflict with groupBy resultMaps".
 * <p>
 * The JIRA was about a problem using groupBy in a hierarchy (look in the SqlMap of this test case for an example). The
 * problem was that when a value of a groupBy key for the second level groupBy existed in more than 1 first level
 * groupBy some objects would be assigned to the wrong 2nd level group.
 * <p>
 * The reason for this was that the values of the keys of the groupBys would only take into account the current groupBy
 * level. The fix is to keep track of all of the values for the keys used in previous groupBys in the hierarchy so that
 * the objects can be properly assigned.
 * <p>
 * Broken in v2.1.7, v2.1.6, and earlier. Fixed in versions > 2.1.7
 *
 * @author Sven Boden
 */
class JiraIbatis260Test extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/jira.sql");
  }

  /**
   * Regression test case for JIRA IBATIS-260. This is also a good example of the power of groupBy. We load the exact
   * same data but once via groupBy and once via a flat structure. Then we put the grouped result in a hashMap and we
   * delete all entries from the flat result from that hashMap. If we don't find a key in the hashMap there's a problem,
   * if at the end the hashMap is not empty there's also a problem.
   *
   * @throws Exception
   *           none should be thrown (if the regression test succeeds)
   */
  @Test
  void testIbatis260Error1() throws Exception {
    List<?> groupedResult = sqlMap.queryForList("getJira260GroupedResult", null);

    HashMap<String, Object> test = new HashMap<>();
    Iterator<?> indexIterator = groupedResult.iterator();
    while (indexIterator.hasNext()) {
      ArticleIndex articleIndex = (ArticleIndex) indexIterator.next();
      Iterator<?> topicIterator = articleIndex.getTopics().iterator();
      while (topicIterator.hasNext()) {
        Topic topic = (Topic) topicIterator.next();
        Iterator<?> descriptionIterator = topic.getDescriptionList().iterator();
        while (descriptionIterator.hasNext()) {
          TopicDescription desc = (TopicDescription) descriptionIterator.next();

          // Put a flattened key in the hashMap
          test.put(articleIndex.getCategoryTitle() + "||" + topic.getTopicTitle() + "||" + desc.getDescription(), null);
        }
      }
    }

    // Iterate over the flat version of the results and remove
    // all those keys from the hashMap. If an entry does not exists
    // the test case fails. If at the end the hashMap is not empty
    // the test case also fails.
    String key = null;
    List<?> flatResult = sqlMap.queryForList("getJira260FlatResult", null);
    Iterator<?> iterator = flatResult.iterator();
    while (iterator.hasNext()) {
      ArticleIndexDenorm articleIndex = (ArticleIndexDenorm) iterator.next();
      key = articleIndex.getCategoryTitle() + "||" + articleIndex.getTopicTitle() + "||"
          + articleIndex.getDescription();
      if (!test.containsKey(key)) {
        throw new Exception("Key \"" + key + "\" does not exist in the hashMap, regression test fails");
      }
      test.remove(key);
    }

    if (test.size() != 0) {
      throw new Exception("Map is not empty at the end of the test, regression test fails");
    }
  }
}
