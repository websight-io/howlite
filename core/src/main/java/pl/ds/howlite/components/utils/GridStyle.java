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

package pl.ds.howlite.components.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import pl.ds.howlite.components.Grid;

public class GridStyle {

  private static final String GRID_CLASS_PREFIX = "hl-grid";
  private static final String COLS_CLASS_PART = "-cols";
  private static final String SM_CLASS_PART = "-sm";
  private static final String MD_CLASS_PART = "-md";
  private static final String LG_CLASS_PART = "-lg";

  private final Grid gridComponent;
  private final GridDisplayType displayType;

  public GridStyle(Grid gridComponent, GridDisplayType displayType) {
    this.gridComponent = gridComponent;
    this.displayType = displayType;
  }

  public GridStyle(Grid gridComponent) {
    this(gridComponent, null);
  }

  public Collection<String> getClasses() {
    List<String> classes = new LinkedList<>();
    if (displayType != null) {
      classes.addAll(getGridDisplayClasses());
    }
    classes.addAll(getGridColumnClasses());

    return classes;
  }

  private Collection<String> getGridDisplayClasses() {
    List<String> classes = new LinkedList<>();
    if (GridDisplayType.GRID == displayType) {
      classes.add(GRID_CLASS_PREFIX);
      classes.addAll(getGridColSizeClasses());
    } else if (GridDisplayType.INLINE == displayType) {
      classes.add(GRID_CLASS_PREFIX + "--inline");
    }
    return classes;
  }

  private Collection<String> getGridColSizeClasses() {
    Integer defaultColSize = getDefaultColSize();
    if (defaultColSize != null) {
      return prepareGridColSizeClasses(defaultColSize);
    } else {
      return prepareGridColSizeClasses();
    }
  }

  private Collection<String> prepareGridColSizeClasses(Integer defaultColSize) {
    return Collections.singleton(GRID_CLASS_PREFIX + "-" + defaultColSize);
  }

  private Collection<String> prepareGridColSizeClasses() {
    Set<String> classes = new LinkedHashSet<>();
    if (gridComponent.getSmColSize() != null) {
      classes.add(GRID_CLASS_PREFIX + SM_CLASS_PART + "-" + gridComponent.getSmColSize());
    }

    if (gridComponent.getMdColSize() != null) {
      classes.add(GRID_CLASS_PREFIX + MD_CLASS_PART + "-" + gridComponent.getMdColSize());
    }

    if (gridComponent.getLgColSize() != null) {
      classes.add(GRID_CLASS_PREFIX + LG_CLASS_PART + "-" + gridComponent.getLgColSize());
    }
    return classes;
  }

  private Collection<String> getGridColumnClasses() {
    if (hasAnyColStart()) {
      return getGridColumnClassesWithColStart();
    } else {
      return getGridColumnClassesWithoutColStart();
    }
  }

  private Collection<String> getGridColumnClassesWithColStart() {
    Integer defaultColStart = getDefaultColStart();
    if (defaultColStart != null) {
      return getGridColumnClassesWithColStart(defaultColStart);
    } else {
      return prepareGridColumnClassesWithColStart();
    }
  }

  private Collection<String> getGridColumnClassesWithColStart(Integer defaultColStart) {
    Integer defaultColSize = getDefaultColSize();
    if (defaultColSize != null) {
      return prepareGridColumnClassesWithColStart(defaultColStart, defaultColSize);
    } else {
      return prepareGridColumnClassesWithColStart(defaultColStart);
    }
  }

  private Collection<String> prepareGridColumnClassesWithColStart(Integer defaultColStart,
      Integer defaultColSize) {
    return Collections.singleton(
        GRID_CLASS_PREFIX + COLS_CLASS_PART + "-" + defaultColStart + "-" + defaultColSize);
  }

  private Collection<String> prepareGridColumnClassesWithColStart(Integer defaultColStart) {
    Set<String> classes = new LinkedHashSet<>();
    if (gridComponent.getSmColSize() != null) {
      classes.add(GRID_CLASS_PREFIX + SM_CLASS_PART + COLS_CLASS_PART + "-" + defaultColStart + "-"
          + gridComponent.getSmColSize());
    }
    if (gridComponent.getMdColSize() != null) {
      classes.add(GRID_CLASS_PREFIX + MD_CLASS_PART + COLS_CLASS_PART + "-" + defaultColStart + "-"
          + gridComponent.getMdColSize());
    }
    if (gridComponent.getLgColSize() != null) {
      classes.add(GRID_CLASS_PREFIX + LG_CLASS_PART + COLS_CLASS_PART + "-" + defaultColStart + "-"
          + gridComponent.getLgColSize());
    }
    return classes;
  }

  private Collection<String> prepareGridColumnClassesWithColStart() {
    Set<String> classes = new LinkedHashSet<>();
    if (gridComponent.getSmColSize() != null) {
      if (gridComponent.getSmColStart() != null) {
        classes.add(GRID_CLASS_PREFIX + SM_CLASS_PART + COLS_CLASS_PART + "-"
            + gridComponent.getSmColStart() + "-" + gridComponent.getSmColSize());
      } else {
        classes.add(GRID_CLASS_PREFIX + SM_CLASS_PART + COLS_CLASS_PART + "-"
            + gridComponent.getSmColSize());
      }
    }
    if (gridComponent.getMdColSize() != null) {
      if (gridComponent.getMdColStart() != null) {
        classes.add(GRID_CLASS_PREFIX + MD_CLASS_PART + COLS_CLASS_PART + "-"
            + gridComponent.getMdColStart() + "-" + gridComponent.getMdColSize());
      } else {
        classes.add(GRID_CLASS_PREFIX + MD_CLASS_PART + COLS_CLASS_PART + "-"
            + gridComponent.getMdColSize());
      }
    }
    if (gridComponent.getLgColSize() != null) {
      if (gridComponent.getLgColStart() != null) {
        classes.add(GRID_CLASS_PREFIX + LG_CLASS_PART + COLS_CLASS_PART + "-"
            + gridComponent.getLgColStart() + "-" + gridComponent.getLgColSize());
      } else {
        classes.add(
            GRID_CLASS_PREFIX + LG_CLASS_PART + COLS_CLASS_PART + gridComponent.getLgColSize());
      }
    }
    return classes;
  }

  private Collection<String> getGridColumnClassesWithoutColStart() {
    Integer defaultColSize = getDefaultColSize();

    if (defaultColSize != null) {
      return prepareGridColumnClassesWithoutColStart(defaultColSize);
    } else {
      return prepareGridColumnClassesWithoutColStart();
    }
  }

  private Collection<String> prepareGridColumnClassesWithoutColStart(Integer defaultColSize) {
    return Collections.singleton(GRID_CLASS_PREFIX + COLS_CLASS_PART + "-" + defaultColSize);
  }

  private Collection<String> prepareGridColumnClassesWithoutColStart() {
    Set<String> classes = new LinkedHashSet<>();
    if (gridComponent.getSmColSize() != null) {
      classes.add(
          GRID_CLASS_PREFIX + SM_CLASS_PART + COLS_CLASS_PART + "-" + gridComponent.getSmColSize());
    }
    if (gridComponent.getMdColSize() != null) {
      classes.add(
          GRID_CLASS_PREFIX + MD_CLASS_PART + COLS_CLASS_PART + "-" + gridComponent.getMdColSize());
    }
    if (gridComponent.getLgColSize() != null) {
      classes.add(
          GRID_CLASS_PREFIX + LG_CLASS_PART + COLS_CLASS_PART + "-" + gridComponent.getLgColSize());
    }
    return classes;
  }

  private Integer getDefaultColSize() {
    Set<Integer> colSizes = getCollSizes().collect(Collectors.toSet());

    if (colSizes.size() == 1) {
      return colSizes.iterator().next();
    }
    return null;
  }

  private Stream<Integer> getCollSizes() {
    return Stream.of(gridComponent.getSmColSize(), gridComponent.getMdColSize(),
            gridComponent.getLgColSize())
        .filter(Objects::nonNull);
  }

  private boolean hasAnyColStart() {
    return !getCollStarts()
        .collect(Collectors.toSet())
        .isEmpty();
  }

  private Integer getDefaultColStart() {
    Set<Integer> colStarts = getCollStarts().collect(Collectors.toSet());

    if (colStarts.size() == 1) {
      return colStarts.iterator().next();
    }
    return null;
  }

  private Stream<Integer> getCollStarts() {
    return Stream.of(gridComponent.getSmColStart(), gridComponent.getMdColStart(),
            gridComponent.getLgColStart())
        .filter(Objects::nonNull);
  }

}
