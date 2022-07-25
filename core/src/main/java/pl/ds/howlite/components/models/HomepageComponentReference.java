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

import java.util.Objects;
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.howlite.components.utils.PageUtil;
import pl.ds.websight.pages.core.api.Page;
import pl.ds.websight.pages.core.api.PageManager;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public abstract class HomepageComponentReference<T> {

  @SlingObject
  private Resource resource;

  protected Resource homepage;

  @PostConstruct
  private void initReferenceComponent() {
    PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
    if (pageManager == null) {
      return;
    }

    Page rootPage = PageUtil.findTopLevelParentPageForCurrentPage(
        Objects.requireNonNull(pageManager.getContainingPage(resource.getPath())));
    homepage = PageUtil.findSiblingPageForCurrentPage(rootPage, "/libs/howlite/templates/homepage");
  }

  public T getComponent() {
    return homepage.getChild(getPath()) != null ? homepage.getChild(getPath()).adaptTo(getType())
        : null;
  }

  protected abstract String getPath();

  protected abstract Class<T> getType();
}
