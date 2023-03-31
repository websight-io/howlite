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

export type Target = Element | null;

/**
 * @returns HTML Element or its parent (whitelisted by 'tagSelectors') that is covering given element (if found)
 *
 * @param element to check is it covered by a tag
 * @param tagSelectors those or their parents that are considered to possibly cover the element
 *
 * @example isElementOrParentOfTag(document.querySelector('NAV'), ['HEADER', 'NAV'])
 */
export const isElementOrParentOfTag = (element: Element, tagSelectors: string[]): Element | false => {
    let result: Element;

    tagSelectors.some(nodeName => {
        // it is the given HTML tag
        if (element.nodeName === nodeName) {
            result = element;
            return true;
        }

        // it is the closest parent element with given HTML tag
        const closest = element.closest(nodeName);

        if (closest) {
            result = closest;
            return true;
        }
    });

    return result || false;
};

/**
 * @returns element that is covering the given element with 'tagSelectors'
 *
 * @param element to check is it covered by a tag
 * @param tagSelectors those or their parents that are considered to possibly cover the element
 *
 * @example getCoveringElement(document.querySelector('NAV'), ['HEADER', 'NAV'])
 */
export const getCoveringElement = (element: Element | null, tagSelectors: string[]): Element | false => {
    // to allow direct document.querySelector... as 'element' thus it can be null
    if (!element) {
        return false;
    }

    const { x, y } = element.getBoundingClientRect();

    const elementFromPoint = document.elementFromPoint(x, y);

    // elementFromPoint not visible (empty)
    if (!elementFromPoint) {
        return false;
    }

    // elementFromPoint is the target - so it's not covered
    if (element === elementFromPoint) {
        return false;
    }

    // elementFromPoint is covering the Title by occupying its space?
    return isElementOrParentOfTag(elementFromPoint, tagSelectors);
};


/**
 * @see memory for 'getTargetElement'
 */
let targetElementMemory: undefined | null | Element;

/**
 * @returns Memoized. Unique target element with an id matching the URL's fragment (# anchor)
 *
 * @see https://developer.mozilla.org/en-US/docs/Web/CSS/:target
 */
export const getAnchorTargetElement = (): Promise<Target> => {
    if (typeof targetElementMemory !== 'undefined') {
        return Promise.resolve(targetElementMemory);
    }

    return new Promise<Target>((resolve) => {
        window.addEventListener('load', () => {
            // setTimeout used, so it resolves at next frame, when :target was already scrolled-to by browser
            setTimeout(() => {
                const result = document.querySelector(':target');
                targetElementMemory = Object.freeze(result);

                return resolve(targetElementMemory);
            });
        });
    });
};

/**
 * @returns Calculated new top position for the window scroll taking into account visibility of the given 'anchorTarget'
 *
 * @param coveredBy element that covers the 'target' element
 * @param target element that has to be visible and not covered by the 'coveredBy' element
 * @param margin optional offset for the output
 */
export const getWindowTopScrollPositionWithVisibleTarget = (
    coveredBy: Element,
    target: Element,
    margin = 0
): ScrollToOptions => {
    const { height: coveredByHeight } = coveredBy.getBoundingClientRect();
    const { top: targetTop } = target.getBoundingClientRect();

    const newTopPosition = targetTop - coveredByHeight + window.pageYOffset - margin;

    return {
        top: newTopPosition,
    };
};
