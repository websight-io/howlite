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
import javax.inject.Inject;
import lombok.Getter;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.howlite.components.Grid;
import pl.ds.howlite.components.Styled;
import pl.ds.howlite.components.utils.GridDisplayType;
import pl.ds.howlite.components.utils.GridStyle;
import pl.ds.howlite.components.utils.LinkUtil;

@Slf4j
@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ContainerComponent implements Styled, Grid {

  private static final String BACKGROUND_NONE = "none";
  private static final String BACKGROUND_URL_PATTERN = "url(\"%s\")";

  @SlingObject
  private ResourceResolver resourceResolver;

  @Inject
  @Default(values = "grid")
  @Getter
  private String displayType;

  @Inject
  private String backgroundImageSm;

  @Inject
  private String backgroundImageMd;

  @Inject
  private String backgroundImageLg;

  public String getBackgroundImageSm() {
    return getBackgroundImage(backgroundImageSm);
  }

  public String getBackgroundImageMd() {
    return getBackgroundImage(backgroundImageMd);
  }

  public String getBackgroundImageLg() {
    return getBackgroundImage(backgroundImageLg);
  }

  private String getBackgroundImage(String image) {
    if (StringUtils.isEmpty(image)) {
      return BACKGROUND_NONE;
    }

    return String.format(BACKGROUND_URL_PATTERN, LinkUtil.handleLink(image, resourceResolver));
  }

  @Self
  private DefaultStyledComponent style;

  @Self
  @Delegate
  private DefaultGridComponent grid;

  private String[] componentClasses;

  @Override
  public String[] getClasses() {
    return componentClasses;
  }

  @PostConstruct
  private void init() {
    GridDisplayType gridDisplayType = queryDisplayType();

    componentClasses = Stream.concat(
            Arrays.stream(style.getClasses()),
            new GridStyle(this, gridDisplayType).getClasses().stream())
        .collect(Collectors.toCollection(LinkedHashSet::new))
        .toArray(new String[]{});
  }

  private GridDisplayType queryDisplayType() {
    if (StringUtils.isNotEmpty(displayType)) {
      try {
        return GridDisplayType.valueOf(displayType.toUpperCase());
      } catch (IllegalArgumentException e) {
        log.debug("Invalid display type: {}.", displayType);
      }
    }
    return GridDisplayType.GRID;
  }

}
