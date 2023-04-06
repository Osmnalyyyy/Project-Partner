package com.pp.controller;

import com.pp.dto.UserDTO;
import com.pp.dto.request.AdminUserUpdateRequest;
import com.pp.dto.request.UpdatePasswordRequest;
import com.pp.dto.request.UserUpdateRequest;
import com.pp.dto.response.ResponseMessage;
import com.pp.dto.response.PPResponse;
import com.pp.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		super(); 
		this.userService = userService;
	}

	@GetMapping("/auth/all")
	@PreAuthorize("hasRole('ADMIN')")// ROLE_ADMIN----->>>>>hasRole methodu içinde bulunan değerin önüne (ROLE_) ibaresi yoksa ekler
	public ResponseEntity<List<UserDTO>> getAllUsers(){
	List<UserDTO> allUsers=userService.getAllUsersDTO();
	return ResponseEntity.ok(allUsers);
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<UserDTO> getUser(){
	UserDTO userDTO=userService.getPrincipal();
	return ResponseEntity.ok(userDTO);
	}

	@GetMapping("/auth/pages")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<UserDTO>> getAllUsersByPage(@RequestParam("page") int page,
														   @RequestParam("size") int size,
														   @RequestParam("sort") String prop, // hangi sutuna göre sıralayacagız
														   @RequestParam(value = "direction", required = true, // direction required olmasın
																defaultValue = "DESC") Direction direction) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
		Page<UserDTO> userDTOPage=userService.getUserPage(pageable);
		return ResponseEntity.ok(userDTOPage);
	}
	

	@GetMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id){
	UserDTO userDTO=userService.getUserById(id);
	return ResponseEntity.ok(userDTO);
	}
	
	
	// update Password
	@PatchMapping("/auth")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<PPResponse> updatePassword(@Valid@RequestBody UpdatePasswordRequest updatePasswordRequest){
		userService.updatePassword(updatePasswordRequest);
		PPResponse response=new PPResponse(ResponseMessage.PASSWORD_UPDATE_RESPONSE,true);
		return ResponseEntity.ok(response);
	}
	
	// update user
	@PutMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<PPResponse> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest){
		userService.updateUser(userUpdateRequest);
		PPResponse response=new PPResponse(ResponseMessage.USER_UPDATE_RESPONSE,true);
		return ResponseEntity.ok(response);
	}
	
	
	// admin herhangi bir kullanıcıyı update etsin
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PPResponse> updateUserAuthenticate(@PathVariable("id")Long id, @Valid @RequestBody AdminUserUpdateRequest adminUserUpdateRequest ){
	userService.updateUserAuth(id,adminUserUpdateRequest);
		PPResponse response=new PPResponse(ResponseMessage.USER_UPDATE_RESPONSE,true);
		return ResponseEntity.ok(response);
	}
	
	//delete user
	@DeleteMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PPResponse> deleteUser(@PathVariable Long id){
		userService.removeUserById(id);
		PPResponse response=new PPResponse(ResponseMessage.USER_DELETE_RESPONSE,true);
		return ResponseEntity.ok(response);
	}


	
	
}
	

