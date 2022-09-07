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

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(SlingContextExtension.class)
class ContainerComponentTest {

  private static final String PATH = "/content/container";
  private static final String GRID_PATH = "/content/container/grid";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(ContainerComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("container.json")),
        PATH);
  }

  @Test
  void defaultContainerComponentModelTest() {
    ContainerComponent model = requireNonNull(
        context.resourceResolver().getResource(PATH + "/default")).adaptTo(
        ContainerComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getBackgroundImageSm()).isEqualTo("none");
    assertThat(model.getBackgroundImageMd()).isEqualTo("none");
    assertThat(model.getBackgroundImageLg()).isEqualTo("none");
  }

  @Test
  void containerComponentModelTest() {
    ContainerComponent model = requireNonNull(
        context.resourceResolver().getResource(PATH + "/complex")).adaptTo(
        ContainerComponent.class);

    assertThat(model).isNotNull();
  }

  @ParameterizedTest
  @MethodSource("styleClasses")
  void shouldPrepareClasses(String component, String[] expectedClasses) {
    ContainerComponent styleComponent = requireNonNull(
        context.resourceResolver().getResource(component)).adaptTo(ContainerComponent.class);

    assertThat(styleComponent).isNotNull();
    Collection<String> classes = Arrays.asList(styleComponent.getClasses());

    assertThat(classes).containsExactlyInAnyOrder(expectedClasses);
  }

  private static Stream<Arguments> styleClasses() {
    return Stream.of(
        Arguments.of(GRID_PATH + "/gridWithDefaultParameters",
            new String[]{"hl-grid", "hl-cols-12"}),
        Arguments.of(GRID_PATH + "/gridWithSameSize",
            new String[]{"hl-grid", "hl-cols-6"}),
        Arguments.of(GRID_PATH + "/gridWithDifferentSize",
            new String[]{"hl-grid", "hl-cols-sm-4", "hl-cols-md-5", "hl-cols-lg-6"}),
        Arguments.of(GRID_PATH + "/gridWithSameSizeAndSameOffset",
            new String[]{"hl-grid", "hl-cols-6", "hl-offset-3"}),
        Arguments.of(GRID_PATH + "/gridWithSameSizeAndDifferentOffset",
            new String[]{"hl-grid", "hl-cols-6", "hl-offset-sm-1",
                "hl-offset-md-2", "hl-offset-lg-3"}),
        Arguments.of(GRID_PATH + "/gridWithDifferentSizeAndDifferentOffset",
            new String[]{"hl-grid", "hl-cols-sm-4", "hl-cols-md-5", "hl-cols-lg-6",
                "hl-offset-sm-1", "hl-offset-md-2", "hl-offset-lg-3"}),
        Arguments.of(GRID_PATH + "/gridWithDifferentSizeAndSameOffset",
            new String[]{"hl-grid", "hl-cols-sm-4", "hl-cols-md-5", "hl-cols-lg-6",
                "hl-offset-3"}),
        Arguments.of(GRID_PATH + "/gridWithDifferentSizeAndOnlyLgOffset",
            new String[]{"hl-grid", "hl-cols-sm-4", "hl-cols-md-5", "hl-cols-lg-6",
                "hl-offset-lg-3"}),
        Arguments.of(GRID_PATH + "/gridWithLgBreakpoint",
            new String[]{"hl-grid", "hl-cols-sm-12", "hl-cols-md-12", "hl-cols-lg-6",
                "hl-offset-lg-3"})
    );
  }

}