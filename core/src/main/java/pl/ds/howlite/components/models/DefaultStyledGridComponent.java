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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import pl.ds.howlite.components.Grid;
import pl.ds.howlite.components.Styled;
import pl.ds.howlite.components.utils.GridStyle;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class DefaultStyledGridComponent implements Styled, Grid {

  @Self
  private DefaultStyledComponent style;

  @Self
  private DefaultGridComponent grid;

  private String[] componentClasses;

  @Override
  public String[] getClasses() {
    return componentClasses;
  }

  @Override
  public Integer getSmColSize() {
    return grid.getSmColSize();
  }

  @Override
  public Integer getMdColSize() {
    return grid.getMdColSize();
  }

  @Override
  public Integer getLgColSize() {
    return grid.getLgColSize();
  }

  @Override
  public Integer getSmColStart() {
    return grid.getSmColStart();
  }

  @Override
  public Integer getMdColStart() {
    return grid.getMdColStart();
  }

  @Override
  public Integer getLgColStart() {
    return grid.getLgColStart();
  }

  @PostConstruct
  private void init() {
    componentClasses = Stream.concat(
            Arrays.stream(style.getClasses()),
            new GridStyle(this).getClasses().stream())
        .collect(Collectors.toCollection(LinkedHashSet::new))
        .toArray(new String[]{});
  }
}
