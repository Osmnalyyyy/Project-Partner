package com.pp.service;

import com.pp.domain.*;
import com.pp.domain.enums.ProjectStatus;
import com.pp.domain.enums.RoleType;
import com.pp.dto.ProjectDTO;
import com.pp.dto.UserDTO;
import com.pp.dto.request.ProjectRequest;
import com.pp.exception.BadRequestException;
import com.pp.exception.ConflictException;
import com.pp.exception.ResourceNotFoundException;
import com.pp.exception.message.ErrorMessage;
import com.pp.mapper.ProjectMapper;
import com.pp.mapper.UserMapper;
import com.pp.repository.ParticipantRepository;
import com.pp.repository.ProjectRepository;
import com.pp.repository.RoleRepository;
import com.pp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;
    private final ImageFileService imageFileService;

    private final AttachmentFileService attachmentFileService;

    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ParticipantRepository participantRepository;

    public ProjectDTO saveProject(ProjectRequest projectRequest) {

        ImageFile imageFile = imageFileService.findImageById(projectRequest.getImageId());
        AttachmentFile attachmentFile;
        Integer usedProjectCount = projectRepository.findProjectCountByImageId(imageFile.getId());
        Set<AttachmentFile> atchFiles = new HashSet<>();
        for (int i = 0; i <projectRequest.getAttachmentId().size() ; i++) {
           attachmentFile= attachmentFileService.getAttachmentById(projectRequest.getAttachmentId().get(i));
           atchFiles.add(attachmentFile);
        }

        if (usedProjectCount>0){
            throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
        }
        Role role=roleService.findByType(RoleType.ROLE_ADMIN);
        Project project = projectMapper.projectRequestToProject(projectRequest);
        boolean isAdmin=userService.getCurrentUser().getRoles().stream().anyMatch(r->r.equals(role));
        if(isAdmin){
            project.setProjectStatus(ProjectStatus.PUBLISHED);
        }else{
            project.setProjectStatus(ProjectStatus.NOT_PUBLISHED);
        }
        User user=userService.getCurrentUser();
        Set<ImageFile> imgFiles = new HashSet<>();
        imgFiles.add(imageFile);
        project.setAttachments(atchFiles);
      //  DecimalFormat df = new DecimalFormat("#.##");
        String strResult = String.format("%.2f",(projectRequest.getProjectValue()/projectRequest.getTotalShares()));
        strResult = strResult.replace(",",".");
        Double result = Double.parseDouble(strResult);
        project.setShareValue(result);
        project.setCreatedBy(userService.getCurrentUser().getName());
        project.setProjectImage(imgFiles);
        project.setUser(user);
        projectRepository.save(project);


        user.getProjectsCreated().add(project);
        userRepository.save(user);
        return projectMapper.projectToProjectDTO(project);
    }

    public void save(Project project){
        projectRepository.save(project);
    }

    public Project findProjectById(Long id){
        return projectRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,id)));
    }

    public List<ProjectDTO> getAllProjects() {
        List<Project> projectList=projectRepository.findAll();
        return projectMapper.projectListToProjectDTOList(projectList);
    }

    public Page<ProjectDTO> getProjectsByPage(Pageable pageable) {
        Page<Project> projectsPage=projectRepository.findAll(pageable);
        return projectsPage.map(projectMapper::projectToProjectDTO);
    }

    public ProjectDTO getProjectById(Long id) {
        Project project=projectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Project not found"));
        return projectMapper.projectToProjectDTO(project);
    }

    @Transactional
    public ProjectDTO deleteById(Long id) {
        Project project=findProjectById(id);
        boolean isProjectExists = participantRepository.existsByProjectId(id);
        if (isProjectExists){
            throw new BadRequestException(String.format(ErrorMessage.PROJECT_CAN_NOT_BE_DELETED_MESSAGE,id));
        }
        List<User> users = userService.getAllUsers();
        for (User user:users) {
            user.getProjectsFollowed().remove(project);
        }
        for (ImageFile img:project.getProjectImage()) {
         imageFileService.removeById(img.getId());
        }
        for (AttachmentFile attach:project.getAttachments()) {
            attachmentFileService.removeById(attach.getId());
        }
        projectRepository.deleteById(id);
        return projectMapper.projectToProjectDTO(project);
    }


    public ProjectDTO updateProject(Long id,boolean status) {
        Project project=projectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Project not found"));

         if (status){
             project.setProjectStatus(ProjectStatus.PUBLISHED);
         }else{
             project.setProjectStatus(ProjectStatus.NOT_PUBLISHED);
         }
         projectRepository.save(project);
        return projectMapper.projectToProjectDTO(project);
    }

    public ProjectDTO setFollowedProduct(Long id) {
        User user = userService.getCurrentUser();
        Project project = findProjectById(id);
        if (!user.getProjectsFollowed().contains(project)){
            user.getProjectsFollowed().add(project);
        }else user.getProjectsFollowed().remove(project);

        userRepository.save(user);
        return projectMapper.projectToProjectDTO(project);
    }



    public List<UserDTO> getAllParticipantsByProjectId(Long projectId) {
       Project project=findProjectById(projectId);
       Set<Participant> participants=project.getProjectParticipantDetails();
       List<UserDTO> usersDTO=new ArrayList<>();

        for (Participant p: participants) {
            usersDTO.add(userMapper.userToUserDTO(p.getUser()));
        }
       return usersDTO;
    }

    public ProjectDTO updateAdvice(Long projectId,boolean status) {
        Project project=findProjectById(projectId);
        project.setAdminAdvice(status);
        projectRepository.save(project);
        return projectMapper.projectToProjectDTO(project);

    }

    public List<ProjectDTO> getAllAdviceList() {
        List<Project> projectList=projectRepository.getProjectsByAdminAdviceIsTrue();
        return projectMapper.projectListToProjectDTOList(projectList);
    }


    public ProjectDTO deleteUserOwnDelete(Long id) {
        User user=userService.getCurrentUser();
        Project project=projectRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.PROJECT_NOT_FOUND_MESSAGE,id)));
        if(!user.getProjectsCreated().contains(project)){
            throw new BadRequestException(String.format(ErrorMessage.PROJECT_NOT_OWN_MESSAGE,id));
        }
        ProjectDTO projectDTO=projectMapper.projectToProjectDTO(project);
        projectRepository.delete(project);
        return projectDTO;
    }

    public Page<ProjectDTO> getProjectsByPagePublished(Pageable pageable) {
        ProjectStatus status=ProjectStatus.PUBLISHED;
        Page<Project> projectsPage=projectRepository.findProjectsByProjectStatus(pageable,status);
        return  projectsPage.map(projectMapper::projectToProjectDTO);
    }

    public Page<ProjectDTO> getProjectsByPageNotPublished(Pageable pageable) {
        ProjectStatus status=ProjectStatus.NOT_PUBLISHED;
        Page<Project> projectsPage=projectRepository.findProjectsByProjectStatus(pageable,status);
        return  projectsPage.map(projectMapper::projectToProjectDTO);
    }
}
