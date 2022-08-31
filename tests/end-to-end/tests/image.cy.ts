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
  imagePlaceholder:
    'ComponentOverlay_rootcontainer/maincontainer/pagesection/image_1'
};

describe('Image component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/howlite-test/pages/Image.html');
    cy.percySnapshotPreview('Image preview');

    cy.get('.hl-image__link').last().should('have.attr', 'target', '_blank');
  });

  it('renders correctly in edit mode', function () {
    cy.intercept('GET', '**/libs/howlite/web_root/images/broken-image.svg').as(
      'assetLoaded'
    );

    cy.intercept(
      'POST',
      '**/pagesection/image_1.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Image::editor'
    );

    cy.wait('@assetLoaded');
    cy.percySnapshotPageEditor('Image editor');

    cy.getByTestId(paths.imagePlaceholder)
      .click()
      .find(selectors.overlayName)
      .should('have.text', 'Image');

    cy.getByTestId(testIds.editIcon).click();

    // TODO: check asset drag & drop
    cy.getByTestId('Input_Alttext').clear().type('Image of a logo');
    cy.getByTestId('Input_Link--toggle-check-icon').click();
    cy.get('input[placeholder="Choose a path"]').clear().type('#');
    cy.getByTestId('Input_Openlinkinanewtab--toggle-cross-icon').click();

    cy.percySnapshotDialog('Image dialog');

    cy.getByTestId(testIds.dialogSubmitButton).click();
    cy.wait('@saveProperties');

    cy.request(
      '/content/howlite-test/pages/Image/jcr:content/rootcontainer/maincontainer/pagesection/image_1.json'
    )
      .its('body')
      .should('deep.eq', {
        mdOffset: '1',
        smOffset: '1',
        alt: 'Image of a logo',
        'sling:resourceType': 'howlite/components/image',
        url: '#',
        lgOffset: '2',
        smColsize: '10',
        mdColSize: '10',
        'jcr:primaryType': 'nt:unstructured',
        lgColSize: '8',
        showLink: 'true',
        openInNewTab: 'true'
      });
  });
});
