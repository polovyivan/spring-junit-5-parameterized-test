package com.polovyi.ivan.tutorials.testv2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class BirthDateValidationTest extends ControllerTest {

    @ParameterizedTest
    @ValueSource(strings = { "1899-12-31", "2030-06-01" })
    public void shouldNotCreateCustomerGivenInvalidDate(
            @JavaTimeConversionPattern("yyyy-MM-dd") LocalDate birthDate) throws Exception {
        log.info("name = <{}>", birthDate);
        givenCreateCustomerRequestWithBirthDate(birthDate);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotCreateCustomerGivenBirthDateAsNull(LocalDate birthDate) throws Exception {
        log.info("name = <{}>", birthDate);
        givenCreateCustomerRequestWithBirthDate(birthDate);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @MethodSource("invalidDatesVariation")
    public void shouldNotCreateCustomerGivenInvalidBirthDate(LocalDate birthDate) throws Exception {
        log.info("name = <{}>", birthDate);
        givenCreateCustomerRequestWithBirthDate(birthDate);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    /*
     * GIVEN Method
     */

    private void givenCreateCustomerRequestWithBirthDate(LocalDate birthDate) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setBirthDate(birthDate);
    }

    private static List<LocalDate> invalidDatesVariation() {
        return Arrays.asList(null, LocalDate.of(1899, 12, 31), LocalDate.of(2030, 06, 01));
    }
}
