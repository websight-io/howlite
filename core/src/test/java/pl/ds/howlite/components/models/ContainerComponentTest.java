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

    assertThat(classes).containsExactly(expectedClasses);
  }

  private static Stream<Arguments> styleClasses() {
    return Stream.of(
        Arguments.of(GRID_PATH + "/gridWithSameSize",
            new String[]{"hl-grid", "hl-grid-6", "hl-grid-cols-6"}),
        Arguments.of(GRID_PATH + "/gridWithDifferentSize",
            new String[]{"hl-grid", "hl-grid-sm-4", "hl-grid-md-5", "hl-grid-lg-6",
                "hl-grid-sm-cols-4", "hl-grid-md-cols-5", "hl-grid-lg-cols-6"}),
        Arguments.of(GRID_PATH + "/gridWithSameSizeAndSameStart",
            new String[]{"hl-grid", "hl-grid-6", "hl-grid-cols-3-6"}),
        Arguments.of(GRID_PATH + "/gridWithSameSizeAndDifferentStart",
            new String[]{"hl-grid", "hl-grid-6", "hl-grid-sm-cols-1-6", "hl-grid-md-cols-2-6",
                "hl-grid-lg-cols-3-6"}),
        Arguments.of(GRID_PATH + "/gridWithDifferentSizeAndDifferentStart",
            new String[]{"hl-grid", "hl-grid-sm-4", "hl-grid-md-5", "hl-grid-lg-6",
                "hl-grid-sm-cols-1-4", "hl-grid-md-cols-2-5", "hl-grid-lg-cols-3-6"}),
        Arguments.of(GRID_PATH + "/gridWithDifferentSizeAndSameStart",
            new String[]{"hl-grid", "hl-grid-sm-4", "hl-grid-md-5", "hl-grid-lg-6",
                "hl-grid-sm-cols-3-4", "hl-grid-md-cols-3-5", "hl-grid-lg-cols-3-6"}),
        Arguments.of(GRID_PATH + "/gridWithDifferentSizeAndOnlyLgStart",
            new String[]{"hl-grid", "hl-grid-sm-4", "hl-grid-md-5", "hl-grid-lg-6",
                "hl-grid-sm-cols-1-4", "hl-grid-md-cols-1-5", "hl-grid-lg-cols-3-6"}),
        Arguments.of(GRID_PATH + "/gridWithLgBreakpoint",
            new String[]{"hl-grid", "hl-grid-sm-12", "hl-grid-md-12", "hl-grid-lg-6",
                "hl-grid-sm-cols-1-12", "hl-grid-md-cols-1-12", "hl-grid-lg-cols-3-6"}),
        Arguments.of(GRID_PATH + "/inlineWithSameSize",
            new String[]{"hl-grid--inline", "hl-grid-cols-6"}),
        Arguments.of(GRID_PATH + "/inlineWithDifferentSize",
            new String[]{"hl-grid--inline", "hl-grid-sm-cols-4", "hl-grid-md-cols-5",
                "hl-grid-lg-cols-6"}),
        Arguments.of(GRID_PATH + "/inlineWithSameSizeAndSameStart",
            new String[]{"hl-grid--inline", "hl-grid-cols-3-6"}),
        Arguments.of(GRID_PATH + "/inlineWithSameSizeAndDifferentStart",
            new String[]{"hl-grid--inline", "hl-grid-sm-cols-1-6", "hl-grid-md-cols-2-6",
                "hl-grid-lg-cols-3-6"}),
        Arguments.of(GRID_PATH + "/inlineWithDifferentSizeAndDifferentStart",
            new String[]{"hl-grid--inline", "hl-grid-sm-cols-1-4", "hl-grid-md-cols-2-5",
                "hl-grid-lg-cols-3-6"}),
        Arguments.of(GRID_PATH + "/inlineWithDifferentSizeAndSameStart",
            new String[]{"hl-grid--inline", "hl-grid-sm-cols-3-4", "hl-grid-md-cols-3-5",
                "hl-grid-lg-cols-3-6"})
    );
  }

}