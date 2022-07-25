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
class FooterComponentTest {

  private static final String PATH = "/content/footer";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(FooterComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("footer.json")), PATH);
  }

  @Test
  void defaultHeaderComponentModelTest() {
    FooterComponent model = requireNonNull(
        context.resourceResolver().getResource(PATH + "/default")).adaptTo(FooterComponent.class);

    assertThat(model).isNotNull();
  }

  @Test
  void complexHeaderComponentModelTest() {
    FooterComponent model = requireNonNull(
        context.resourceResolver().getResource(PATH + "/complex")).adaptTo(FooterComponent.class);

    assertThat(model).isNotNull();

    assertThat(model.getImage()).isNotNull();
    assertThat(model.getImage().getAlt()).isNotNull();
    assertThat(model.getImage().getAlt()).isEqualTo("alt test text");

    assertThat(model.getNavigation()).isNotNull();
    assertThat(model.getNavigation().getMenuItems()).isNotNull();
    assertThat(model.getNavigation().getMenuItems()).isNotEmpty().hasSize(2);
    assertThat(model.getNavigation().getMenuItems().stream()
        .filter(i -> i.getLabel().equals("Navigation item 1") &&
            i.getLink().equals("http://www.navigate-to-link-1.com") &&
            i.getOpenInNewTab().equals("true"))
        .count()).isEqualTo(1);

    assertThat(model.getNavigation().getMenuItems().stream()
        .filter(i -> i.getLabel().equals("Navigation item 2") &&
            i.getLink().equals("http://www.navigate-to-link-2.com") &&
            i.getOpenInNewTab().equals("false"))
        .count()).isEqualTo(1);

    assertThat(model.getSocialLinks()).isNotNull();
    assertThat(model.getSocialLinks().getSocialLinkItems()).isNotNull();
    assertThat(model.getSocialLinks().getSocialLinkItems()).isNotEmpty().hasSize(4);
    assertThat(model.getSocialLinks().getSocialLinkItems().stream()
        .filter(i -> i.getTitle().equals("Title linkedin") &&
            i.getUrl().equals("http://www.linkedin.com") &&
            i.getIcon().equals("linkedin")).count())
        .isEqualTo(1);
    assertThat(model.getSocialLinks().getSocialLinkItems().stream()
        .filter(i -> i.getTitle().equals("Title facebook") &&
            i.getUrl().equals("http://www.facebook.com") &&
            i.getIcon().equals("facebook")).count())
        .isEqualTo(1);
    assertThat(model.getSocialLinks().getSocialLinkItems().stream()
        .filter(i -> i.getTitle().equals("Title twitter") &&
            i.getUrl().equals("http://www.twitter.com") &&
            i.getIcon().equals("twitter")).count())
        .isEqualTo(1);
    assertThat(model.getSocialLinks().getSocialLinkItems().stream()
        .filter(i -> i.getTitle().equals("Title instagram") &&
            i.getUrl().equals("http://www.instagram.com") &&
            i.getIcon().equals("instagram")).count())
        .isEqualTo(1);

    assertThat(model.getCopyright()).isNotNull();
    assertThat(model.getCopyright()).isEqualTo("copyrights test");
  }
}