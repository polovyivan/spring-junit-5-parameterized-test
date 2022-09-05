package com.polovyi.ivan.tutorials.entity;

import com.polovyi.ivan.tutorials.dto.request.CreateCustomerRequest;
import com.polovyi.ivan.tutorials.enm.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    private String id;

    private String fullName;

    private PaymentType paymentType;

    private LocalDate birthDate;

    private Set<String> phoneNumber;

    private String address;

    private LocalDate createdAt;

    private String password;

    private String passwordConfirmation;

    public static CustomerEntity valueOf(CreateCustomerRequest createCustomerRequest) {
        return builder()
                .id(UUID.randomUUID().toString())
                .fullName(createCustomerRequest.getFullName())
                .paymentType(createCustomerRequest.getPaymentType())
                .birthDate(createCustomerRequest.getBirthDate())
                .phoneNumber(createCustomerRequest.getPhoneNumbers())
                .address(createCustomerRequest.getAddress())
                .createdAt(LocalDate.now())
                .password(createCustomerRequest.getPassword())
                .passwordConfirmation(createCustomerRequest.getPasswordConfirmation())
                .build();
    }
}