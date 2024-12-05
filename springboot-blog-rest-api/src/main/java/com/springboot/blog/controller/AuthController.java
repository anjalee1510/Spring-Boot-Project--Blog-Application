package com.springboot.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.payload.JWTAuthResponse;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.SignupDto;
import com.springboot.blog.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	
	// Login REST API 
	@PostMapping(value={"/login","/signin"})
	public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
		String response =authService.login(loginDto);
		
		JWTAuthResponse authResponse=new JWTAuthResponse();
		authResponse.setAccesToken(response);
		authResponse.setTokenType("Bearer");
		return ResponseEntity.ok(authResponse);
	}
	
	//Register REST API
	@PostMapping(value={"/register","/signup"})
	public ResponseEntity<String> register(@RequestBody SignupDto signupDto){
		String response=authService.register(signupDto);
		
		return new ResponseEntity<>(response,HttpStatus.CREATED);
		
	}
	

}
