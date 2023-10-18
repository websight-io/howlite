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
    'ComponentOverlay_/content/howlite-test/pages/Image/jcr:content/rootcontainer/maincontainer/pagesection/image_1'
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

    cy.wait('@assetLoaded', { requestTimeout: 10000, responseTimeout: 10000 });
    cy.percySnapshotPageEditor('Image editor');

    cy.getByTestId(paths.imagePlaceholder)
      .click()
      .find(selectors.overlayName)
      .should('contain.text', 'Image');

    cy.getByTestId(testIds.editIcon).click();

    cy.intercept('GET', '**/content/howlite-test/assets/landscape.jpg.renditions/thumbnail-200x200.webp/**').as(
    'assetItemLoaded'
    );

    cy.getByTestId('SidebarElement_Assets').click();
    cy.wait('@assetItemLoaded', { requestTimeout: 10000, responseTimeout: 10000 });

    cy.getByTestId('AssetItem_landscape_jpg').dragByTestId(
        'ModalDialog_Image',
      'Input_Chooseimage-Lbreakpoint',
      { forceDrop: true }
    );

    cy.getByTestId('AssetItem_portrait_jpg').dragByTestId(
        'ModalDialog_Image',
      'Input_Chooseimage-Mbreakpoint',
      { forceDrop: true }
    );
    cy.getByTestId('AssetItem_landscape_jpg').dragByTestId(
        'ModalDialog_Image',
      'Input_Chooseimage-Sbreakpoint',
      { forceDrop: true }
    );

    cy.getByTestId('ModalDialog_Image')
      .findByTestId('Input_Alttext').clear().type('Image of a logo');
    cy.getByTestId('ModalDialog_Image')
      .findByTestId('Input_Link--toggle-check-icon').click();
    cy.getByTestId('ModalDialog_Image')
      .find('input[placeholder="Type / to choose a path or enter a value"]').clear().type('#');
    cy.getByTestId('ModalDialog_Image')
      .findByTestId('Input_Openlinkinanewtab--toggle-cross-icon').click();

    cy.percySnapshotDialog('Image dialog');

    cy.checkGridProperties();

    cy.getByTestId(testIds.dialogSubmitButton).click();
    cy.wait('@saveProperties');

    cy.request(
      '/content/howlite-test/pages/Image/jcr:content/rootcontainer/maincontainer/pagesection/image_1.json'
    )
      .its('body')
      .should('deep.eq', {
        mdOffset: '',
        smColSize: '8',
        lgImageSrc: '/content/howlite-test/assets/landscape.jpg',
        smOffset: '2',
        alt: 'Image of a logo',
        'sling:resourceType': 'howlite/components/image',
        url: '#',
        lgOffset: '1',
        smColsize: '10',
        mdColSize: '12',
        'jcr:primaryType': 'nt:unstructured',
        lgColSize: '11',
        showLink: 'true',
        smImageSrc: '/content/howlite-test/assets/landscape.jpg',
        openInNewTab: 'true',
        mdImageSrc: '/content/howlite-test/assets/portrait.jpg'
      });
  });
});
