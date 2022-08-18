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
  title: 'ComponentOverlay_rootcontainer/maincontainer/pagesection/title'
};

describe('Heading component', function () {
  it('renders correctly in preview mode', function () {
    cy.login();

    cy.visit('/content/howlite-test/pages/Heading.html');

    cy.percySnapshot("Heading preview");
  });

  it('renders correctly in edit mode', function () {
    cy.login();

    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Heading::editor'
    );

    cy.intercept(
      'POST',
      '**/pagesection/title.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.percySnapshot("Heading editor");

    cy.getByTestId(paths.title)
      .click()
      .find(selectors.overlayName)
      .should('have.text', 'Heading');

    cy.getByTestId(testIds.editIcon).click();

    cy.percySnapshot("Heading dialog");

    cy.getByTestId('RadioElement_h1').click();
    cy.getByTestId('RadioElement_hl-title__heading--size-2').click();
    cy.getByTestId('Input_Headingtext').clear().type('New heading');
    cy.getByTestId('Input_Addanoverline').click();
    cy.getByTestId('Input_Overlinetext').clear().type('New overline text');
    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
      '/content/howlite-test/pages/Heading/jcr:content/rootcontainer/maincontainer/pagesection/title.json'
    )
      .its('body')
      .should('deep.eq', {
        'sling:resourceType': 'howlite/components/title',
        title: 'New heading',
        showSubtitle: 'true',
        subtitle: 'New overline text',
        'jcr:primaryType': 'nt:unstructured',
        headingLevel: 'h1',
        headingSize: 'hl-title__heading--size-2'
      });
  });
});
