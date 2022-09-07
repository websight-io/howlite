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
  private static final String GRID_COLS_PREFIX = "hl-cols";
  private static final String GRID_OFFSET_PREFIX = "hl-offset";

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

    classes.addAll(getGridDisplayClasses());
    classes.addAll(getGridColSizeClasses());
    classes.addAll(getGridOffsetClasses());

    return classes;
  }

  private Collection<String> getGridDisplayClasses() {
    List<String> classes = new LinkedList<>();
    if (displayType == null) {
      classes.add(GRID_CLASS_PREFIX + "-component");
    } else if (GridDisplayType.GRID.equals(displayType)) {
      classes.add(GRID_CLASS_PREFIX);
    }
    return classes;
  }

  private Collection<String> getGridColSizeClasses() {
    if (!hasAnyColSize()) {
      //we do not set classes when there is no setting for it
      return Collections.emptyList();
    } else if (hasSameColSizes()) {
      return prepareDefaultGridColSizeClass();
    } else {
      return prepareGridColSizeClasses();
    }
  }

  private Collection<String> prepareDefaultGridColSizeClass() {
    return Collections.singleton(GRID_COLS_PREFIX + "-" + getDefaultColSize());
  }

  private Collection<String> prepareGridColSizeClasses() {
    Set<String> classes = new LinkedHashSet<>();
    if (gridComponent.getSmColSize() != null) {
      classes.add(GRID_COLS_PREFIX + SM_CLASS_PART + "-" + gridComponent.getSmColSize());
    }

    if (gridComponent.getMdColSize() != null) {
      classes.add(GRID_COLS_PREFIX + MD_CLASS_PART + "-" + gridComponent.getMdColSize());
    }

    if (gridComponent.getLgColSize() != null) {
      classes.add(GRID_COLS_PREFIX + LG_CLASS_PART + "-" + gridComponent.getLgColSize());
    }
    return classes;
  }

  private Collection<String> getGridOffsetClasses() {
    if (!hasAnyOffset()) {
      return Collections.emptyList();
    } else if (hasSameOffset()) {
      return prepareDefaultGridOffsetClass();
    } else {
      return prepareGridOffsetClasses();
    }
  }

  private Set<String> prepareDefaultGridOffsetClass() {
    return Collections.singleton(GRID_OFFSET_PREFIX + "-" + getDefaultOffset());
  }

  private Collection<String> prepareGridOffsetClasses() {
    Set<String> classes = new LinkedHashSet<>();
    if (gridComponent.getSmOffset() != null) {
      classes.add(GRID_OFFSET_PREFIX + SM_CLASS_PART + "-" + gridComponent.getSmOffset());
    }

    if (gridComponent.getMdOffset() != null) {
      classes.add(GRID_OFFSET_PREFIX + MD_CLASS_PART + "-" + gridComponent.getMdOffset());
    }

    if (gridComponent.getLgOffset() != null) {
      classes.add(GRID_OFFSET_PREFIX + LG_CLASS_PART + "-" + gridComponent.getLgOffset());
    }
    return classes;
  }

  private boolean hasAnyColSize() {
    return !getCollSizes()
        .collect(Collectors.toSet())
        .isEmpty();
  }

  private boolean hasSameColSizes() {
    Set<Integer> colSizes = getCollSizes()
        .collect(Collectors.toSet());

    return getCollSizes().count() == 3L && colSizes.size() == 1
        && colSizes.iterator().next() != null;
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

  private boolean hasAnyOffset() {
    return !getOffset()
        .collect(Collectors.toSet())
        .isEmpty();
  }

  private boolean hasSameOffset() {
    Set<Integer> offsets = getOffset()
        .collect(Collectors.toSet());

    return getOffset().count() == 3L && offsets.size() == 1 && offsets.iterator().next() != null;
  }

  private Integer getDefaultOffset() {
    Set<Integer> colSizes = getOffset().collect(Collectors.toSet());

    if (colSizes.size() == 1) {
      return colSizes.iterator().next();
    }
    return null;
  }

  private Stream<Integer> getOffset() {
    return Stream.of(gridComponent.getSmOffset(), gridComponent.getMdOffset(),
            gridComponent.getLgOffset())
        .filter(Objects::nonNull);
  }

}
