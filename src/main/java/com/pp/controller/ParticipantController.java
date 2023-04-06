package com.pp.controller;

import com.pp.dto.ParticipantDTO;
import com.pp.dto.ProjectDTO;
import com.pp.dto.response.PPResponse;
import com.pp.dto.response.ResponseMessage;
import com.pp.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/{projectId}/auth/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PPResponse> participateForProject(@PathVariable("projectId") Long projectId,
                                                            @PathVariable("userId")Long userId,
                                                            @RequestParam("share") Integer sharesTaken
    ){
        ParticipantDTO participantDTO = participantService.participateForProject(projectId,sharesTaken,userId);
        PPResponse response = new PPResponse(String.format(ResponseMessage.PROJECT_PARTICIPATED_RESPONSE_MESSAGE,projectId),true,participantDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}/auth/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PPResponse> deleteParticipantByUserAndProjectId(@PathVariable("projectId") Long projectId,@PathVariable("userId") Long userId){
        ParticipantDTO participantDTO = participantService.deleteParticipantByProjectAndUserId(projectId,userId);
        PPResponse response = new PPResponse(String.format(ResponseMessage.PROJECT_PARTICIPANT_DELETED_RESPONSE_MESSAGE,userId),true,participantDTO);
    return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}/admin/participants")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PPResponse> deleteAllParticipants(@PathVariable("projectId") Long projectId){
        participantService.deleteAllParticipantsOfProject(projectId);
        PPResponse response = new PPResponse(String.format(ResponseMessage.PROJECT_PARTICIPANTS_DELETED_RESPONSE_MESSAGE,projectId),true,null);
        return ResponseEntity.ok(response);
    }
}
