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
import lombok.experimental.Delegate;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.howlite.components.Grid;
import pl.ds.howlite.components.GridImpl;
import pl.ds.howlite.components.Styled;
import pl.ds.howlite.components.utils.GridDisplayType;
import pl.ds.howlite.components.utils.GridStyle;
import pl.ds.howlite.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class QuoteComponent implements Styled, Grid {

  @SlingObject
  private ResourceResolver resourceResolver;

  @Getter
  @Inject
  @Default(values = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam vel "
      + "dictum eros.</p>")
  private String quoteText;

  @Getter
  @Inject
  private String authorName;

  @Getter
  @Inject
  private String authorDescription;

  @Getter
  @Inject
  private Boolean showImage;

  @Inject
  private String authorImage;

  @Getter
  @Inject
  private String imageAlt;

  @Self
  @Delegate
  private DefaultStyledGridComponent grid;

  @Getter
  private String quoteGridClasses;

  @Getter
  private String quoteImageGridClasses;

  @Getter
  private String quoteContentGridClasses;

  @Getter
  private String authorGridClasses;

  @PostConstruct
  private void init() {
    initQuoteGridClasses();
    initQuoteImageGridClasses();
    initQuoteContentGridClasses();
    initAuthorGridClasses();
  }

  private void initQuoteGridClasses() {
    Grid quoteGrid = GridImpl.builder().lgColSize(11).mdColSize(12).smColSize(12).build();
    quoteGridClasses = String.join(" ",
        new GridStyle(quoteGrid, GridDisplayType.GRID).getClasses());
  }

  private void initQuoteImageGridClasses() {
    Grid quoteGrid = GridImpl.builder().lgColSize(12).mdColSize(12).smColSize(12).build();
    quoteImageGridClasses = String.join(" ", new GridStyle(quoteGrid).getClasses());
  }

  private void initQuoteContentGridClasses() {
    Grid quoteGrid = GridImpl.builder().lgColSize(12).mdColSize(12).smColSize(12).lgOffset(2)
        .mdOffset(3).build();
    quoteContentGridClasses = String.join(" ", new GridStyle(quoteGrid).getClasses());
  }

  private void initAuthorGridClasses() {
    Grid quoteGrid = GridImpl.builder().lgColSize(12).mdColSize(12).smColSize(12).lgOffset(2)
        .mdOffset(3).build();
    authorGridClasses = String.join(" ", new GridStyle(quoteGrid).getClasses());
  }

  public String getAuthorImage() {
    return LinkUtil.handleLink(authorImage, resourceResolver);
  }
}
