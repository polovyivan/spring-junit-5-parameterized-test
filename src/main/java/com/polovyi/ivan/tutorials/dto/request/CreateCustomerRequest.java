package com.polovyi.ivan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {

    @NotNull(message = "Field fullName cannot be null")
    private String fullName;

    @NotNull(message = "Field phoneNumber cannot be null")
    private String phoneNumber;

    @NotNull(message = "Field address cannot be null")
    private String address;
}
