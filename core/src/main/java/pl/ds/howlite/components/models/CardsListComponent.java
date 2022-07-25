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
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import pl.ds.howlite.components.Grid;
import pl.ds.howlite.components.Styled;

@Model(adaptables = Resource.class)
public class CardsListComponent implements Styled, Grid {

  @Getter
  @Inject
  @Default(booleanValues = false)
  private boolean isSlider;

  @Getter
  @Inject
  @Default(values = "h3")
  private String headingLevel;

  @Getter
  @Inject
  @Default(values = "hl-title__heading--default")
  private String headingSize;

  @Getter
  @Inject
  @Default(intValues = 2)
  private Integer itemsPerRowSm;

  @Getter
  @Inject
  @Default(intValues = 2)
  private Integer itemsPerRowMd;

  @Getter
  @Inject
  @Default(intValues = 3)
  private Integer itemsPerRowLg;

  @Self
  @Delegate
  private DefaultStyledGridComponent grid;

}
