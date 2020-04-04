package com.revature.practice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.practice.model.AuthenticationRequest;
import com.revature.practice.model.AuthenticationResponse;
import com.revature.practice.service.JwtUtil;
import com.revature.practice.service.MyUserDetailsService;

@RestController

public class Hello {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping({ "/" })
	public String hello() {
		return "<h1>Hello. This is Endpoint</h1>";
	}

	@GetMapping({ "/user" })
	@PreAuthorize("hasAnyRole('USER','ADMIN')") 
	public String user() {
		return "<h1>Hello. This is User</h1>";
	}

	@GetMapping({ "/admin" })
	@PreAuthorize("hasRole('ADMIN')")
	public String admin() {
		return "<h1>Hello. This is Admin</h1>";
	}

	@GetMapping({ "/dashboard" })
	public String dashboard() {
		return "<h1>Hello. This is Dashboard</h1>";
	}

	@PostMapping({ "/authenticate" })
	public ResponseEntity<?> createAuthenticateToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect Username and Password", e);

		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}
