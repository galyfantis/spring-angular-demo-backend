package org.gal.fullstack.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.gal.fullstack.api.UserDto;
import org.gal.fullstack.entity.User;
import org.gal.fullstack.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("api/users")
@CrossOrigin(origins = {"http://localhost:4200"})
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@GetMapping
	public ResponseEntity<List<UserDto>> list() {
		List<UserDto> users = userService.getAll().stream()
					.map(this::toUserDto)
					.collect(Collectors.toList());
		
		return new ResponseEntity<List<UserDto>>(users, HttpStatus.OK);
	}
	
	@GetMapping("{username}")
	public ResponseEntity<UserDto> user(@PathVariable("username") String username) {
		System.out.println("Path param: " + username);
		UserDto user = Optional.ofNullable(userService.findByUserName(username))
								.map(this::toUserDto)
								.orElseThrow(UserNotFoundException::new);
		
		return new ResponseEntity<UserDto>(user, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody UserDto user, UriComponentsBuilder builder) {
        User u = new User();
        toUser(user, u);
        
        if (u.getRoles().isEmpty()) {
        	u.getRoles().add("USER");
        }
        u.setPassword("Test1");
        u.setEnabled(false);
        
		boolean flag = userService.createUser(u);
        if (flag == false) {
        	return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/user?id={id}").buildAndExpand(u.getUsername()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Void> updateUser(@RequestBody UserDto user) {
		User persistedUser = Optional.ofNullable(userService.findByUserName(user.getUsername())).orElseThrow(UserNotFoundException::new);
		toUser(user, persistedUser);
		userService.updateUser(persistedUser);
        return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("{username}")
	public ResponseEntity<Void> deleteUser(@PathVariable("username") String username) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = auth.getName();
		if (currentUsername.equals(username)) {
			throw new CannotDeleteSelfException();
		}
		userService.deleteUser(username);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("{username}/enable")
	public ResponseEntity<Void> enableUser(@PathVariable("username") String username) {
		userService.enableUser(username);
        return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PostMapping("{username}/disable")
	public ResponseEntity<Void> disableUser(@PathVariable("username") String username) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = auth.getName();
		if (currentUsername.equals(username)) {
			throw new CannotDisableSelfException();
		}
		userService.disableUser(username);
        return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PostMapping("register")
	public ResponseEntity<Void> registerUser(@RequestBody UserDto user, UriComponentsBuilder builder) {
        User u = new User();
        toUser(user, u);
        
        if (u.getRoles().isEmpty()) {
        	u.getRoles().add("USER");
        }
        u.setEnabled(true);
        u.setPassword(user.getPassword());
        
		boolean flag = userService.createUser(u);
        if (flag == false) {
        	return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/user?id={id}").buildAndExpand(u.getUsername()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	private UserDto toUserDto(User u) {
		UserDto user = new UserDto();
		user.setFirstName(u.getFirstName());
		user.setLastName(u.getLastName());
		user.setUsername(u.getUsername());
		user.setRoles(u.getRoles());
		user.setEnabled(u.isEnabled());
		return user;
	}
	
	private void toUser(UserDto u, User user) {
		user.setFirstName(u.getFirstName());
		user.setLastName(u.getLastName());
		user.setUsername(u.getUsername());
		user.setRoles(u.getRoles() == null ? new HashSet<>() : u.getRoles());
		user.setEnabled(u.isEnabled());
	}

}
