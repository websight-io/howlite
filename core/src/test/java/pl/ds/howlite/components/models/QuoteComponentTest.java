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
class QuoteComponentTest {

  private static final String PATH = "/components/quote";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(QuoteComponent.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("quote.json")), PATH);
  }

  @Test
  void defaultCtaComponentModelTest() {
    QuoteComponent model = requireNonNull(
        context.resourceResolver().getResource(PATH + "/default")).adaptTo(QuoteComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getQuoteText()).isEqualTo("<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam vel dictum eros.</p>");
    assertThat(model.getQuoteGridClasses().split(" ")).containsExactlyInAnyOrder("hl-grid", "hl-cols-lg-11", "hl-cols-md-12", "hl-cols-sm-12");
    assertThat(model.getQuoteImageGridClasses().split(" ")).containsExactlyInAnyOrder("hl-grid-component", "hl-cols-12");
    assertThat(model.getQuoteContentGridClasses().split(" ")).containsExactlyInAnyOrder("hl-grid-component", "hl-cols-12", "hl-offset-lg-2", "hl-offset-md-3");
    assertThat(model.getAuthorGridClasses().split(" ")).containsExactlyInAnyOrder("hl-grid-component", "hl-cols-12", "hl-offset-lg-2", "hl-offset-md-3");
  }

  @Test
  void complexQuoteComponentModelTest() {
    QuoteComponent model = requireNonNull(context.resourceResolver().getResource(PATH + "/complex"))
        .adaptTo(QuoteComponent.class);

    assertThat(model).isNotNull();
    assertThat(model.getQuoteText()).isEqualTo("Sample Quote");
    assertThat(model.getAuthorName()).isEqualTo("Author Name");
    assertThat(model.getAuthorDescription()).isEqualTo("Author Description");
    assertThat(model.getAuthorImage()).isEqualTo("http://content/author/image");
    assertThat(model.getImageAlt()).isEqualTo("Author profile image");
  }

}