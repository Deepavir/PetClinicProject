package org.springframework.samples.petclinic.UserController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.entity.User;
import org.springframework.samples.petclinic.model.JwtRequest;
import org.springframework.samples.petclinic.model.JwtResponse;
import org.springframework.samples.petclinic.securityconfig.JwtUtils;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllers {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtTokenUtil;

	@Autowired
	private UserService userDetailsService;

	@PostMapping("/signup")
	public ResponseEntity saveUser(@RequestBody User user){
		this.userDetailsService.saveUser(user);
		return new ResponseEntity(Map.of("message","Registration done successfully"),HttpStatus.OK);
	}
	
	
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		System.out.println("createAuthenticationToken started");
		try {
			// authenticate username and password and than only will generate the token
			System.out.println("authentication manager started");
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLE", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
         UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userdetails);

		System.out.println("token " + token);
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setToken(token);
		return ResponseEntity.ok(jwtResponse);
	}

}
