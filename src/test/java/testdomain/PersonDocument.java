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
package testdomain;

/**
 * @author Jeff Butler
 */
public class PersonDocument {

  private Integer id;
  private String name;
  private Document favoriteDocument;

  /**
   *
   */
  public PersonDocument() {
  }

  public Document getFavoriteDocument() {
    return this.favoriteDocument;
  }

  public void setFavoriteDocument(final Document favoriteDocument) {
    this.favoriteDocument = favoriteDocument;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

}
