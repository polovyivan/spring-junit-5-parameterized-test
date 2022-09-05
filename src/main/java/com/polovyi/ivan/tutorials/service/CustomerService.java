package com.polovyi.ivan.tutorials.service;

import com.polovyi.ivan.tutorials.dto.request.CreateCustomerRequest;
import com.polovyi.ivan.tutorials.dto.response.CustomerResponse;
import com.polovyi.ivan.tutorials.entity.CustomerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerService {

    private static Set<CustomerEntity> customers = new HashSet<>();

    public String createCustomer(CreateCustomerRequest createCustomerRequest) {
        log.info("Creating a customer... ");
        CustomerEntity customerEntity = CustomerEntity.valueOf(createCustomerRequest);
        customers.add(customerEntity);
        return customerEntity.getId();
    }

    public List<CustomerResponse> getAllCustomers() {
        log.info("Getting all customers...");
        return customers.stream()
                .map(CustomerResponse::valueOf)
                .collect(Collectors.toList());
    }
}
