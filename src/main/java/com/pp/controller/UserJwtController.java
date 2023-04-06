package com.pp.controller;

import com.pp.dto.UserDTO;
import com.pp.dto.request.LoginRequest;
import com.pp.dto.request.RegisterRequest;
import com.pp.dto.response.LoginResponse;
import com.pp.dto.response.PPResponse;
import com.pp.dto.response.ResponseMessage;
import com.pp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserJwtController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<PPResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest)  {
        UserDTO userDTO = userService.saveUser(registerRequest);
        PPResponse response = new PPResponse(ResponseMessage.REGISTER_RESPONSE_MESSAGE,true,userDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest)  {
        LoginResponse response = userService.loginUser(loginRequest);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
}
