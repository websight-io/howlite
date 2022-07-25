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
class AccordionItemComponentTest {

  private static final String PATH = "/content/accordion";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(AccordionItemComponent.class);
    context.load().json(requireNonNull(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("accordionItem.json")),
        PATH);
  }

  @Test
  void accordionItemDefaultValuesTest() {
    AccordionItemComponent model = requireNonNull(context.resourceResolver()
        .getResource(PATH + "/accordion-default/accordionitem-default")).adaptTo(
        AccordionItemComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getTitle()).isEqualTo("Accordion item title");
        assertThat(model.getContent()).isEqualTo("Add your content here");
    }

  @Test
  void simpleAccordionItemTest() {
    AccordionItemComponent model = requireNonNull(context.resourceResolver()
        .getResource(PATH + "/accordion-default/accordionitem-simple"))
        .adaptTo(AccordionItemComponent.class);

        assertThat(model).isNotNull();
        assertThat(model.getTitle()).isEqualTo("Simple title");
        assertThat(model.getContent()).isEqualTo("Simple content");
    }

  @Test
  void headingLevelFallbackTest() {
    AccordionItemComponent model = requireNonNull(context.resourceResolver()
        .getResource(PATH + "/accordion-with-hlevel/accordionitem-without-hlevel"))
        .adaptTo(AccordionItemComponent.class);

        assertThat(model).isNotNull();
    }
}