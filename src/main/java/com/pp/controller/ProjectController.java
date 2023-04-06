package com.pp.controller;

import com.pp.domain.Participant;
import com.pp.dto.ProjectDTO;
import com.pp.dto.UserDTO;
import com.pp.dto.request.ProjectRequest;
import com.pp.dto.response.PPResponse;
import com.pp.dto.response.ResponseMessage;
import com.pp.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PPResponse> createProject(@RequestBody ProjectRequest projectRequest){
    ProjectDTO projectDTO = projectService.saveProject(projectRequest);
    PPResponse response = new PPResponse(ResponseMessage.PROJECT_SAVED_RESPONSE_MESSAGE,true,projectDTO);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/all/pages")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Page<ProjectDTO>> getAllProjectsByPage(@RequestParam("page") int page,
                                                                 @RequestParam("size") int size,
                                                                 @RequestParam("sort") String prop,
                                                                 @RequestParam(value="direction",required=false,
                                                                 defaultValue = "ASC")Sort.Direction direction){

        Pageable pageable= PageRequest.of(page,size,Sort.by(direction,prop));
        Page<ProjectDTO> projectDTOs=projectService.getProjectsByPage(pageable);

        return ResponseEntity.ok(projectDTOs);

    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(){
        List<ProjectDTO> allProjects = projectService.getAllProjects();
        return ResponseEntity.ok(allProjects);

    }



    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable("id") Long id){
        ProjectDTO projectDTO=projectService.getProjectById(id);
        return ResponseEntity.ok(projectDTO);
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PPResponse> deleteProjectById(@PathVariable("id") Long id){
        ProjectDTO projectDTO=projectService.deleteById(id);
        PPResponse response = new PPResponse(ResponseMessage.PROJECT_DELETE_RESPONSE_MESSAGE,true,projectDTO);
        return ResponseEntity.ok(response);
    }

    // admin update project by id
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PPResponse> updateProjectById(@PathVariable Long id,boolean status){
        ProjectDTO projectDTO=projectService.updateProject(id,status);
        PPResponse response = new PPResponse(ResponseMessage.PROJECT_UPDATE_RESPONSE_MESSAGE,true,projectDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/auth/favorite/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PPResponse> setFollowedProduct(@PathVariable("id") Long id){
        ProjectDTO projectDTO = projectService.setFollowedProduct(id);
        PPResponse response = new PPResponse(ResponseMessage.PROJECT_FOLLOW_RESPONSE_MESSAGE,true,projectDTO);
        return ResponseEntity.ok(response);
    }


    // admin get all participant
    @GetMapping("/admin/participant/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllParticipantsForProject(@PathVariable("projectId") Long projectId){
        List<UserDTO> participantList=projectService.getAllParticipantsByProjectId(projectId);
        return ResponseEntity.ok(participantList);
    }

    @PatchMapping("/admin/{projectId}/advice")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PPResponse> adminUpdateAdvice(@PathVariable("projectId") Long projectId,
                                                        @RequestParam("status") boolean status){
        ProjectDTO projectDTO=projectService.updateAdvice(projectId,status);
        PPResponse response = new PPResponse(ResponseMessage.PROJECT_ADVICE_RESPONSE_MESSAGE,true,projectDTO);
        return  ResponseEntity.ok(response);
    }

    @GetMapping("/admin/advice")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ProjectDTO>> getAdminAdviceList(){
        List<ProjectDTO> projectDTOS=projectService.getAllAdviceList();
        return ResponseEntity.ok(projectDTOS);
    }

    // user delete own project
    @DeleteMapping("/user/{projectId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PPResponse> userDeleteOwnProject(@PathVariable("projectId") Long projectId){
        ProjectDTO projectDTO=projectService.deleteUserOwnDelete(projectId);
        PPResponse response=new PPResponse(ResponseMessage.PROJECT_DELETE_RESPONSE_MESSAGE,true,projectDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all/published")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Page<ProjectDTO>> getAllPublishedProject(@RequestParam("page") int page,
                                                                   @RequestParam("size") int size,
                                                                   @RequestParam("sort") String prop,
                                                                   @RequestParam(value="direction",required=false,
                                                                           defaultValue = "ASC")Sort.Direction direction){
        Pageable pageable= PageRequest.of(page,size,Sort.by(direction,prop));
        Page<ProjectDTO> projectDTOs=projectService.getProjectsByPagePublished(pageable);

        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("/all/not-published")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ProjectDTO>> getAllNotPublishedProject(@RequestParam("page") int page,
                                                                   @RequestParam("size") int size,
                                                                   @RequestParam("sort") String prop,
                                                                   @RequestParam(value="direction",required=false,
                                                                           defaultValue = "ASC")Sort.Direction direction){
        Pageable pageable= PageRequest.of(page,size,Sort.by(direction,prop));
        Page<ProjectDTO> projectDTOs=projectService.getProjectsByPageNotPublished(pageable);
        return ResponseEntity.ok(projectDTOs);
    }


}
