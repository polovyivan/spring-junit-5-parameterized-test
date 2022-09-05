package com.polovyi.ivan.tutorials.dto.request;

import com.polovyi.ivan.tutorials.enm.PaymentType;
import com.polovyi.ivan.tutorials.validations.NoCashPaymentType;
import com.polovyi.ivan.tutorials.validations.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Password(message = "Fields password and passwordConfirmation are invalid!")
public class CreateCustomerRequest {

    @NotBlank(message = "The field fullName cannot be blank")
    private String fullName;

    @NotNull(message = "The field paymentType cannot be null")
    @NoCashPaymentType(message = "The payment type CASH is not allowed!")
    private PaymentType paymentType;

    @NotNull(message = "The field birthDate cannot be null")
    @Past(message = "The field birthDate has to be in the past")
    private LocalDate birthDate;

    @NotNull(message = "The field address cannot be null")
    @Size(min = 10, max = 100, message = "The field address has to be between 10 and 100 characters")
    private String address;

    @NotEmpty(message = "The field phoneNumbers cannot be empty")
    private Set<String> phoneNumbers;

    private String password;

    private String passwordConfirmation;

}
