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
  accordionItem:
    'ComponentOverlay_rootcontainer/maincontainer/pagesection/accordion/accordionitem1',
  emptyAccordion:
    'ComponentOverlay_rootcontainer/maincontainer/pagesection_1/accordion'
};

describe('Accordion component', function () {
  it('renders correctly in preview mode', function () {
    cy.login();

    cy.visit('/content/howlite-test/pages/Accordion.html');

    cy.percySnapshotWithAuth('Accordion preview');
  });

  it('renders correctly in edit mode', function () {
    cy.login();

    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Accordion::editor'
    );

    cy.intercept(
      'POST',
      '**/pagesection/accordion/accordionitem1.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.percySnapshotWithAuth('Accordion editor');

    cy.getByTestId(paths.accordionItem)
      .click()
      .find(selectors.overlayName)
      .should('have.text', 'Accordion Item');

    cy.getByTestId(testIds.editIcon).click();

    cy.percySnapshotWithAuth('Accordion dialog');

    cy.getByTestId('Input_Title-container').clear().type('Title');
    cy.get('.ProseMirror').clear().type('Text');
    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
      '/content/howlite-test/pages/Accordion/jcr:content/rootcontainer/maincontainer/pagesection/accordion/accordionitem1.json'
    )
      .its('body')
      .should('deep.eq', {
        'sling:resourceType': 'howlite/components/accordion/accordionitem',
        'jcr:primaryType': 'nt:unstructured',
        title: 'Title',
        content: '<p>Text</p>'
      });
  });

  it('renders empty accordion correctly in edit mode', function () {
    cy.login();

    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Accordion::editor'
    );

    cy.percySnapshotWithAuth('Accordion editor');

    cy.getByTestId(paths.emptyAccordion)
      .click()
      .find(selectors.overlayName)
      .should('have.text', 'Accordion');
  });
});
