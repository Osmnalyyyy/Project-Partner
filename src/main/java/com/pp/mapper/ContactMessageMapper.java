package com.pp.mapper;


import com.pp.domain.ContactMessage;
import com.pp.domain.User;
import com.pp.dto.ContactMessageDTO;
import com.pp.dto.request.ContactMessageRequest;
import com.pp.service.UserService;
import lombok.AllArgsConstructor;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ContactMessageMapper {
	ContactMessageDTO contactMessageToDTO(ContactMessage contactMessage);

	ContactMessage contactMessageRequestToContactMessage(ContactMessageRequest contactMessageRequest);


	List<ContactMessageDTO> map(List<ContactMessage> contactMessageList);

	
	
	
	
}
