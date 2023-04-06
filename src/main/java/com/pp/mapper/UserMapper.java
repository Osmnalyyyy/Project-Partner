package com.pp.mapper;

import com.pp.domain.User;
import com.pp.dto.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;



@Mapper(componentModel = "spring")
public interface UserMapper {
 
	UserDTO userToUserDTO(User user);
	
	List<UserDTO> map(List<User> userList);
	
	
}
