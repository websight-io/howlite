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

import { debounce, throttle } from '../../js/helpers/utils';
import { breakpoints } from '../../js/constants/breakpoints';

const stateClasses = {
  MOBILE_MENU_OPENED: 'mobile-menu-opened',
  SCROLLED: 'scrolled',
};

export const initNavbarEvents = () => {
  document.addEventListener(
    'DOMContentLoaded',
    () => {
      const navBarClass = 'hl-header';
      const navBar = document.querySelector(`.${navBarClass}`);

      if (!navBar) {
        return;
      }

      const hamburger = navBar.querySelector(`.${navBarClass}__hamburger`);

      let mobileMenuOpened = false;

      const openHamburgerMenu = () => {
        navBar.classList.add(stateClasses.MOBILE_MENU_OPENED);
        document.body.style.overflow = 'hidden';
        mobileMenuOpened = true;
      };

      const closeHamburgerMenu = () => {
        navBar.classList.remove(stateClasses.MOBILE_MENU_OPENED);
        document.body.style.overflow = 'unset';
        mobileMenuOpened = false;
      };

      hamburger.addEventListener('click', () => {
        if (!mobileMenuOpened) {
          openHamburgerMenu();
        } else {
          closeHamburgerMenu();
        }
      });

      hamburger.addEventListener('keyup', (event) => {
        if (event.key === 'Enter') {
          event.preventDefault();

          if (!mobileMenuOpened) {
            openHamburgerMenu();
          } else {
            closeHamburgerMenu();
          }
        }
      });

      window.addEventListener(
        'resize',
        debounce(() => {
          // eslint-disable-next-line no-restricted-globals
          if (innerWidth >= breakpoints.lg && mobileMenuOpened) {
            closeHamburgerMenu();
          }
        }),
      );

      const navbarSpecialLink = document.querySelector(
        '.hl-cta',
      );
      const navbarMarginTop = 24;

      const onPageScrolled = () => {
        // eslint-disable-next-line no-restricted-globals
        if (scrollY >= navbarMarginTop) {
          navbarSpecialLink.classList.add(stateClasses.SCROLLED);
        } else {
          navbarSpecialLink.classList.remove(stateClasses.SCROLLED);
        }
      };

      onPageScrolled();
      window.addEventListener('scroll', throttle(onPageScrolled));
    },
    { once: true },
  );
};

initNavbarEvents();
