package org.example.main.feign;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

import java.util.Date;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() >= 500)
        {
            return new RetryableException(
                    response.status(),
                    "Retrying due to server error",
                    response.request().httpMethod(),
                    100L,
                    response.request()
            );
        }

        return new Default().decode(methodKey, response);
    }
}

