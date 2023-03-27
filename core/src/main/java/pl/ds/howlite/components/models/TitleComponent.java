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

import javax.inject.Inject;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import pl.ds.howlite.components.Grid;
import pl.ds.howlite.components.Styled;

@Model(adaptables = Resource.class)
public class TitleComponent implements Styled, Grid {

  @Inject
  @Getter
  @Default(values = "Add your heading here")
  private String title;

  @Inject
  @Getter
  @Default(values = "h2")
  private String headingLevel;

  @Inject
  @Getter
  @Default(values = "hl-title__heading--size-4")
  private String headingSize;

  @Inject
  @Getter
  @Default(booleanValues = false)
  private Boolean showSubtitle;

  @Inject
  @Getter
  @Default(values = "Add your text here")
  private String subtitle;

  @Inject
  @Getter
  @Default(values = "Add your anchor-id here")
  private String anchorId;

  @Self
  @Delegate
  private DefaultStyledGridComponent style;
}
