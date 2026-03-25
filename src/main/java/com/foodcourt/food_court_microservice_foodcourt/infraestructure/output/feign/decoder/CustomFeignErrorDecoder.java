package com.foodcourt.food_court_microservice_foodcourt.infraestructure.output.feign.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcourt.food_court_microservice_foodcourt.application.dto.response.UserErrorResponse;
import com.foodcourt.food_court_microservice_foodcourt.infraestructure.exception.UserServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        String body;

        try (InputStream is = response.body().asInputStream()) {
            body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            UserErrorResponse error = mapper.readValue(body, UserErrorResponse.class);

            return new UserServiceException(
                    error.getMessage(),
                    response.status()
            );

        } catch (Exception e) {
            return new UserServiceException("Error parsing user service response", response.status());
        }
    }
}