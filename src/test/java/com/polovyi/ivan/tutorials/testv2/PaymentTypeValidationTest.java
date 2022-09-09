package com.polovyi.ivan.tutorials.testv2;

import com.polovyi.ivan.tutorials.enm.PaymentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

@Slf4j
public class PaymentTypeValidationTest extends ControllerTest {

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = { "CASH" })
    public void shouldCreateCustomerGivenPaymentTypes(PaymentType paymentType) throws Exception {
        log.info("name = <{}>", paymentType);
        givenCreateCustomerRequestWithPaymentType(paymentType);
        givenCustomerServiceCreateCustomerReturnsCustomerId();
        whenCreateCustomersAPICalled();
        thenExpectCustomerServiceCreateCustomerCalledOnce();
        thenExpectResponseHasCreatedStatus();
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(mode = INCLUDE, names = { "CASH" })
    public void shouldNotCreateCustomerGivenInvalidPaymentType(PaymentType paymentType) throws Exception {
        log.info("name = <{}>", paymentType);
        givenCreateCustomerRequestWithPaymentType(paymentType);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    /*
     * GIVEN Method
     */

    private void givenCreateCustomerRequestWithPaymentType(PaymentType paymentType) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPaymentType(paymentType);
    }

}
