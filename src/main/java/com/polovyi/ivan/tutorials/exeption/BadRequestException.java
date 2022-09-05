package com.polovyi.ivan.exeption;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class BadRequestException extends RuntimeException implements GraphQLError {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    private String message = "Bad request";

    // Below code used for GraphQL only
    private List<SourceLocation> locations;

    public BadRequestException(String message, List<SourceLocation> locations) {
        this.message = message;
        this.locations = locations;
    }

    @Override
    public Map<String, Object> getExtensions() {
        Map<String, Object> customAttributes = new LinkedHashMap<>();
        customAttributes.put("errorCode", this.status.value());
        return customAttributes;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return locations;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.ValidationError;
    }

    @Override
    public Map<String, Object> toSpecification() {
        return GraphQLError.super.toSpecification();
    }

}
