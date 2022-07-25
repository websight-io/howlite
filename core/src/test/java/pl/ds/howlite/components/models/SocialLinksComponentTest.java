/*
 * Copyright (C) 2022 Dynamic Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.ds.howlite.components.models;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class SocialLinksComponentTest {

  private static final String PATH = "/content/video";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(SocialLinksComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("sociallinks.json")),
        PATH);
  }

  @Test
  void defaultSocialLinksComponentModelTest() {
    SocialLinksComponent model = requireNonNull(
        context.resourceResolver().getResource(PATH + "/default")).adaptTo(
        SocialLinksComponent.class);

    assertThat(model).isNotNull();
  }

  @Test
  void complexSocialLinksComponentModelTest() {
    SocialLinksComponent model = requireNonNull(
        context.resourceResolver().getResource(PATH + "/complex")).adaptTo(
        SocialLinksComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getSocialLinkItems()).isNotNull();
    assertThat(model.getSocialLinkItems()).isNotEmpty().hasSize(4);

    assertThat(model.getSocialLinkItems().stream()
        .filter(i -> i.getTitle().equals("Title linkedin") &&
            i.getUrl().equals("http://www.linkedin.com") &&
            i.getIcon().equals("linkedin")).count())
        .isEqualTo(1);
    assertThat(model.getSocialLinkItems().stream()
        .filter(i -> i.getTitle().equals("Title facebook") &&
            i.getUrl().equals("http://www.facebook.com") &&
            i.getIcon().equals("facebook")).count())
        .isEqualTo(1);
    assertThat(model.getSocialLinkItems().stream()
        .filter(i -> i.getTitle().equals("Title twitter") &&
            i.getUrl().equals("http://www.twitter.com") &&
            i.getIcon().equals("twitter")).count())
        .isEqualTo(1);
    assertThat(model.getSocialLinkItems().stream()
        .filter(i -> i.getTitle().equals("Title instagram") &&
            i.getUrl().equals("http://www.instagram.com") &&
            i.getIcon().equals("instagram")).count())
        .isEqualTo(1);
  }
}