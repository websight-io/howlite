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

import javax.annotation.PostConstruct;
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
public class RichTextComponent implements Styled, Grid {

  private static final String DEFAULT_TEXT =
      "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam vel dictum eros. "
          + "Morbi in elementum lacus. Duis non viverra nulla. Sed pretium urna risus, "
          + "at dapibus quam varius consectetur. Sed eget ex condimentum, fringilla ligula vitae, "
          + "mattis purus. Etiam sed ultrices leo. Integer nec arcu et sem tincidunt scelerisque. "
          + "In sit amet accumsan erat. Sed malesuada nibh sagittis iaculis molestie. Nunc "
          + "sollicitudin turpis ipsum. Ut ultricies dictum leo, in fermentum eros dignissim eget. "
          + "Mauris semper accumsan vehicula. Phasellus nec lectus augue. Ut suscipit, arcu eu "
          + "sodales faucibus, mauris leo pulvinar elit, nec fringilla augue elit ac ex.</p>";

  @Inject
  @Getter
  @Default(values = DEFAULT_TEXT)
  private String text;

  @Self
  @Delegate
  private DefaultStyledGridComponent style;

  @PostConstruct
  private void init() {
    if ("<p></p>".equals(text)) {
      text = DEFAULT_TEXT;
    }
  }
}
