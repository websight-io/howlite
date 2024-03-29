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
  quote: 'ComponentOverlay_/content/howlite-test/pages/Quote/jcr:content/rootcontainer/maincontainer/pagesection/quote'
};

describe('Quote component', () => {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/howlite-test/pages/Quote.html');
    cy.percySnapshotPreview('Quote preview');
  });

  it('renders correctly in edit mode', function () {
    cy.intercept(
      'POST',
      '**/pagesection/quote.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Quote::editor'
    );

    cy.getByTestId(paths.quote)
      .click()
      .find(selectors.overlayName)
      .should('contain.text', 'Quote');

    cy.percySnapshotPageEditor('Quote editor');

    cy.getByTestId(testIds.editIcon).click();

    cy.getByTestId('ModalDialog_Quote')
      .find('.ProseMirror').should('have.text', 'Default quote component.');
    cy.getByTestId('ModalDialog_Quote')
      .findByTestId('Input_Author’sname').clear().type('John Doe');
    cy.getByTestId('ModalDialog_Quote')
      .findByTestId('Input_Author’sdescription')
      .clear()
      .type('Automated tester');
    cy.getByTestId('ModalDialog_Quote')
      .findByTestId('Input_Author’simage')
      .findByTestId('Input_Author’simage--toggle-cross-icon')
      .click();
    cy.getByTestId('ModalDialog_Quote')
      .findByTestId('Input_Author’simage--input').get('[value="true"]');
    //TODO: Asset uploading should be tested in image.cy.ts
    cy.getByTestId('ModalDialog_Quote')
      .findByTestId('Input_Alttext').clear().type('Image from John Doe');

    cy.percySnapshotDialog('Quote dialog');

    cy.getByTestId(testIds.dialogSubmitButton).click();
    cy.wait('@saveProperties');

    cy.request(
      '/content/howlite-test/pages/Quote/jcr:content/rootcontainer/maincontainer/pagesection/quote.json'
    )
      .its('body')
      .should('deep.eq', {
        'sling:resourceType': 'howlite/components/quote',
        authorDescription: 'Automated tester',
        showImage: 'true',
        imageAlt: 'Image from John Doe',
        quoteText: '<p>Default quote component.</p>',
        authorName: 'John Doe',
        'jcr:primaryType': 'nt:unstructured'
  });
  });
});
