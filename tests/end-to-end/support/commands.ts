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

import '@4tw/cypress-drag-drop';

/**
 * Adds support for '/' in testId
 */
const prepareTestId = (testId: string) =>
  testId.replaceAll('/', '\\/').replaceAll('.', '\\.');

Cypress.Commands.add('getByTestId', (testId) => {
  return cy.get(`[data-testid=${prepareTestId(testId)}]`);
});

Cypress.Commands.add('getPageIframe', () => {
  return cy
    .get(`.page-editor iframe`)
    .its('0.contentDocument')
    .should('exist')
    .its('body')
    .should('not.be.undefined')
    .then(cy.wrap);
});

Cypress.Commands.add(
  'findByTestId',
  {
    prevSubject: true
  },
  (subject, testId) => {
    return subject.find(`[data-testid=${prepareTestId(testId)}]`);
  }
);

Cypress.Commands.add(
  'dragByTestId',
  {
    prevSubject: 'element'
  },
  (subject, targetTestId, options) => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore

    cy.wrap(subject).drag(`[data-testid=${prepareTestId(targetTestId)}]`, {
      target: {
        force: true,
        position: 'top'
      }
    });

    if (options?.forceDrop) {
      cy.getByTestId(targetTestId)
        .trigger('drop', {
          eventConstructor: 'DragEvent',
          force: true
        })
        .then(() => {
          cy.getByTestId(targetTestId).trigger('mouseup', {
            which: 1,
            button: 0,
            eventConstructor: 'MouseEvent',
            force: true
          });
        });
    }
  }
);

Cypress.Commands.add('listByTestIdPrefix', (testIdPrefix) => {
  return cy.get(`[data-testid^=${prepareTestId(testIdPrefix)}_]`);
});

Cypress.Commands.add('checkGridProperties', () => {
  cy.get('[data-testid^="ModalDialog_"][role="dialog"]')
    .find('div[role="tab"]')
    .contains('Layout')
    .click();

  cy.get('.Input_Offset-Sbreakpoint__control').click();
  cy.get('.Input_Offset-Sbreakpoint__option')
    .contains(/^2 Columns$/)
    .click({ force: true });
  cy.get('.Input_Offset-Mbreakpoint__control').click();
  cy.get('.Input_Offset-Mbreakpoint__option')
    .contains('No offset')
    .click({ force: true });
  cy.get('.Input_Offset-Lbreakpoint__control').click();
  cy.get('.Input_Offset-Lbreakpoint__option')
    .contains(/^1 Column$/)
    .click({ force: true });

  cy.get('.Input_Width-Sbreakpoint__control').click();
  cy.get('.Input_Width-Sbreakpoint__option')
    .contains('8 Columns')
    .click({ force: true });
  cy.get('.Input_Width-Mbreakpoint__control').click();
  cy.get('.Input_Width-Mbreakpoint__option')
    .contains('12 Columns')
    .click({ force: true });
  cy.get('.Input_Width-Lbreakpoint__control').click();
  cy.get('.Input_Width-Lbreakpoint__option')
    .contains('11 Columns')
    .click({ force: true });
});

Cypress.Commands.add('login', () => {
  const authUrl = `${
    Cypress.env('baseUrl') || ''
  }/apps/websight-authentication/j_security_check`;
  const spacesUrl = '/websight/index.html/content::spaces';

  cy.session('admin', () => {
    cy.request({
      method: 'POST',
      url: authUrl,
      form: true,
      body: {
        j_username: Cypress.env('loginUsername'),
        j_password: Cypress.env('loginPassword'),
        resource: `/apps${spacesUrl}`,
        _charset_: 'UTF-8'
      }
    });
  });
});

const hideHowliteHeaderFooterCSS = `
  .hl-header {
    visibility: hidden !important;
  }
  .hl-footer {
    visibility: hidden !important;
  }
`;

const hideWSNavbarAndSidepanelCSS = `
  [data-ds--page-layout--slot="top-navigation"] {
    visibility: hidden !important;
  }

  [data-ds--page-layout--slot="left-sidebar"] {
    visibility: hidden !important;
  }
`;

Cypress.Commands.add('percySnapshotWithAuth', (name: string, options) => {
  cy.getCookie('websight.auth').then((authCookie) => {
    cy.percySnapshot(name, {
      discovery: {
        requestHeaders: {
          cookie: `${authCookie.name}=${authCookie.value}`
        }
      },
      ...options
    });
  });
});

Cypress.Commands.add('percySnapshotPreview', (name: string, options) => {
  cy.percySnapshotWithAuth(name, {
    percyCSS: hideHowliteHeaderFooterCSS,
    widths: [375, 768, 970],
    ...options
  });
});

Cypress.Commands.add('percySnapshotPageEditor', (name: string, options) => {
  cy.percySnapshotWithAuth(name, {
    percyCSS: hideWSNavbarAndSidepanelCSS,
    ...options
  });
});

Cypress.Commands.add('percySnapshotDialog', (name: string, options) => {
  cy.percySnapshotWithAuth(name, {
    scope: '[data-testid^="ModalDialog_"][role="dialog"]',
    ...options
  });
});
