package com.polovyi.ivan.tutorials.dto.response;

import com.polovyi.ivan.tutorials.enm.PaymentType;
import com.polovyi.ivan.tutorials.entity.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private String id;

    private String fullName;

    private PaymentType paymentType;

    private LocalDate birthDate;

    private Set<String> phoneNumbers;

    private String address;

    private LocalDate createdAt;

    public static CustomerResponse valueOf(CustomerEntity customer) {
        return builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .paymentType(customer.getPaymentType())
                .birthDate(customer.getBirthDate())
                .phoneNumbers(customer.getPhoneNumber())
                .address(customer.getAddress())
                .createdAt(customer.getCreatedAt())
                .build();
    }

}
