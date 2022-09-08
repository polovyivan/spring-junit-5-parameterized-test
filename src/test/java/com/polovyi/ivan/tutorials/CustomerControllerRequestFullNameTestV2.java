package com.polovyi.ivan.tutorials;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
class CustomerControllerRequestFullNameTestV2 extends ControllerTest {

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { " " })
    public void shouldNotCreateCustomerGivenBlankFullNameV1(String name) throws Exception {
        log.info("name = <{}>", name);
        givenRequestWithInvalidFullName(name);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    public void shouldNotCreateCustomerGivenBlankFullNameV2(String name) throws Exception {
        log.info("name = <{}>", name);
        givenRequestWithInvalidFullName(name);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    public void shouldNotCreateCustomerGivenBlankFullNameV3(String name) throws Exception {
        log.info("name = <{}>", name);
        givenRequestWithInvalidFullName(name);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    /*
     * GIVEN Method
     */

    private void givenRequestWithInvalidFullName(String name) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setFullName(name);
    }

    private static List<String> blankStrings() {
        return Arrays.asList(null, "", " ");
    }

}
