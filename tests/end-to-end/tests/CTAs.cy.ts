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
  ctasList: 'ComponentOverlay_/content/howlite-test/pages/CTAs-list-and-CTA/jcr:content/rootcontainer/maincontainer/pagesection/ctaslist',
  cta: 'ComponentOverlay_/content/howlite-test/pages/CTAs-list-and-CTA/jcr:content/rootcontainer/maincontainer/pagesection/ctaslist/cta1'
};

const pathpickerInput = '[data-testid="ModalDialog_CTA"] input[placeholder="Type / to choose a path or enter a value"';

describe('CTA components and CTA list component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/howlite-test/pages/CTAs-list-and-CTA.html');
    cy.percySnapshotPreview('CTA preview');
  });

  it('renders correctly in edit mode and saves a text, path and checkbox property', function () {
    cy.intercept(
      'GET',
      '**CTAs-list-and-CTA.websight-pages-editor-service.get-page-data.action'
    ).as('contentRendered');

    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/CTAs-list-and-CTA::editor'
    );

    cy.getByTestId(paths.ctasList)
      .click({ force: true })
      .find(selectors.overlayName)
      .should('contain.text', 'CTAs List');

    cy.getByTestId(paths.cta)
      .click()
      .find(selectors.overlayName)
      .should('contain.text', 'CTA');

    cy.percySnapshotPageEditor('CTA editor');

    cy.getByTestId(testIds.editIcon).click();

    cy.getByTestId('ModalDialog_CTA')
      .findByTestId('Input_Text').clear().type('new value');

    cy.get(pathpickerInput)
      .clear()
      .type('/')
      .get(selectors.autosuggestionsBox)
      .find('div')
      .contains('content')
      .click()
      .get(selectors.autosuggestionsBox)
      .find('div')
      .contains('howlite-test')
      .click()
      .get(selectors.autosuggestionsBox)
      .find('div')
      .contains('pages')
      .click()
      .get(selectors.autosuggestionsBox)
      .find('div')
      .contains('Home')
      .click();

    cy.get(pathpickerInput)
      .should('have.value', '/content/howlite-test/pages/Home/')
      .blur();

    cy.getByTestId('ModalDialog_CTA')
      .findByTestId('Input_Openinnewtab').click({ force: true });

    cy.percySnapshotDialog('CTA dialog');

    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@contentRendered');
    cy.wait(1000);

    cy.getPageIframe()
      .find('.hl-cta.hl-cta--text')
      .should('have.attr', 'href', '/content/howlite-test/pages/Home.html')
      .should('have.attr', 'target', '_blank')
      .find('span')
      .should('have.text', 'new value');
  });
});
