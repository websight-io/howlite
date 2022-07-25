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

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.jetbrains.annotations.Nullable;
import pl.ds.websight.assets.core.api.Asset;
import pl.ds.websight.assets.core.api.AssetsConstants;

public class LinkUtil {

  private static final String ANCHOR_LINK_PREFIX = "#";

  private LinkUtil() {
    // no instance
  }

  public static String handleLink(String link, ResourceResolver resourceResolver) {
    if (StringUtils.isNotEmpty(link)) {
      if (isInternal(link, resourceResolver)) {
        return handleInternalLink(link, resourceResolver);
      } else if (isAnchorLink(link)) {
        return link;
      } else {
        return handleExternalLink(link);
      }
    }

    return link;
  }

  private static String handleInternalLink(String link, ResourceResolver resourceResolver) {
    if (isAsset(link, resourceResolver)) {
      Asset asset = getAssetForProvidedLink(link, resourceResolver);
      if (asset != null && asset.getOriginalRendition() != null) {
        return asset.getOriginalRendition().getPath();
      }

      return null;
    }

    return addHtmlExtensionSuffixToLink(link);
  }

  private static boolean isAnchorLink(String link) {
    return link.startsWith(ANCHOR_LINK_PREFIX);
  }

  private static String handleExternalLink(String link) {
    return addProtocolPrefixToLink(link);
  }

  private static boolean isInternal(String link, ResourceResolver resourceResolver) {
    return resourceResolver.getResource(link) != null;
  }

  private static boolean isAsset(String link, ResourceResolver resourceResolver) {
    Resource resource = resourceResolver.getResource(link);
    return AssetsConstants.NT_ASSET.equals(getPrimaryType(resource));
  }

  private static String addProtocolPrefixToLink(String link) {
    return link.startsWith("http") ? link : "http://" + link;
  }

  private static String addHtmlExtensionSuffixToLink(String link) {
    return StringUtils.removeEnd(link, "/") + ".html";
  }

  @Nullable
  private static Asset getAssetForProvidedLink(String link, ResourceResolver resourceResolver) {
    Resource assetResource = resourceResolver.getResource(link);
    Asset asset = null;
    if (assetResource != null) {
      asset = assetResource.adaptTo(Asset.class);
    }
    return asset;
  }

  private static String getPrimaryType(Resource resource) {
    if (resource == null) {
      return null;
    }
    return resource.getValueMap().get("jcr:primaryType", String.class);
  }
}