package com.pp.service;


import com.pp.domain.ContactMessage;
import com.pp.dto.ContactMessageDTO;
import com.pp.dto.request.ContactMessageRequest;
import com.pp.exception.ResourceNotFoundException;
import com.pp.exception.message.ErrorMessage;
import com.pp.mapper.ContactMessageMapper;
import com.pp.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ContactMessageService {

	private final ContactMessageRepository contactMessageRepository;
	private final ContactMessageMapper contactMessageMapper;

	private final UserService userService;


	public ContactMessage findMessageById(Long id) {

		return contactMessageRepository.findById(id).orElseThrow(() ->
				new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

	}

	public ContactMessageDTO saveMessage(ContactMessageRequest contactMessageRequest) {
		ContactMessage contactMessage = contactMessageMapper.contactMessageRequestToContactMessage(contactMessageRequest);
		contactMessage.setEmail(userService.getCurrentUser().getEmail());
		contactMessageRepository.save(contactMessage);
		return contactMessageMapper.contactMessageToDTO(contactMessage);
	}

	public List<ContactMessage> getAll() {
		return contactMessageRepository.findAll();
	}

	public Page<ContactMessageDTO> getAll(Pageable pageable) {
		Page<ContactMessage> contactMessage = contactMessageRepository.findAll(pageable);
		return contactMessage.map(contactMessageMapper::contactMessageToDTO);
	}

	public ContactMessage getContactMessage(Long id) {

		return contactMessageRepository.findById(id).orElseThrow(() ->
//		new ResourceNotFoundException("ContactMessage is not found with id: "+id));---->>>>>  bad code 
		new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

	}

	public ContactMessageDTO deleteContactMessage(Long id) {
		ContactMessage foundContactMessage=getContactMessage(id);
		contactMessageRepository.delete(foundContactMessage);
		return contactMessageMapper.contactMessageToDTO(foundContactMessage);
		
	}
}
