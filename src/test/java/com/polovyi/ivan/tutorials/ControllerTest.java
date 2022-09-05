package com.polovyi.ivan.tutorials;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ControllerTest {

    static List<String> blankStrings() {
        return Arrays.asList(null, "", " ");
    }

    static List<String> addressRange() {
        return Arrays.asList(null, RandomStringUtils.randomAlphabetic(9), RandomStringUtils.randomAlphabetic(101));
    }

    static Stream<Set<String>> invalidPhones() {
        return Stream.of(null, Collections.emptySet());
    }

    static Stream<Arguments> invalidPasswordAndPasswordConfirmationCombination() {
        return Stream.of(
                arguments(null, null),
                arguments(null, "password"),
                arguments("password", null),
                arguments("password", "_password"),
                arguments("_password", "password"),
                arguments(StringUtils.EMPTY, StringUtils.EMPTY),
                arguments(StringUtils.EMPTY, "password"),
                arguments("password", StringUtils.EMPTY),
                arguments(StringUtils.SPACE, StringUtils.SPACE),
                arguments(StringUtils.SPACE, "password"),
                arguments("password", StringUtils.SPACE),
                arguments(StringUtils.EMPTY, StringUtils.SPACE)
        );
    }

}
