package com.pp.controller;


import com.pp.domain.ContactMessage;
import com.pp.dto.ContactMessageDTO;
import com.pp.dto.request.ContactMessageRequest;
import com.pp.dto.response.PPResponse;
import com.pp.dto.response.ResponseMessage;
import com.pp.mapper.ContactMessageMapper;
import com.pp.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/contact-message")
public class ContactMessageController {

	private final ContactMessageService contactMessageService;

	private final ContactMessageMapper contactMessageMapper;


	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<PPResponse> createMessage(@Valid @RequestBody ContactMessageRequest contactMessageRequest) {
		ContactMessageDTO contactMessageDTO = contactMessageService.saveMessage(contactMessageRequest);
			PPResponse response = new PPResponse(ResponseMessage.CONTACT_MESSAGE_CREATE_RESPONSE,true,contactMessageDTO);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ContactMessageDTO>> getAllContactMessage() {
		List<ContactMessage> contactMessageList = contactMessageService.getAll();
		List<ContactMessageDTO> contactMessageDTOList = contactMessageMapper.map(contactMessageList);
		return ResponseEntity.ok(contactMessageDTOList);
	}


	@GetMapping("/pages")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<ContactMessageDTO>> getAllContactMessageWithPage(@RequestParam("page") int page,
																				@RequestParam("size") int size,
																				@RequestParam("sort") String prop,
																				@RequestParam(value = "direction", required = true,
																						defaultValue = "DESC") Direction direction) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
		Page<ContactMessageDTO> contactMessagePageDTO = contactMessageService.getAll(pageable);
		return ResponseEntity.ok(contactMessagePageDTO);

	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ContactMessageDTO> getMessageWithPath(@PathVariable("id") Long id) {
		ContactMessage contactMessage = contactMessageService.getContactMessage(id);
		ContactMessageDTO contactMessageDTO = contactMessageMapper.contactMessageToDTO(contactMessage);
		return ResponseEntity.ok(contactMessageDTO);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PPResponse> deleteContactMessage(@PathVariable("id")Long id){
		ContactMessageDTO contactMessageDTO = contactMessageService.deleteContactMessage(id);
		PPResponse response=new PPResponse(ResponseMessage.CONTACT_MESSAGE_DELETE_RESPONSE,true,contactMessageDTO);
		return ResponseEntity.ok(response);
	
	}
}
