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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.ds.websight.assets.core.api.Asset;
import pl.ds.websight.assets.core.api.AssetsConstants;
import pl.ds.websight.assets.core.api.Rendition;

class LinkUtilTest {

    private static final String PATH = "/content/test";
    private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

    @Test
    void handleInternalPageLink() {
        //given
        Resource page = context.create().resource(PATH + "/internal/page/");

        //when
        String parsedLink = LinkUtil.handleLink(page.getPath(), context.resourceResolver());

        //then
        Assertions.assertThat(parsedLink).isEqualTo("/content/test/internal/page.html");
    }

    @Test
    void handleInternalAssetLink() {
        //given
        ResourceResolver resourceResolver = mock(ResourceResolver.class);
        Resource assetResource = mock(Resource.class);
        ValueMap valueMap = mock(ValueMap.class);
        Asset asset = mock(Asset.class);
        Rendition originalRendition = mock(Rendition.class);
        when(resourceResolver.getResource(PATH + "/internal/asset/")).thenReturn(assetResource);
        when(assetResource.adaptTo(Asset.class)).thenReturn(asset);
        when(assetResource.getPath()).thenReturn(PATH + "/internal/asset/");
        when(assetResource.getValueMap()).thenReturn(valueMap);
        when(valueMap.get("jcr:primaryType", String.class)).thenReturn(AssetsConstants.NT_ASSET);
        when(asset.getOriginalRendition()).thenReturn(originalRendition);
        when(originalRendition.getPath()).thenReturn(PATH + "/internal/asset/renditions/original.png");

        //when
        String parsedLink = LinkUtil.handleLink(assetResource.getPath(), resourceResolver);

        //then
        Assertions.assertThat(parsedLink).isEqualTo("/content/test/internal/asset/renditions/original.png");
    }

    @Test
    void handleExternalLinkWithoutProtocol() {
        //given
        String link = "ds.pl";

        //when
        String parsedLink = LinkUtil.handleLink(link, context.resourceResolver());

        //then
        Assertions.assertThat(parsedLink).isEqualTo("http://ds.pl");
    }

    @Test
    void handleExternalLinkWithProtocol() {
        //given
        String link = "https://ds.pl";

        //when
        String parsedLink = LinkUtil.handleLink(link, context.resourceResolver());

        //then
        Assertions.assertThat(parsedLink).isEqualTo("https://ds.pl");
    }

    @Test
    void handleAnchorLink() {
        //given
        String link = "#someAnchorLink";

        //when
        String parsedLink = LinkUtil.handleLink(link, context.resourceResolver());

        //then
        Assertions.assertThat(parsedLink).isEqualTo("#someAnchorLink");
    }

}