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
class NavigationComponentTest {

  private static final String PATH = "/components/navigation";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(NavigationComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("navigation.json")),
        PATH);
  }

  @Test
  void defaultNavigationComponentModelTest() {
    NavigationComponent model = requireNonNull(
        context.resourceResolver().getResource(PATH + "/default")).adaptTo(
        NavigationComponent.class);

    assertThat(model).isNotNull();
  }

  @Test
  void complexNavigationComponentModelTest() {
    NavigationComponent model = requireNonNull(
        context.resourceResolver().getResource(PATH + "/complex")).adaptTo(
        NavigationComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getMenuItems()).isNotNull();
    assertThat(model.getMenuItems()).isNotEmpty().hasSize(2);
    assertThat(model.getMenuItems().stream()
        .filter(i -> i.getLabel().equals("Navigation item 1") &&
            i.getLink().equals("http://www.navigate-to-link-1.com") &&
            i.getOpenInNewTab().equals("true"))
        .count()).isEqualTo(1);
    assertThat(model.getMenuItems().stream()
        .filter(i -> i.getLabel().equals("Navigation item 2") &&
            i.getLink().equals("http://www.navigate-to-link-2.com") &&
            i.getOpenInNewTab().equals("false"))
        .count()).isEqualTo(1);

  }
}