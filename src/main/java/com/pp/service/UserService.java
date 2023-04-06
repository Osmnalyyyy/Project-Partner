package com.pp.service;

import com.pp.domain.Role;
import com.pp.domain.User;
import com.pp.domain.enums.RoleType;
import com.pp.dto.UserDTO;
import com.pp.dto.request.*;
import com.pp.dto.response.LoginResponse;
import com.pp.exception.BadRequestException;
import com.pp.exception.ConflictException;
import com.pp.exception.ResourceNotFoundException;
import com.pp.exception.message.ErrorMessage;
import com.pp.mapper.UserMapper;
import com.pp.repository.ParticipantRepository;
import com.pp.repository.UserRepository;
import com.pp.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final ParticipantRepository participantRepository;


	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, email)));
	}

	public void save(User user){
		userRepository.save(user);
	}

	public UserDTO saveUser(RegisterRequest registerRequest) {
		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			throw new ConflictException(
					String.format(ErrorMessage.EMAIL_ALREADY_EXISTS_MESSAGE, registerRequest.getEmail()));
		}

		Role role = roleService.findByType(RoleType.ROLE_USER);

		Set<Role> roles = new HashSet<>();
		roles.add(role);
		String encodedPassword =  passwordEncoder.encode(registerRequest.getPassword());
		User user = new User();


		user.setName(registerRequest.getName());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(encodedPassword);
		user.setRoles(roles);

		userRepository.save(user);

		return userMapper.userToUserDTO(user);
	}

	public LoginResponse loginUser(LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
		Authentication authentication  =  authenticationManager.
				authenticate(usernamePasswordAuthenticationToken);
		UserDetails userDetails  =  (UserDetails) authentication.getPrincipal() ;
		String jwtToken =   jwtUtils.generateJwtToken(userDetails);
		return new LoginResponse(jwtToken);
	}

	public List<UserDTO> getAllUsersDTO() {
		List<User> usersList = userRepository.findAll();
		return userMapper.map(usersList);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public UserDTO getPrincipal() {
		User currentUser = getCurrentUser();
		return userMapper.userToUserDTO(currentUser);
	}

	public User getCurrentUser() {
		String email = com.pp.security.SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));

		return getUserByEmail(email);
	}

	public Page<UserDTO> getUserPage(Pageable pageable) {
		Page<User> userPage = userRepository.findAll(pageable);
		return userPage.map(userMapper::userToUserDTO);
	}


	public UserDTO getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
		return userMapper.userToUserDTO(user);
	}

	public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
		User user = getCurrentUser();
		if (user.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}
		if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
			throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED);
		}
//
		String hashedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
		user.setPassword(hashedPassword);
		userRepository.save(user);
	}


	@Transactional
	public void updateUser( UserUpdateRequest userUpdateRequest) {

		User user = getCurrentUser();

		if (user.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}

		boolean emailExist = userRepository.existsByEmail(userUpdateRequest.getEmail());

		if (emailExist && !userUpdateRequest.getEmail().equals(user.getEmail())) {
			throw new ConflictException(
					String.format(ErrorMessage.EMAIL_ALREADY_EXISTS_MESSAGE, userUpdateRequest.getEmail()));
		}

		userRepository.update(user.getId(),
				userUpdateRequest.getName(),
				userUpdateRequest.getJob(),
				userUpdateRequest.getLocation(),
				userUpdateRequest.getEmail(),
				userUpdateRequest.getPhone(),
				userUpdateRequest.getWebsite(),
				userUpdateRequest.getAboutMe());
	}


	public void updateUserAuth (Long id, AdminUserUpdateRequest adminUserUpdateRequest){

		User user = getById(id);

		if (user.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}

		boolean emailExist1 = userRepository.existsByEmail(adminUserUpdateRequest.getEmail());

		if (emailExist1 && !adminUserUpdateRequest.getEmail().equals(user.getEmail())) {
			throw new ConflictException(
					String.format(ErrorMessage.EMAIL_ALREADY_EXISTS_MESSAGE, adminUserUpdateRequest.getEmail()));
		}
		// password boş ise
		if (adminUserUpdateRequest.getPassword() == null) {
			adminUserUpdateRequest.setPassword(user.getPassword());
		} else {
			String encodedPassword = passwordEncoder.encode(adminUserUpdateRequest.getPassword());
			adminUserUpdateRequest.setPassword(encodedPassword);
		}

		Set<String> userStrRoles = adminUserUpdateRequest.getRoles();
		Set<Role> roles = convertRoles(userStrRoles);

		user.setName(adminUserUpdateRequest.getName());
		user.setJob(adminUserUpdateRequest.getJob());
		user.setLocation(adminUserUpdateRequest.getLocation());
		user.setEmail(adminUserUpdateRequest.getEmail());
		user.setPhone(adminUserUpdateRequest.getPhone());
		user.setPassword(adminUserUpdateRequest.getPassword());
		user.setWebsite(adminUserUpdateRequest.getWebsite());
		user.setBuiltIn(adminUserUpdateRequest.getBuiltIn());
		user.setRoles(roles);

		userRepository.save(user);

	}

	public User getById (Long id){
		return userRepository.findUserById(id).orElseThrow(()->
				new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

	}

	public void removeUserById (Long id){
		boolean isUserExists = participantRepository.existsByUserId(id);
		if (isUserExists){
			throw new BadRequestException(String.format(ErrorMessage.PROJECT_CAN_NOT_BE_DELETED_MESSAGE,id));
		}
		User user = getById(id);
		if (user.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}




		//boolean exist=	projectService.existsByUser(user);
		//
		//if (exist) {
		//	throw new BadRequestException(ErrorMessage.CAR_USED_BY_RESERVATION_MESSAGE);
		//}

		//	userRepository.delete(user);

		userRepository.deleteById(id);

	}

	public Set<Role> convertRoles(Set<String> pRoles) {
		Set<Role> roles = new HashSet<>();

		if(pRoles==null) {
			Role userRole =  roleService.findByType(RoleType.ROLE_USER);
			roles.add(userRole);
		}else {
			pRoles.forEach(roleStr->{
				if(roleStr.equals(RoleType.ROLE_ADMIN.getName())) {
					Role adminRole = roleService.findByType(RoleType.ROLE_ADMIN);
					roles.add(adminRole);

				}else {
					Role userRole = roleService.findByType(RoleType.ROLE_USER);
					roles.add(userRole);
				}
			});
		}

		return roles;
	}
}
