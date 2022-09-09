package com.polovyi.ivan.tutorials;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.polovyi.ivan.tutorials.dto.request.CreateCustomerRequest;
import com.polovyi.ivan.tutorials.dto.response.CustomerResponse;
import com.polovyi.ivan.tutorials.enm.PaymentType;
import com.polovyi.ivan.tutorials.service.CustomerService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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

    // full name validation
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

    // payment type
    @Test
    public void shouldNotCreateCustomerGivenPaymentTypeAsNull() throws Exception {
        givenRequestWithPaymentTypeAsNull();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenInvalidPaymentType() throws Exception {
        givenRequestWithInvalidPaymentType();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    // birth Date
    @Test
    public void shouldNotCreateCustomerGivenBirthDateAsNull() throws Exception {
        givenRequestWithBirthDateAsNull();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenBirthDateLessThenRequired() throws Exception {
        givenRequestWithBirthDateLessThenRequired();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenBirthDateMoreThenRequired() throws Exception {
        givenRequestWithBirthDateMoreThenRequired();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    // address validation
    @Test
    public void shouldNotCreateCustomerGivenAddressAsNull() throws Exception {
        givenRequestWithAddressAsNull();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenAddressSizeLessThenRequired() throws Exception {
        givenRequestWithAddressWithSizeLessThanRequired();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenAddressSizeMoreThenRequired() throws Exception {
        givenRequestWithAddressWithSizeMoreThanRequired();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    // phone numbers validation
    @Test
    public void shouldNotCreateCustomerGivenPhoneNumbersAsNull() throws Exception {
        givenRequestWithPhoneNumbersAsNull();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenPhoneNumbersAsEmptySet() throws Exception {
        givenRequestWithPhoneNumbersAsEmptyList();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    // password validation
    @Test
    public void shouldNotCreateCustomerGivenPasswordAndPasswordConfirmationAsNull() throws Exception {
        givenRequestWithPasswordAndPasswordConfirmationAsNull();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenPasswordAsNull() throws Exception {
        givenRequestWithPasswordAsNull();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenPasswordConfirmationAsNull() throws Exception {
        givenRequestWithPasswordConfirmationAsNull();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenPasswordDifferentThanPasswordConfirmation() throws Exception {
        givenRequestWithPasswordDifferentThanPasswordConfirmation();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenPasswordAndPasswordConfirmationAsEmptyString() throws Exception {
        givenRequestWithPasswordAndPasswordConfirmationAsEmptyString();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenPasswordAsEmptyString() throws Exception {
        givenRequestWithPasswordAndPasswordAsEmptyString();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenPasswordConfirmationAsEmptyString() throws Exception {
        givenRequestWithPasswordConfirmationAsEmptyString();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenPasswordAndPasswordConfirmationAsBlankString() throws Exception {
        givenRequestWithPasswordAndPasswordConfirmationAsBlankString();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenPasswordAsBlankString() throws Exception {
        givenRequestWithPasswordAsBlankString();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenPasswordConfirmationAsBlankString() throws Exception {
        givenRequestWithPasswordConfirmationAsBlankString();
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @Test
    public void shouldNotCreateCustomerGivenPasswordAsEmptyStringAndPasswordConfirmationAsBlankString() throws Exception {
        givenRequestWithPasswordAsEmptyStringAndPasswordConfirmationAsBlankString();
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
                .birthDate(LocalDate.of(1984, 1, 5))
                .phoneNumbers(Set.of("1-669-210-0504"))
                .address("Apt. 843 399 Lachelle Crossing, New Eldenhaven, LA 63962-9260")
                .password("password")
                .passwordConfirmation("password")
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

    private void givenRequestWithPaymentTypeAsNull() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPaymentType(null);
    }

    private void givenRequestWithInvalidPaymentType() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPaymentType(PaymentType.CASH);
    }

    private void givenRequestWithPhoneNumbersAsNull() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPhoneNumbers(null);

    }

    private void givenRequestWithPhoneNumbersAsEmptyList() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPhoneNumbers(Set.of());

    }

    private void givenRequestWithAddressAsNull() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setAddress(null);
    }

    private void givenRequestWithAddressWithSizeLessThanRequired() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setAddress(RandomStringUtils.randomAlphabetic(9));
    }

    private void givenRequestWithAddressWithSizeMoreThanRequired() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setAddress(RandomStringUtils.randomAlphabetic(101));
    }

    private void givenRequestWithBirthDateAsNull() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setBirthDate(null);
    }

    private void givenRequestWithBirthDateLessThenRequired() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setBirthDate(LocalDate.of(1899, 12, 31));
    }

    private void givenRequestWithBirthDateMoreThenRequired() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setBirthDate(LocalDate.of(2030, 06, 01));
    }

    private void givenRequestWithPasswordAndPasswordConfirmationAsNull() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword(null);
        createCustomerRequest.setPasswordConfirmation(null);
    }

    private void givenRequestWithPasswordAsNull() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword(null);
        createCustomerRequest.setPasswordConfirmation("password");
    }

    private void givenRequestWithPasswordConfirmationAsNull() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword("password");
        createCustomerRequest.setPasswordConfirmation(null);
    }

    private void givenRequestWithPasswordDifferentThanPasswordConfirmation() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword("password");
        createCustomerRequest.setPasswordConfirmation("_password");
    }

    private void givenRequestWithPasswordAndPasswordConfirmationAsEmptyString() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword(StringUtils.EMPTY);
        createCustomerRequest.setPasswordConfirmation(StringUtils.EMPTY);
    }

    private void givenRequestWithPasswordAndPasswordAsEmptyString() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword(StringUtils.EMPTY);
        createCustomerRequest.setPasswordConfirmation("password");
    }

    private void givenRequestWithPasswordConfirmationAsEmptyString() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword("password");
        createCustomerRequest.setPasswordConfirmation(StringUtils.EMPTY);
    }

    private void givenRequestWithPasswordAndPasswordConfirmationAsBlankString() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword(StringUtils.SPACE);
        createCustomerRequest.setPasswordConfirmation(StringUtils.SPACE);
    }

    private void givenRequestWithPasswordAsBlankString() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword(StringUtils.SPACE);
        createCustomerRequest.setPasswordConfirmation("password");
    }

    private void givenRequestWithPasswordConfirmationAsBlankString() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword("password");
        createCustomerRequest.setPasswordConfirmation(StringUtils.SPACE);
    }

    private void givenRequestWithPasswordAsEmptyStringAndPasswordConfirmationAsBlankString() {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword(StringUtils.EMPTY);
        createCustomerRequest.setPasswordConfirmation(StringUtils.SPACE);
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
