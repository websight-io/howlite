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
class CtaComponentTest {

  private static final String PATH = "/components/cta";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(CtaComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("cta.json")), PATH);
  }

  @Test
  void defaultCtaComponentModelTest() {
    CtaComponent model = requireNonNull(
        context.resourceResolver().getResource(PATH + "/default")).adaptTo(CtaComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getLabel()).isEqualTo("Sample CTA");
    assertThat(model.getDisplayIcon()).isEqualTo("false");
    assertThat(model.getLink()).isNull();
    assertThat(model.getOpenInNewTab()).isEqualTo("false");
  }

  @Test
  void complexCtaComponentModelTest() {
    CtaComponent model = requireNonNull(context.resourceResolver().getResource(PATH + "/complex"))
        .adaptTo(CtaComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getLabel()).isEqualTo("Sample CTA");
    assertThat(model.getDisplayIcon()).isEqualTo("true");
    assertThat(model.getLink()).isEqualTo("https://www.example.com");
    assertThat(model.getOpenInNewTab()).isEqualTo("true");
  }

}