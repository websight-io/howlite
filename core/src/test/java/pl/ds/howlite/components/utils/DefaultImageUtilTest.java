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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DefaultImageUtilTest {

    @ParameterizedTest
    @MethodSource(value = "sources")
    void findLastNotEmptyImage(String defaultImage, String[] sources) {

        //when:
        String chosenImage = DefaultImageUtil.chooseDefaultImage(sources);

        //then:
        assertThat(chosenImage).isEqualTo(defaultImage);
    }

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of("third", new String[]{"first", "second", "third"}),
                Arguments.of("second", new String[]{"first", "second", ""}),
                Arguments.of("second", new String[]{"first", "second", null}),
                Arguments.of("second", new String[]{"first", "second"}),
                Arguments.of("first", new String[]{"first", "", ""}),
                Arguments.of("first", new String[]{"first"})
        );
    }


}