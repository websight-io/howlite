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

describe('CTA components and CTA list component', function () {
  it('renders correctly in preview mode', function () {
    cy.login();

    cy.visit('/content/howlite-test/pages/CTAs-list-and-CTA.html');

    cy.percySnapshot();
  });

  it('renders correctly in edit mode and saves a text property', function () {
    cy.login();

    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/CTAs-list-and-CTA::editor'
    );

    cy.intercept(
      'GET',
      '**/CTAs-list-and-CTA/jcr:content/rootcontainer.html?wcmmode=edit'
    ).as('contentRendered');

    cy.getByTestId('ComponentOverlay_CTAsList')
      .click()
      .get('.name')
      .should('be.visible');

    cy.percySnapshot();

    cy.getByTestId('ComponentOverlay_CTAsList')
      .getByTestId('ComponentOverlay_CTA')
      .last()
      .click()
      .get('.name')
      .should('be.visible');

    cy.getByTestId('ToolbarOption_Edit').click();
    cy.getByTestId('Input_Text').type(' changed');
    cy.getByTestId('Input_Openinnewtab').click();
    cy.getByTestId('Input_Showicon').click();
    cy.getByTestId('Button_Submit').click();

    cy.wait('@contentRendered');
    cy.getPageIframe().find('.hl-cta').last().should('contain', ' changed');
  });
});
