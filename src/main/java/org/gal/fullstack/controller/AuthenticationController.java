package org.gal.fullstack.controller;

import java.util.Optional;

import org.gal.fullstack.api.LoginDto;
import org.gal.fullstack.api.TokenDto;
import org.gal.fullstack.api.UserDto;
import org.gal.fullstack.entity.User;
import org.gal.fullstack.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/auth")
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthenticationController {
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("authenticate")
	public ResponseEntity<TokenDto> authenticate(@RequestBody LoginDto loginDto) {
		
		User u = Optional.ofNullable(userService.findByUserName(loginDto.getUsername()))
							.filter(User::isEnabled)
							.filter(us -> us.getPassword().equals(loginDto.getPassword()))
							.orElseThrow(LoginFailureException::new);
		
		String token = new String(Base64.encode((u.getUsername() + ":" + u.getPassword()).getBytes()));
		TokenDto tokenDto = new TokenDto();
		tokenDto.setUsername(loginDto.getUsername());
		tokenDto.setToken(token);
		
		return new ResponseEntity<TokenDto>(tokenDto , HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<UserDto> user() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		System.out.println("Username: " + username);
		UserDto user = Optional.ofNullable(userService.findByUserName(username))
								.filter(User::isEnabled)
								.map(this::toUserDto)
								.orElseThrow(UserNotFoundException::new);
		
		return new ResponseEntity<UserDto>(user, HttpStatus.OK);
	}
	
	private UserDto toUserDto(User u) {
		UserDto user = new UserDto();
		user.setFirstName(u.getFirstName());
		user.setLastName(u.getLastName());
		user.setUsername(u.getUsername());
		user.setRoles(u.getRoles());
		return user;
	}

}
