package com.polovyi.ivan.tutorials;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
class CustomerControllerRequestPhonesTestV2 extends ControllerTest {

    @Test
    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateCustomerGivenInvalidPhone(
            Set<String> phones) throws Exception {
        log.info("phones = <{}>", phones);
        givenCreateCustomerRequestWithAddress(phones);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @MethodSource("invalidPhones")
    public void shouldNotCreateCustomerGivenInvalidPhoneV2(
            Set<String> phones) throws Exception {
        log.info("phones = <{}>", phones);
        givenCreateCustomerRequestWithAddress(phones);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    /*
     * GIVEN Method
     */

    private void givenCreateCustomerRequestWithAddress(Set<String> phones) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPhoneNumbers(phones);
    }

    private static Stream<Set<String>> invalidPhones() {
        return Stream.of(null, Collections.emptySet());
    }

}
