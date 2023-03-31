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
  title: 'ComponentOverlay_/content/howlite-test/pages/Title/jcr:content/rootcontainer/maincontainer/pagesection/title'
};

describe('Title component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/howlite-test/pages/Title.html');
    cy.percySnapshotPreview('Title preview');
  });

  it('renders correctly in edit mode', function () {
    cy.intercept(
      'POST',
      '**/pagesection/title.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    // assert initial state before test
    cy.request(
      '/content/howlite-test/pages/Title/jcr:content/rootcontainer/maincontainer/pagesection/title.json'
    )
      .its('body')
      .should('deep.eq', {
          'sling:resourceType': 'howlite/components/title',
          'jcr:primaryType': 'nt:unstructured',
      });

    // execute test
    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Title::editor'
    );

    cy.getByTestId(paths.title)
      .click()
      .find(selectors.overlayName)
      .should('contain.text', 'Title');

    cy.percySnapshotPageEditor('Title editor');

    cy.getByTestId(testIds.editIcon).click();

    cy.getByTestId('ModalDialog_Title')
      .findByTestId('RadioElement_h1').click();
    cy.getByTestId('ModalDialog_Title')
      .findByTestId('RadioElement_hl-title__heading--size-2').click();
    cy.getByTestId('ModalDialog_Title')
      .findByTestId('Input_Headingtext').clear().type('New heading');
    cy.getByTestId('ModalDialog_Title')
      .findByTestId('Input_Addanoverline').click();
    cy.getByTestId('ModalDialog_Title')
      .findByTestId('Input_Overlinetext').clear().type('New overline text');

    cy.percySnapshotDialog('Title dialog');

    cy.getByTestId(testIds.dialogSubmitButton).click();
    cy.wait('@saveProperties');

    // assert model change after test
    cy.request(
      '/content/howlite-test/pages/Title/jcr:content/rootcontainer/maincontainer/pagesection/title.json'
    )
      .its('body')
      .should('deep.eq', {
        'sling:resourceType': 'howlite/components/title',
        title: 'New heading',
        showSubtitle: 'true',
        subtitle: 'New overline text',
        'jcr:primaryType': 'nt:unstructured',
        headingLevel: 'h1',
        headingSize: 'hl-title__heading--size-2',
        'anchorId': '',
      });
  });
});
