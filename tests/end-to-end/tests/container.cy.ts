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
  container: 'ComponentOverlay_/content/howlite-test/pages/Container/jcr:content/rootcontainer/maincontainer/onecol/container'
};

describe('Container component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/howlite-test/pages/Container.html');
    cy.percySnapshotPreview('Container preview');
  });

  it('renders correctly in edit mode', function () {
    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Container::editor'
    );

    cy.intercept(
      'POST',
      '**/maincontainer/onecol/container.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.getByTestId(paths.container)
      .click()
      .find(selectors.overlayName)
      .should('contain.text', 'Container');

    cy.percySnapshotPageEditor('Container editor');

    cy.getByTestId(testIds.editIcon).click();

    cy.wait(1000);
    // TODO: add test for saving background image properties

    cy.percySnapshotDialog('Container dialog');

    cy.checkGridProperties();

    cy.getByTestId(testIds.dialogSubmitButton).click();
    cy.wait('@saveProperties');

    cy.request(
      '/content/howlite-test/pages/Container/jcr:content/rootcontainer/maincontainer/onecol/container.json'
    )
      .its('body')
      .should('deep.eq', {
        mdOffset: '',
        smColSize: '8',
        smOffset: '2',
        'sling:resourceType': 'howlite/components/container',
        lgOffset: '1',
        mdColSize: '12',
        'jcr:primaryType': 'nt:unstructured',
        lgColSize: '11'
      });
  });
});
