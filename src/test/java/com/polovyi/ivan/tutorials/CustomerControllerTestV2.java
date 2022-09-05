package com.polovyi.ivan.tutorials;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.polovyi.ivan.tutorials.dto.request.CreateCustomerRequest;
import com.polovyi.ivan.tutorials.dto.response.CustomerResponse;
import com.polovyi.ivan.tutorials.enm.PaymentType;
import com.polovyi.ivan.tutorials.service.CustomerService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class CustomerControllerTestV2 extends ControllerTest {

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
    public void shouldCreateCustomerCallingRestAPI() throws Exception {
        givenCustomerResponse();
        givenValidCreateCustomerRequest();
        givenCustomerServiceCreateCustomerReturnsCustomerResponse();
        whenCreateCustomersAPICalled();
        thenExpectCustomerServiceCreateCustomerCalledOnce();
        thenExpectResponseHasCreatedStatus();
    }

    // @NotBlank
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
    public void shouldNotCreateCustomerGivenFullNameAsNull(String name) throws Exception {
        log.info("name = <{}>", name);
        givenRequestWithInvalidFullName(name);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    // @NotNull
    // Enum

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = { "CASH" })
    public void shouldCreateCustomerGivenPaymentTypes(PaymentType paymentType) throws Exception {
        log.info("name = <{}>", paymentType);
        givenCustomerResponse();
        givenCreateCustomerRequestWithPaymentType(paymentType);
        givenCustomerServiceCreateCustomerReturnsCustomerResponse();
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

    // Data
    @ParameterizedTest
    @ValueSource(strings = { "2030-06-01" })
    public void shouldNotCreateCustomerGivenInvalidDate(
            @JavaTimeConversionPattern("yyyy-MM-dd") LocalDate birthDate) throws Exception {
        log.info("name = <{}>", birthDate);
        givenCreateCustomerRequestWithBirthDate(birthDate);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    // Address
    @ParameterizedTest
    @MethodSource("addressRange")
    public void shouldNotCreateCustomerGivenInvalidAddress(
            String address) throws Exception {
        log.info("address = <{}>", address);
        givenCreateCustomerRequestWithAddress(address);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    // Phones
    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateCustomerGivenInvalidPhone(
            Set<String> phones) throws Exception {
        log.info("phones = <{}>", phones);
        givenCreateCustomerRequestWithAddress(phones);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @MethodSource("invalidPhones")
    public void shouldNotCreateCustomerGivenInvalidPhoneV2(
            Set<String> phones) throws Exception {
        log.info("phones = <{}>", phones);
        givenCreateCustomerRequestWithAddress(phones);
        whenCreateCustomersAPICalled();
        thenExpectNoCallToCustomerServiceCreateCustomer();
        thenExpectResponseHasBadRequestStatus();
    }


    // Password
    @ParameterizedTest
    @MethodSource("invalidPasswordAndPasswordConfirmationCombination")
    public void shouldNotCreateCustomerGivenInvalidPasswordAndPasswordCombination(String password,
            String passwordConfirmation) throws Exception {
        log.info("password = <{}>, passwordConfirmation = <{}>", password, passwordConfirmation);
        givenCreateCustomerRequestWithAddress(password, passwordConfirmation);
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

    private void givenCreateCustomerRequestWithPaymentType(PaymentType paymentType) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPaymentType(paymentType);
    }

    private void givenCreateCustomerRequestWithBirthDate(LocalDate birthDate) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setBirthDate(birthDate);
    }

    private void givenCreateCustomerRequestWithAddress(String address) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setAddress(address);
    }

    private void givenCreateCustomerRequestWithAddress(Set<String> phones) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPhoneNumbers(phones);
    }

    private void givenCreateCustomerRequestWithAddress(String password, String passwordConfirmation) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPassword(password);
        createCustomerRequest.setPasswordConfirmation(passwordConfirmation);
    }

    private void givenCustomerServiceCreateCustomerReturnsCustomerResponse() {
        doReturn("id-123").when(customerService).createCustomer(createCustomerRequest);
    }

    private void givenRequestWithInvalidFullName(String name) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setFullName(name);
    }

    private void givenRequestWithInvalidPaymentType(PaymentType paymentType) {
        givenValidCreateCustomerRequest();
        createCustomerRequest.setPaymentType(paymentType);
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
