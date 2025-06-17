package com.marcoindev.mcshop.auth.config.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Feign Client: Bad request through Feign");
            case 404:
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Feign Client: User not found");
            case 500:
                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Feign Client: User service error");
            default:
                return new Exception("Feign Client: generic error");
        }
    }
}