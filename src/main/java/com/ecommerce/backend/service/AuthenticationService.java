package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.UserDto;
import com.ecommerce.backend.dto.request.user.AuthenticationRequest;
import com.ecommerce.backend.dto.request.user.VerifyRequest;
import com.ecommerce.backend.dto.response.AuthenticationResponse;
import com.ecommerce.backend.entity.user.User;

public interface AuthenticationService {

    UserDto register(User user);
    AuthenticationResponse authenticate(AuthenticationRequest request);

    void verify(VerifyRequest verifyRequest);
    void resendVerificationCode(VerifyRequest verifyRequest);

    void redeemPassword(String email);
    void resetPassword(String token, String password);

}
