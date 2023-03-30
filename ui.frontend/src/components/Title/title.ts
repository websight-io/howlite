/*
 * Copyright (C) 2023 Dynamic Solutions
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

import {
    getCoveringElement,
    getAnchorTargetElement,
    getWindowTopScrollPositionWithVisibleTarget
} from '../../js/helpers/dom.element';

export class TitleComponent {
    static readonly SELECTOR = 'header.hl-title';
    static readonly DO_OFFSET_IF_COVERED_BY_TAGS = ['NAV', 'HEADER'];

    public readonly componentElement: HTMLElement;

    constructor (element) {
        this.componentElement = element;

        this.scrollToIfCovered();
    }

    /**
     * Decorate scrolling to URL anchor target by offsetting position of the header menu items.
     *
     * @example localhost/home.html#test - if id="test" element is covered then scroll it to view
     *
     * @see TitleComponent.DO_OFFSET_IF_COVERED_BY_TAGS for list of elements considered a "header menu"
     *
     * @todo add automated tests to future-proof the solution (DS WOS-166)
     */
    private async scrollToIfCovered () {
        const anchorTarget = await getAnchorTargetElement();

        // empty (no target)
        if (!anchorTarget) {
            return;
        }

        // target points to another Title Component (not this one)
        if (anchorTarget.parentElement !== this.componentElement) {
            return;
        }

        const coveredBy = await getCoveringElement(anchorTarget, TitleComponent.DO_OFFSET_IF_COVERED_BY_TAGS);

        // not covered, no need to do anything
        if (!coveredBy) {
            return;
        }

        const newPosition = getWindowTopScrollPositionWithVisibleTarget(coveredBy, anchorTarget, 30);

        window.scrollTo(newPosition);
    }
}

const initTitleComponents = () => {
    document.addEventListener( 'DOMContentLoaded', () => {
        Array.from(document.querySelectorAll(TitleComponent.SELECTOR)).forEach(component => {
            new TitleComponent(component);
        });
    });
};

initTitleComponents();
