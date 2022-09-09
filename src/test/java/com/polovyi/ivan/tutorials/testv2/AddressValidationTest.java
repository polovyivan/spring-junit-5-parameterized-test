package com.polovyi.ivan.tutorials.testv2;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class AddressValidationTest extends ControllerTest {

    @ParameterizedTest
    @MethodSource("invalidAddressVariation")
    public void shouldNotCreateCustomerGivenInvalidAddress(
            String address) throws Exception {
        log.info("address = <{}>", address);
        givenCreateCustomerRequestWithAddress(address);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    /*
     * GIVEN Method
     */

    private void givenCreateCustomerRequestWithAddress(String address) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setAddress(address);
    }

    private static List<String> invalidAddressVariation() {
        return Arrays.asList(null, RandomStringUtils.randomAlphabetic(9), RandomStringUtils.randomAlphabetic(101));
    }

}
