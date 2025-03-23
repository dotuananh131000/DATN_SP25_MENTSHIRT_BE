package com.java.project.controllers;

import com.java.project.dtos.ApiResponse;
import com.java.project.dtos.AuthenTicationResponse;
import com.java.project.dtos.IntrospceResponse;
import com.java.project.request.APIRequestOrResponse;
import com.java.project.request.AuthenticationRequest;
import com.java.project.request.IntrospecRequest;
import com.java.project.services.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    public APIRequestOrResponse<AuthenTicationResponse>authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        var result = authenticationService.getAuthenTication(authenticationRequest);
        return APIRequestOrResponse.<AuthenTicationResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/introspect")
    public APIRequestOrResponse<IntrospceResponse>authticate(@RequestBody IntrospecRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspce(request);
        return APIRequestOrResponse.<IntrospceResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/token/khach-hang")
    public APIRequestOrResponse<AuthenTicationResponse>authenticateCustomer(@RequestBody AuthenticationRequest authenticationRequest){
        var result = authenticationService.getAuthenTicationCustomer(authenticationRequest);
        return APIRequestOrResponse.<AuthenTicationResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/introspect/khach-hang")
    public APIRequestOrResponse<IntrospceResponse>authticateCustomer(@RequestBody IntrospecRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspce(request);
        return APIRequestOrResponse.<IntrospceResponse>builder()
                .data(result)
                .build();
    }

}
