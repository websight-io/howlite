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

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.howlite.components.models.CardsListComponent;
import pl.ds.howlite.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class CardComponent {

  @Getter
  private String headingLevel;

  @Getter
  private String headingSize;

  @Getter
  @Inject
  @Default(values = "Card Title")
  private String title;

  @Getter
  @Inject
  @Default(values = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque "
      + "varius auctor pulvinar. Duis feugiat malesuada enim tincidunt lacinia. Nulla at "
      + "elementum augue, id facilisis libero.</p>")
  private String content;

  @Inject
  private String image;

  @Getter
  @Inject
  private String alt;

  @Getter
  @Inject
  private String ctaLabel;

  @Getter
  @Inject
  private String ctaLink;

  @Getter
  @Inject
  @Default(values = "false")
  private String ctaOpenInNewTab;

  @Getter
  @Inject
  @Default(values = "true")
  private String ctaDisplayIcon;

  @Getter
  @Inject
  @Default(values = "hl-cta--text")
  private String ctaClasses;

  @SlingObject
  private Resource resource;

  @PostConstruct
  private void init() {
    initHeadingProperties();
  }

  private void initHeadingProperties() {
    Resource parent = resource.getParent();
    if (parent == null) {
      return;
    }

    CardsListComponent cardsListComponent = parent.adaptTo(CardsListComponent.class);
    if (cardsListComponent == null) {
      return;
    }

    this.headingLevel = cardsListComponent.getHeadingLevel();
    this.headingSize = cardsListComponent.getHeadingSize();
  }

  public String getImage() {
    return LinkUtil.handleLink(image, resource.getResourceResolver());
  }

  public String getCtaLink() {
    return LinkUtil.handleLink(ctaLink, resource.getResourceResolver());
  }

}
