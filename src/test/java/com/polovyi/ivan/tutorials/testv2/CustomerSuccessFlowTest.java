package com.polovyi.ivan.tutorials.testv2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class CustomerSuccessFlowTest extends ControllerTest {

    /*
    GET /v1/customers
     */

    @Test
    public void shouldReturnListOfCustomersFromGetAllCustomersRestAPI() throws Exception {
        givenCustomerServiceGetAllCustomersReturnsListOfCustomers();
        whenGetAllCustomersAPICalled();
        thenExpectResponseHasOkStatus();
        thenExpectCustomerServiceGetAllCustomersCalledOnce();
        thenExpectResponseWithCustomerList();
    }

    /*
    POST /v1/customers
     */

    @Test
    public void shouldCreateCustomerCallingRestAPI() throws Exception {
        givenValidCreateCustomerRequest();
        givenCustomerServiceCreateCustomerReturnsCustomerId();
        whenCreateCustomersAPICalled();
        thenExpectCustomerServiceCreateCustomerCalledOnce();
        thenExpectResponseHasCreatedStatus();
    }

}
