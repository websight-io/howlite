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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(SlingContextExtension.class)
class DefaultStyledGridComponentTest {

  private static final String PATH = "/content/styled";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(DefaultStyledGridComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("styled.json")), PATH);

  }

  @ParameterizedTest
  @MethodSource("styleClasses")
  void shouldPrepareClasses(String component, String[] expectedClasses) {
    DefaultStyledGridComponent styleComponent = requireNonNull(
        context.resourceResolver().getResource(component)).adaptTo(
        DefaultStyledGridComponent.class);

    assertThat(styleComponent).isNotNull();
    Collection<String> classes = Arrays.asList(styleComponent.getClasses());

    assertThat(classes).containsExactly(expectedClasses);
  }

  private static Stream<Arguments> styleClasses() {
    return Stream.of(
        Arguments.of(PATH + "/sameSize", new String[]{"hl-grid-cols-6"}),
        Arguments.of(PATH + "/differentSize",
            new String[]{"hl-grid-sm-cols-4", "hl-grid-md-cols-5", "hl-grid-lg-cols-6"}),
        Arguments.of(PATH + "/sameSizeAndSameStart", new String[]{"hl-grid-cols-3-6"}),
        Arguments.of(PATH + "/sameSizeAndDifferentStart",
            new String[]{"hl-grid-sm-cols-1-6", "hl-grid-md-cols-2-6", "hl-grid-lg-cols-3-6"}),
        Arguments.of(PATH + "/differentSizeAndDifferentStart",
            new String[]{"hl-grid-sm-cols-1-4", "hl-grid-md-cols-2-5", "hl-grid-lg-cols-3-6"}),
        Arguments.of(PATH + "/differentSizeAndSameStart",
            new String[]{"hl-grid-sm-cols-3-4", "hl-grid-md-cols-3-5", "hl-grid-lg-cols-3-6"})
    );
  }

}