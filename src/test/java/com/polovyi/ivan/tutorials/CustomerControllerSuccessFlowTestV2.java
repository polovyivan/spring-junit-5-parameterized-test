package com.polovyi.ivan.tutorials;

import com.polovyi.ivan.tutorials.dto.response.CustomerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class CustomerControllerSuccessFlowTestV2 extends ControllerTest {


    /*
    GET /v1/customers
     */

    @Test
    public void shouldReturnListOfCustomersFromGetAllCustomersRestAPI() throws Exception {
        givenCustomerResponse();
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
        givenCustomerResponse();
        givenValidCreateCustomerRequest();
        givenCustomerServiceCreateCustomerReturnsCustomerId();
        whenCreateCustomersAPICalled();
        thenExpectCustomerServiceCreateCustomerCalledOnce();
        thenExpectResponseHasCreatedStatus();
    }

    /*
     * GIVEN Method
     */

    private void givenCustomerResponse() {
        customerResponse = CustomerResponse.builder()
                .id("1")
                .fullName("Ivan Polovyi")
                .address("Address")
                .phoneNumbers(Set.of("1-669-210-0504"))
                .createdAt(LocalDate.now())
                .build();
    }

    private void givenCustomerServiceGetAllCustomersReturnsListOfCustomers() {
        doReturn(List.of(customerResponse)).when(customerService).getAllCustomers();
    }

}
