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

public class MiniCodec {

  public static String encodeReservedXMLChars(final String s) {
    String s2 = s;
    if (s2 != null) {
      // Do ampersand FIRST to avoid replacing other tokens.
      s2 = MiniCodec.replaceAll("&", "&amp;", s2);

      s2 = MiniCodec.replaceAll(">", "&gt;", s2);
      s2 = MiniCodec.replaceAll("<", "&lt;", s2);
      s2 = MiniCodec.replaceAll("'", "&apos;", s2);
      s2 = MiniCodec.replaceAll("\"", "&quot;", s2);
      s2 = MiniCodec.replaceAll("%", "&#37;", s2);
    }
    return s2;
  }

  public static String decodeReservedXMLChars(final String s) {
    String s2 = s;
    if (s2 != null) {
      s2 = MiniCodec.replaceAll("&amp;", "&", s2);
      s2 = MiniCodec.replaceAll("&gt;", ">", s2);
      s2 = MiniCodec.replaceAll("&lt;", "<", s2);
      s2 = MiniCodec.replaceAll("&apos;", "'", s2);
      s2 = MiniCodec.replaceAll("&quot;", "\"", s2);
      s2 = MiniCodec.replaceAll("&#37;", "%", s2);
    }
    return s2;
  }

  /**
   * This nasty method uses exception handling for flow control to reduce the number of conditional checks. Nasty
   * performance enhancement (hopefully).
   * <p>
   * For all of it's nastiness, this method replaces all occurrences of a pattern with a new pattern, and quite quickly.
   *
   * @param pattern
   *          The pattern to match.
   * @param newPattern
   *          The pattern to replace the matched pattern.
   * @param string
   *          The string in which to search and replace.
   *
   * @return The new string with patterns replaced.
   */
  private static String replaceAll(final String pattern, final String newPattern, final String string) {
    try {
      final StringBuilder stringBuilder = new StringBuilder(string);
      int index = string.length();
      final int offset = pattern.length();
      while ((index = string.lastIndexOf(pattern, index - 1)) > -1) {
        stringBuilder.replace(index, index + offset, newPattern);
      }
      return stringBuilder.toString();
    } catch (final StringIndexOutOfBoundsException e) {
      return string;
    }
  }

}
