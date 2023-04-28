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
import pl.ds.howlite.components.Styled;
import pl.ds.howlite.components.utils.LinkUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class CtaComponent implements Styled {

  private static final String DEFAULT_LABEL = "CTA label";

  @SlingObject
  private ResourceResolver resourceResolver;

  @Getter
  @Inject
  @Default(values = DEFAULT_LABEL)
  private String label;

  @Inject
  private String link;

  @Getter
  @Inject
  @Default(values = "false")
  private String openInNewTab;

  @Getter
  @Inject
  @Default(values = "false")
  private String displayIcon;

  @Self
  @Delegate
  private DefaultStyledComponent styled;

  @PostConstruct
  private void init() {
    if (label.isEmpty()) {
      label = DEFAULT_LABEL;
    }
  }

  public String getLink() {
    return LinkUtil.handleLink(link, resourceResolver);
  }
}
