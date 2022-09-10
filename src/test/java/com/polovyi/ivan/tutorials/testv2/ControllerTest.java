package com.polovyi.ivan.tutorials.testv2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.polovyi.ivan.tutorials.dto.request.CreateCustomerRequest;
import com.polovyi.ivan.tutorials.dto.response.CustomerResponse;
import com.polovyi.ivan.tutorials.enm.PaymentType;
import com.polovyi.ivan.tutorials.service.CustomerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
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
public abstract class ControllerTest {

    protected final static String CUSTOMERS_API_PATH = "/v1/customers";

    private static ObjectMapper mapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected CustomerService customerService;

    protected MockHttpServletResponse response;

    protected CustomerResponse customerResponse;

    protected CreateCustomerRequest createCustomerRequest;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    /*
     * GIVEN Methods
     */

    protected void givenCustomerServiceGetAllCustomersReturnsListOfCustomers() {
        customerResponse = CustomerResponse.builder()
                .id("1")
                .fullName("Ivan Polovyi")
                .address("Address")
                .phoneNumbers(Set.of("1-669-210-0504"))
                .createdAt(LocalDate.now())
                .build();
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

    protected void whenGetAllCustomersAPICalled() throws Exception {
        response = mockMvc.perform(get(CUSTOMERS_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }

    protected void whenCreateCustomersAPICalled() throws Exception {
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
