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

package pl.ds.howlite.components.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import pl.ds.websight.pages.core.api.Page;
import pl.ds.websight.pages.core.api.PageConstants;

public class PageUtil {

  public static Page findTopLevelParentPageForCurrentPage(@NotNull Page page) {
    while (page.getParent() != null) {
      page = page.getParent();
    }

    return page;
  }

  public static Resource findSiblingPageForCurrentPage(@NotNull Page rootPage,
      @NotNull String template) {
    List<Resource> resources = new ArrayList<>();
    Objects.requireNonNull(rootPage.getResource().getParent()).getChildren()
        .forEach(resources::add);

    return resources.stream()
        .filter(PageUtil.isResourceSelectedTemplate(template))
        .findFirst().orElse(null);
  }

  private static Predicate<Resource> isResourceSelectedTemplate(String template) {
    return resource -> {
      if (resource.getChild(JCR_CONTENT) == null) {
        return false;
      }
      Object o = resource.getChild(JCR_CONTENT).getValueMap().get(PageConstants.PN_WS_TEMPLATE);
      return o != null && o.equals(template);
    };
  }

  private static final String JCR_CONTENT = "jcr:content";

  private PageUtil() {
    // no instance
  }
}
