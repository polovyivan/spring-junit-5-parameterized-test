package com.polovyi.ivan.tutorials;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.polovyi.ivan.tutorials.dto.request.CreateCustomerRequest;
import com.polovyi.ivan.tutorials.dto.response.CustomerResponse;
import com.polovyi.ivan.tutorials.enm.PaymentType;
import com.polovyi.ivan.tutorials.service.CustomerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class CustomerControllerTest {

    private final static String CUSTOMERS_API_PATH = "/v1/customers";

    private static ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private MockHttpServletResponse response;

    private CustomerResponse customerResponse;

    private CreateCustomerRequest createCustomerRequest;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }


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
    public void shouldCreateCustomer() throws Exception {
        givenCustomerResponse();
        givenValidCreateCustomerRequest();
        givenCustomerServiceCreateCustomerReturnsCustomerResponse();
        whenCreateCustomersAPICalled();
        thenExpectCustomerServiceCreateCustomerCalledOnce();
        thenExpectResponseHasCreatedStatus();
    }

    // @NotBlank
    @Test
    public void shouldNotCreateCustomerGivenFullNameAsNull() throws Exception {
        givenRequestWithFullNameAsNull();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenFullNameAsEmptyString() throws Exception {
        givenRequestWithFullNameAsEmptyString();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenFullNameAsBlankString() throws Exception {
        givenRequestWithFullNameAsBlankString();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }







    @Test
    public void shouldNotCreateCustomerGivenPhoneNumberAsNull() throws Exception {
        givenRequestWithPhoneNumberAsNull();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenAddressAsNull() throws Exception {
        givenRequestWithAddressAsNull();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }


    /*
     * GIVEN Methods
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

    private void givenValidCreateCustomerRequest() {
        createCustomerRequest = CreateCustomerRequest.builder()
                .fullName("Ivan Polovyi")
                .paymentType(PaymentType.VISA)
                .birthDate(LocalDate.of(1984,1,5))
                .phoneNumbers(Set.of("1-669-210-0504"))
                .address("Apt. 843 399 Lachelle Crossing, New Eldenhaven, LA 63962-9260")
                .build();
    }

    private void givenCustomerServiceCreateCustomerReturnsCustomerResponse() {
        doReturn("id-123").when(customerService).createCustomer(createCustomerRequest);
    }

    private void givenRequestWithFullNameAsNull() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setFullName(null);
    }

    private void givenRequestWithFullNameAsEmptyString() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setFullName("");
    }

    private void givenRequestWithFullNameAsBlankString() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setFullName(" ");
    }

    private void givenRequestWithPhoneNumberAsNull() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPhoneNumbers(null);

    }

    private void givenRequestWithAddressAsNull() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setAddress(null);
    }

    /*
     * WHEN Methods
     */

    private void whenGetAllCustomersAPICalled() throws Exception {
        response = mockMvc.perform(get(CUSTOMERS_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }

    private void whenCreateCustomersAPICalled() throws Exception {
        response = mockMvc.perform(post(CUSTOMERS_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJsonString(createCustomerRequest)))
                .andReturn()
                .getResponse();
    }


    /*
     * THEN Methods
     */

    private void thenExpectResponseHasOkStatus() {
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    private void thenExpectResponseHasBadRequestStatus() {
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    private void thenExpectCustomerServiceGetAllCustomersCalledOnce() {
        verify(customerService).getAllCustomers();
    }

    private void thenExpectResponseWithCustomerList() throws UnsupportedEncodingException {
        List<CustomerResponse> getAllCustomers = stringJsonToList(response.getContentAsString(),
                CustomerResponse.class);
        assertTrue(getAllCustomers.size() == 1);
        assertTrue(getAllCustomers.contains(customerResponse));
    }

    private void thenExpectCustomerServiceCreateCustomerCalledOnce() {
        verify(customerService).createCustomer(any(CreateCustomerRequest.class));
    }

    private void thenExpectNoCallToCustomerServiceCreateCustomer() {
        verify(customerService, times(0)).createCustomer(any(CreateCustomerRequest.class));
    }

    private void thenExpectResponseHasCreatedStatus() {
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @SneakyThrows
    protected <T> List<T> stringJsonToList(String json, Class<T> clazz) {
        return mapper.readValue(json, new TypeReference<>() {
            @Override
            public Type getType() {
                return mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            }
        });
    }

    @SneakyThrows
    protected String objectToJsonString(Object object) {
        return mapper.writeValueAsString(object);
    }

}
