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
const prepareTestId = (testId: string) => testId.replaceAll('/', '\\/');

Cypress.Commands.add('getByTestId', (testId) => {
  return cy.get(`[data-testid=${prepareTestId(testId)}]`);
});

Cypress.Commands.add('getPageIframe', () => {
  return cy.get(`iframe`)
  .its('0.contentDocument').should('exist')
  .its('body').should('not.be.undefined').then(cy.wrap);
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
  (subject, targetTestId) => {
    cy.wrap(subject).drag(`[data-testid=${prepareTestId(targetTestId)}]`);
  }
);

Cypress.Commands.add('listByTestIdPrefix', (testIdPrefix) => {
  return cy.get(`[data-testid^=${prepareTestId(testIdPrefix)}_]`);
});

Cypress.Commands.add(
  'saveDataAttrAsNumber',
  {
    prevSubject: 'element'
  },
  (subject, attrName: string, alias: string) => {
    cy.wrap(subject).invoke('attr', attrName).then(parseInt).as(alias);
  }
);

Cypress.Commands.add(
  'shouldAttrAsNumber',
  {
    prevSubject: 'element'
  },
  (subject, attrName: string, chainer: string, alias: string) => {
    cy.get(`@${alias}`).then((valueToCompareWith) => {
      cy.wrap(subject)
        .invoke('attr', attrName)
        .then(parseInt)
        .should(chainer, valueToCompareWith);
    });
  }
);

Cypress.Commands.add('login', () => {
  const authUrl = `${
    Cypress.env('baseUrl') || ''
  }/apps/websight-authentication/j_security_check`;
  const spacesUrl = '/websight/index.html/content::spaces';

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

Cypress.Commands.add('submitLoginFormWithAdmin', () => {
  cy.getByTestId('login-form-username').type(Cypress.env('loginUsername'));
  cy.getByTestId('login-form-password').type(Cypress.env('loginPassword'));
  cy.getByTestId('login-form-submit').click();
});

Cypress.Commands.add('percySnapshotNoHeaderFooter', (name: string, options) =>
  cy.percySnapshot(name, {
    percyCSS: `
      div[data-testid="Footer_WebSight"] {
        visibility: hidden !important;
      }
      header {
        visibility: hidden !important;
      }
      `,
    ...options
  })
);
