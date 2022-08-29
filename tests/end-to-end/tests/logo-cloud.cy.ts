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
  logoCloud:
    'ComponentOverlay_rootcontainer/maincontainer/pagesection/logoscloud'
};

describe('Logo cloud component', function () {
  beforeEach(() => {
    cy.login();
  });

  it('renders correctly in preview mode', function () {
    cy.visit('/content/howlite-test/pages/Logo-cloud.html');
    cy.percySnapshotPreview('Logo cloud preview');
  });

  it('renders correctly in edit mode', function () {
    cy.visit(
      '/apps/websight/index.html/content/howlite-test/pages/Logo-cloud::editor'
    );

    cy.percySnapshotPageEditor('Logo cloud editor');

    cy.intercept(
      'POST',
      '**/pagesection/logoscloud.websight-dialogs-service.save-properties.action'
    ).as('saveProperties');

    cy.getByTestId(paths.logoCloud)
      .click({force: true})
      .find(selectors.overlayName)
      .should('have.text', 'Logo cloud');

    cy.getByTestId(testIds.editIcon).click();
    cy.percySnapshotDialog('Logo cloud dialog');

    //TODO: modify width and offset values for component, once data-testid attributes are available in WebSight
    cy.getByTestId(testIds.dialogSubmitButton).click();

    cy.wait('@saveProperties');

    cy.request(
      '/content/howlite-test/pages/Logo-cloud/jcr:content/rootcontainer/maincontainer/pagesection/logoscloud.json'
    )
      .its('body')
      .should('deep.eq', {
        'sling:resourceType': 'howlite/components/logoscloud',
        'jcr:primaryType': 'nt:unstructured',
      });
  });
});
