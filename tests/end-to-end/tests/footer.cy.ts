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

import {selectors, testIds} from '../support/consts';

const paths = {
  footer: 'ComponentOverlay_/content/howlite-test/pages/Footer/jcr:content/rootcontainer/footer'
};

describe('Footer component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in edit mode', function () {
    cy.visit(
        '/apps/websight/index.html/content/howlite-test/pages/Footer::editor'
    );

    cy.intercept(
        'GET',
        '**Footer.websight-pages-editor-service.get-page-data.action'
    ).as('contentRendered');

    cy.getByTestId(paths.footer)
    .click()
    .find(selectors.overlayName)
    .should('contain.text', 'Footer');

    cy.getByTestId(testIds.editIcon).click({force: true});

    cy.getByTestId('dialogTab_Sociallinks').click();
    cy.getByTestId('ModalDialog_Footer')
      .findByTestId('Button_Multifield_Add').click();
    cy.getByTestId('ModalDialog_Footer')
      .findByTestId('Input_socialLinks/socialLinkItems/0/title').clear().type('Facebook');
    cy.getByTestId('ModalDialog_Footer')
      .findByTestId('Input_socialLinks/socialLinkItems/0/url').clear().type('fb.com');
    cy.getByTestId('ModalDialog_Footer')
      .findByTestId('Input_socialLinks/socialLinkItems/0/nofollow--checkbox-label').click();

    cy.getByTestId(testIds.dialogSubmitButton).click();
    cy.wait('@contentRendered');

    cy.getPageIframe()
      .find('.hl-social-links__link')
        .should('have.attr', 'href', 'http://fb.com')
        .should('have.attr', 'aria-label', 'Facebook')
        .should('have.attr', 'rel')
          .and('equal', 'nofollow');


    cy.request(
        '/content/howlite-test/pages/Footer/jcr:content/rootcontainer/footer/socialLinks/socialLinkItems/0.json'
    )
    .its('body')
    .should('deep.eq', {
      'title': 'Facebook',
      'icon': 'icon-linkedin',
      'url': 'fb.com',
      'nofollow': 'true',
      'jcr:primaryType': 'nt:unstructured'
    });
  });
});
