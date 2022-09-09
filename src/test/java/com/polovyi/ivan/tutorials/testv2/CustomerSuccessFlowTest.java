package com.polovyi.ivan.tutorials.testv2;

import com.polovyi.ivan.tutorials.dto.request.CreateCustomerRequest;
import com.polovyi.ivan.tutorials.dto.response.CustomerResponse;
import com.polovyi.ivan.tutorials.enm.PaymentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Slf4j
public class CustomerSuccessFlowTest extends ControllerTest {

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

    protected void givenValidCreateCustomerRequest() {
        createCustomerRequest = CreateCustomerRequest.builder()
                .fullName("Ivan Polovyi")
                .paymentType(PaymentType.VISA)
                .birthDate(LocalDate.of(1984, 1, 5))
                .phoneNumbers(Set.of("1-669-210-0504"))
                .address("Apt. 843 399 Lachelle Crossing, New Eldenhaven, LA 63962-9260")
                .password("password")
                .passwordConfirmation("password")
                .build();
    }

    protected void givenCustomerServiceCreateCustomerReturnsCustomerId() {
        doReturn("id-123").when(customerService).createCustomer(createCustomerRequest);
    }


    /*
     * WHEN Methods
     */

    public void whenGetAllCustomersAPICalled() throws Exception {
        response = mockMvc.perform(get(CUSTOMERS_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }

    public void whenCreateCustomersAPICalled() throws Exception {
        response = mockMvc.perform(post(CUSTOMERS_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJsonString(createCustomerRequest)))
                .andReturn()
                .getResponse();
    }


    /*
     * THEN Methods
     */

    protected void thenExpectResponseHasOkStatus() {
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    protected void thenExpectResponseHasBadRequestStatus() {
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    protected void thenExpectResponseHasCreatedStatus() {
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    protected void thenExpectResponseWithCustomerList() throws UnsupportedEncodingException {
        List<CustomerResponse> getAllCustomers = stringJsonToList(response.getContentAsString(),
                CustomerResponse.class);
        assertTrue(getAllCustomers.size() == 1);
        assertTrue(getAllCustomers.contains(customerResponse));
    }

    protected void thenExpectCustomerServiceGetAllCustomersCalledOnce() {
        verify(customerService).getAllCustomers();
    }

    protected void thenExpectCustomerServiceCreateCustomerCalledOnce() {
        verify(customerService).createCustomer(any(CreateCustomerRequest.class));
    }

    protected void thenExpectNoCallToCustomerServiceCreateCustomer() {
        verify(customerService, times(0)).createCustomer(any(CreateCustomerRequest.class));
    }

}
