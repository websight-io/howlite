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
  ctasList: 'ComponentOverlay_rootcontainer/maincontainer/pagesection/ctaslist',
  cta: 'ComponentOverlay_rootcontainer/maincontainer/pagesection/ctaslist/cta1'
};

const pathpickerInput = 'input[placeholder="Choose a path"';

describe('CTA components and CTA list component', function () {
  it('renders correctly in preview mode', function () {
    cy.login();

    cy.visit('/content/howlite-test/pages/CTAs-list-and-CTA.html');

    cy.percySnapshot();
  });

  it('renders correctly in edit mode and saves a text, path and checkbox property', function () {
    cy.login();

    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/CTAs-list-and-CTA::editor'
    );

    cy.intercept(
      'GET',
      '**/CTAs-list-and-CTA/jcr:content/rootcontainer.html?wcmmode=edit'
    ).as('contentRendered');

    cy.getByTestId(paths.ctasList)
      .click()
      .find(selectors.overlayName)
      .should('have.text', 'CTAs List');

    cy.percySnapshot();
    cy.wait(1000);

    cy.getByTestId(paths.ctasList)
      .getByTestId(paths.cta)
      .click()
      .find(selectors.overlayName)
      .should('have.text', 'CTA');

    cy.getByTestId(testIds.editIcon).click();
    cy.getByTestId('Input_Text').type(' changed');

    cy.get(pathpickerInput)
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

    cy.getByTestId('Input_Openinnewtab').click();
    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@contentRendered');
    cy.wait(1000);

    cy.getPageIframe()
      .find('.hl-cta.hl-cta--text')
      .should('contain', ' changed')
      .should('have.attr', 'href', '/content/howlite-test/pages/Home.html')
      .should('have.attr', 'target', '_blank');
  });
});
