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
  pageSection: 'ComponentOverlay_rootcontainer/maincontainer/pagesection'
};

describe('Page Section component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/howlite-test/pages/Page-Section.html');
    cy.percySnapshotPreview('Page Section preview');
  });

  it('renders correctly in edit mode', function () {
    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Page-Section::editor'
    );

    cy.intercept(
      'POST',
      '**/maincontainer/pagesection.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.getByTestId(paths.pageSection)
      .click()
      .find(selectors.overlayName)
      .should('contain.text', 'Page Section');

    cy.percySnapshotPageEditor('Page Section editor');

    cy.getByTestId(testIds.editIcon).click();

    cy.getByTestId('SidebarElement_Assets').click();
    cy.getByTestId('AssetItem_landscape_jpg').dragByTestId(
      'Input_Backgroundimage-Lbreakpoint',
      { forceDrop: true }
    );

    cy.getByTestId('AssetItem_portrait_jpg').dragByTestId(
      'Input_Backgroundimage-Mbreakpoint',
      { forceDrop: true }
    );
    cy.getByTestId('AssetItem_landscape_jpg').dragByTestId(
      'Input_Backgroundimage-Sbreakpoint',
      { forceDrop: true }
    );

    cy.percySnapshotDialog('Page Section dialog');

    cy.getByTestId(testIds.dialogSubmitButton).click();
    cy.wait('@saveProperties');

    cy.request(
      '/content/howlite-test/pages/Page-Section/jcr:content/rootcontainer/maincontainer/pagesection.json'
    )
      .its('body')
      .should('deep.eq', {
        backgroundImageSm: '/content/howlite-test/assets/landscape.jpg',
        backgroundImageMd: '/content/howlite-test/assets/portrait.jpg',
        'sling:resourceType': 'howlite/components/pagesection',
        'jcr:primaryType': 'nt:unstructured',
        backgroundImageLg: '/content/howlite-test/assets/landscape.jpg'
      });
  });

  it('all of the components are visible in edit mode', function () {
    const expectedMainElements = [
      'Accordion',
      'CTA',
      'CTAs List',
      'Cards List',
      'Container',
      'Image',
      'Logo cloud',
      'Quote',
      'Rich text editor',
      'Title'
    ];
    const expectedRestrictedElements = ['Accordion Item', 'Card Item'];
    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Page-Section::editor'
    );

    cy.getByTestId('components-section-components').click();

    //Main Howlite components
    cy.getByTestId('ComponentSection')
      .getByTestId('ComponentDefinitionsGroup_Howlite')
      .children()
      .first()
      .should('have.text', ' Howlite');

    expectedMainElements.forEach((item) => {
      let testIdItem = item.replaceAll(' ', '');
      cy.getByTestId('ComponentSection')
        .getByTestId('ComponentDefinitionsGroup_Howlite')
        .getByTestId('Component_' + testIdItem)
        .find('.name')
        .first()
        .should('have.text', item);
    });

    // Howlite restricted components
    cy.getByTestId('ComponentSection')
      .getByTestId('ComponentDefinitionsGroup_HowliteRestricted')
      .children()
      .first()
      .should('have.text', ' Howlite Restricted');

    expectedRestrictedElements.forEach((item) => {
      let testIdItem = item.replaceAll(' ', '');
      cy.getByTestId('ComponentSection')
        .getByTestId('ComponentDefinitionsGroup_HowliteRestricted')
        .getByTestId('Component_' + testIdItem)
        .find('.name')
        .first()
        .should('have.text', item);
    });
  });
});
