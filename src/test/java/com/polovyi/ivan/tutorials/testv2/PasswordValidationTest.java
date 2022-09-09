package com.polovyi.ivan.tutorials.testv2;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
public class PasswordValidationTest extends ControllerTest {

    @ParameterizedTest
    @MethodSource("invalidPasswordAndPasswordConfirmationCombination")
    public void shouldNotCreateCustomerGivenInvalidPasswordAndPasswordCombination(String password,
            String passwordConfirmation) throws Exception {
        log.info("password = <{}>, passwordConfirmation = <{}>", password, passwordConfirmation);
        givenCreateCustomerRequestWithAddress(password, passwordConfirmation);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    /*
     * GIVEN Method
     */

    private void givenCreateCustomerRequestWithAddress(String password, String passwordConfirmation) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword(password);
        createCustomerRequest.setPasswordConfirmation(passwordConfirmation);
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
