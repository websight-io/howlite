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

import java.util.Objects;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.jetbrains.annotations.Nullable;
import pl.ds.websight.assets.core.api.Asset;
import pl.ds.websight.assets.core.api.AssetsConstants;

public class LinkUtil {

  private static final String ANCHOR_LINK_PREFIX = "#";
  public static final String PUBLISHED = "/published";
  public static final String CONTENT = "/content";

  private LinkUtil() {
    // no instance
  }

  public static String handleLink(String link, ResourceResolver resourceResolver) {
    if (StringUtils.isNotEmpty(link)) {
      if (isAnchorLink(link)) {
        return link;
      } else if (isInternal(link, resourceResolver)) {
        return handleInternalLink(link, resourceResolver);
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
        return isPublished(link) ? RegExUtils.replaceFirst(asset.getOriginalRendition().getPath(),
            CONTENT,
            PUBLISHED) : asset.getOriginalRendition().getPath();
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

  public static boolean isInternal(String link, ResourceResolver resourceResolver) {
    return Objects.nonNull(getResource(removeAnchor(link), resourceResolver));
  }

  private static boolean isAsset(String link, ResourceResolver resourceResolver) {
    Resource resource = getResource(link, resourceResolver);
    return AssetsConstants.NT_ASSET.equals(getPrimaryType(resource));
  }

  private static String addProtocolPrefixToLink(String link) {
    return link.startsWith("http") ? link : "http://" + link;
  }

  private static String addHtmlExtensionSuffixToLink(String link) {
    String anchor = "";
    if (link.contains(ANCHOR_LINK_PREFIX)) {
      anchor = link.substring(link.lastIndexOf(ANCHOR_LINK_PREFIX));
      link = link.substring(0, link.lastIndexOf(ANCHOR_LINK_PREFIX));
    }

    return StringUtils.removeEnd(link, "/") + ".html" + anchor;
  }

  private static String removeAnchor(String link) {
    if (link.contains(ANCHOR_LINK_PREFIX)) {
      return link.substring(0, link.lastIndexOf(ANCHOR_LINK_PREFIX));
    }
    return link;
  }

  @Nullable
  private static Asset getAssetForProvidedLink(String link, ResourceResolver resourceResolver) {
    Resource assetResource = getResource(link, resourceResolver);
    Asset asset = null;
    if (assetResource != null) {
      asset = assetResource.adaptTo(Asset.class);
    }
    return asset;
  }

  public static Resource getResource(String link, ResourceResolver resourceResolver) {
    if (link.startsWith(PUBLISHED)) {
      return resourceResolver.getResource(link.replaceFirst(PUBLISHED, CONTENT));
    }
    return resourceResolver.getResource(link);
  }

  public static boolean isPublished(String link) {
    return link.startsWith(PUBLISHED);
  }

  private static String getPrimaryType(Resource resource) {
    if (resource == null) {
      return null;
    }
    return resource.getValueMap().get("jcr:primaryType", String.class);
  }
}