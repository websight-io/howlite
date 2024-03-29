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
  cardsList:
    'ComponentOverlay_/content/howlite-test/pages/Cards/jcr:content/rootcontainer/maincontainer/pagesection/cardslist',
  cardItem:
    'ComponentOverlay_/content/howlite-test/pages/Cards/jcr:content/rootcontainer/maincontainer/pagesection/cardslist/card1'
};

describe('Cards lists and card item component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('render correctly in preview mode', function () {
    cy.visit('/content/howlite-test/pages/Cards.html');

    cy.percySnapshotPreview('Cards preview');

    cy.get('section:nth-of-type(2) div').should('have.class', 'glide--slider');
    cy.contains('Third card item').move({ deltaX: 1500, deltaY: 0 });
    cy.contains('Last card item').should('be.visible');

    cy.get('section:last-of-type ul').should('have.class', 'hl-grid-lg-2');
  });

  it('Card List render correctly in edit mode', function () {
    cy.visit(
        '/apps/websight/index.html/content/howlite-test/pages/Cards::editor'
    );

    testCardsList();
  });

  it('Card Item render correctly in edit mode', function () {
    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Cards::editor'
    );

    testCardItem();
  });

  function testCardsList() {
    cy.intercept(
      'POST',
      '**/pagesection/cardslist.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.getByTestId(paths.cardsList)
      .click({ force: true })
      .find(selectors.overlayName)
      .should('contain.text', 'Cards List');

    cy.percySnapshotPageEditor('Cards list editor');

    cy.getByTestId(testIds.editIcon).click();

    cy.getByTestId('ModalDialog_CardsList')
      .findByTestId('Input_Displayasaslider--checkbox-label').click();
    cy.getByTestId('ModalDialog_CardsList')
      .findByTestId('RadioElement_h3').click();
    cy.getByTestId('ModalDialog_CardsList')
      .findByTestId('RadioElement_hl-title__heading--size-3').click();
    cy.getByTestId('ModalDialog_CardsList')
      .findByTestId('Input_Itemsperrow-Sbreakpoint').clear().type('1');
    cy.getByTestId('ModalDialog_CardsList')
      .findByTestId('Input_Itemsperrow-Mbreakpoint').clear().type('2');
    cy.getByTestId('ModalDialog_CardsList')
      .findByTestId('Input_Itemsperrow-Lbreakpoint').clear().type('3');

    cy.getByTestId('ModalDialog_CardsList')
      .findByTestId('dialogTab_Style').click();
    cy.getByTestId('ModalDialog_CardsList')
      .find('div[id^="classes-uid"]').click();
    cy.contains('Numbered').click({ force: true });

    cy.percySnapshotDialog('Cards list dialog');

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
      '/content/howlite-test/pages/Cards/jcr:content/rootcontainer/maincontainer/pagesection/cardslist.json'
    )
      .its('body')
      .should('deep.eq', {
        'jcr:primaryType': 'nt:unstructured',
        itemsPerRowLg: '3',
        itemsPerRowMd: '2',
        itemsPerRowSm: '1',
        isSlider: 'true',
        headingSize: 'hl-title__heading--size-3',
        classes: 'hl-cards-list--numbered',
        'sling:resourceType': 'howlite/components/cardslist',
        headingLevel: 'h3',
      });
  }

  function testCardItem() {
    cy.intercept(
      'POST',
      '**/pagesection/cardslist/card1.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.getByTestId(paths.cardItem)
      .click({ force: true })
      .find(selectors.overlayName)
      .should('contain.text', 'Card Item');

    cy.percySnapshotPageEditor('Card item editor');

    cy.getByTestId(testIds.editIcon).click();

    cy.getByTestId('ModalDialog_CardItem')
      .findByTestId('Input_Title').clear().type('Card item title');
    cy.getByTestId('ModalDialog_CardItem')
      .find('.ProseMirror').clear().type('Sample text');

    cy.getByTestId('ModalDialog_CardItem')
      .findByTestId('dialogTab_Image').click();
    cy.getByTestId('ModalDialog_CardItem')
      .findByTestId('Input_Alttext').clear().type('Image of the card item');

    cy.getByTestId('ModalDialog_CardItem')
      .findByTestId('dialogTab_CTA').click();
    cy.getByTestId('ModalDialog_CardItem')
      .findByTestId('Input_Displayname').clear().type('Link title');

    cy.getByTestId('ModalDialog_CardItem')
      .find('input[placeholder="Type / to choose a path or enter a value"]').clear().type('#');
    cy.getByTestId('ModalDialog_CardItem')
      .findByTestId('Input_Openlinkinanewtab--checkbox-label').click();
    cy.getByTestId('ModalDialog_CardItem')
      .findByTestId('Input_Showicon--checkbox-label').click();

    cy.contains('Text Link').click({ force: true });
    cy.contains('Small Button').click({ force: true });

    cy.percySnapshotDialog('Card item dialog');

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
      '/content/howlite-test/pages/Cards/jcr:content/rootcontainer/maincontainer/pagesection/cardslist/card1.json'
    )
      .its('body')
      .should('deep.eq', {
        'sling:resourceType': 'howlite/components/cardslist/card',
        'jcr:primaryType': 'nt:unstructured',
        ctaClasses: 'hl-cta--small',
        alt: 'Image of the card item',
        title: 'Card item title',
        ctaLabel: 'Link title',
        ctaDisplayIcon: 'true',
        content: '<p>Sample text</p>',
        ctaOpenInNewTab: 'true',
        ctaLink: '#'
      });
  }
});
