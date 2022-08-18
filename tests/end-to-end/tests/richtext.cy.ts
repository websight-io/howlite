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

import { selectors, testIds } from '../support/consts';

const paths = {
  richtext: 'ComponentOverlay_rootcontainer/maincontainer/pagesection/richtext',
  richtextWithChecklist:
    'ComponentOverlay_rootcontainer/maincontainer/pagesection_1/richtext'
};

const LOREM_IPSUM =
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sodales, neque in ultricies euismod, ex purus eleifend ex, ut pretium mauris felis sit amet mauris.';

const LIST = '<ul><li><p>List item</p></li><li><p>List item</p></li></ul>';

describe('Rich text editor component', () => {
  it('renders correctly in preview mode', function () {
    cy.login();

    cy.visit('/content/howlite-test/pages/Rich-text.html');

    cy.percySnapshot('Rich text preview');
  });

  it('renders correctly in edit mode', function () {
    cy.login();

    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Rich-text::editor'
    );

    cy.percySnapshot('Rich text editor');

    cy.getByTestId(paths.richtext)
      .click()
      .find(selectors.overlayName)
      .should('have.text', 'Rich text editor');

    cy.getByTestId(testIds.editIcon).click();

    cy.percySnapshot('Rich text dialog');

    cy.get('.ProseMirror').should('have.text', LOREM_IPSUM);

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.request(
      '/content/howlite-test/pages/Rich-text/jcr:content/rootcontainer/maincontainer/pagesection/richtext.json'
    )
      .its('body')
      .should('deep.eq', {
        'sling:resourceType': 'howlite/components/richtext',
        'jcr:primaryType': 'nt:unstructured',
        text: `<p>${LOREM_IPSUM}</p>`
      });
  });

  it('renders check lists correctly in edit mode', function () {
    cy.login();

    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Rich-text::editor'
    );

    cy.percySnapshot('Rich text editor');

    cy.getByTestId(paths.richtextWithChecklist)
      .click()
      .find(selectors.overlayName)
      .should('have.text', 'Rich text editor');

    cy.getByTestId(testIds.editIcon).click();

    cy.percySnapshot('Rich text dialog');

    cy.get('.ProseMirror').should('have.html', LIST);

    cy.get('#undefined-1').click();
    cy.get('div input[name="classes"]').get(
      '[value="hl-rich-text--checked-bullet-points"]'
    );

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.request(
      '/content/howlite-test/pages/Rich-text/jcr:content/rootcontainer/maincontainer/pagesection_1/richtext.json'
    )
      .its('body')
      .should('deep.eq', {
        'sling:resourceType': 'howlite/components/richtext',
        'jcr:primaryType': 'nt:unstructured',
        classes: 'hl-rich-text--checked-bullet-points',
        text: LIST
      });
  });
});
