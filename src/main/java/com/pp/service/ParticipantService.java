package com.pp.service;

import com.pp.domain.Participant;
import com.pp.domain.Project;
import com.pp.domain.User;
import com.pp.domain.enums.ProjectStatus;
import com.pp.dto.ParticipantDTO;
import com.pp.dto.ProjectDTO;
import com.pp.exception.BadRequestException;
import com.pp.mapper.ParticipantMapper;
import com.pp.repository.ParticipantRepository;
import com.pp.exception.ResourceNotFoundException;
import com.pp.exception.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantMapper participantMapper;
    private final ParticipantRepository participantRepository;
    private final UserService userService;
    private final ProjectService projectService;

    public ParticipantDTO participateForProject(Long projectId, Integer sharesTaken, Long userId) {
        User user = userService.getById(userId);
        Project project = projectService.findProjectById(projectId);
        Integer canBeTaken=project.getTotalShares()-project.getSharesTaken();
        Participant participant;

        if (project.getProjectStatus().equals(ProjectStatus.NOT_PUBLISHED)){
            throw new BadRequestException(String.format(ErrorMessage.PROJECT_NOT_FOUND_MESSAGE,projectId));
        }
        if (user.getProjectsParticipated().contains(project)){
            participant=participantRepository.findParticipantByUserId(user.getId());

            if (!(sharesTaken>canBeTaken)){
                participant.setParticipantShares(participant.getParticipantShares()+sharesTaken);
                int taken=project.getSharesTaken()+sharesTaken;
                project.setSharesTaken(taken);
            }else{
                throw new IllegalStateException(String.format(ErrorMessage.MAX_SHARE_LIMIT_REACHED_MESSAGE,canBeTaken,sharesTaken));
            }
        }else{
            if (!(sharesTaken>canBeTaken)){
                participant=new Participant();
                participant.setParticipantShares(sharesTaken);
                participant.setProject(project);
                participant.setUser(user);
                project.getProjectParticipantDetails().add(participant) ;
                participantRepository.save(participant);
                int taken=project.getSharesTaken()+sharesTaken;
                project.setSharesTaken(taken);
                project.setParticipantCount(project.getParticipantCount()+1);
            }else{
                throw new IllegalStateException(String.format(ErrorMessage.MAX_SHARE_LIMIT_REACHED_MESSAGE,canBeTaken,sharesTaken));
            }
        }

        user.getProjectsParticipated().add(project);

        projectService.save(project);
        userService.save(user);
        return participantMapper.participantToParticipantDTO(participant);
    }


    public Participant getParticipantInfoByUserAndProjectId(Long projectId,Long userId){
        return participantRepository.findByProject_IdAndUserId(projectId,userId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.PARTICIPANT_NOT_FOUND_MESSAGE,userId)));
    }

    public ParticipantDTO save(Participant participant) {
        participantRepository.save(participant);
        return participantMapper.participantToParticipantDTO(participant);
    }

    public boolean isProjectExistsById(Long id){
       return participantRepository.existsByProjectId(id);
    }


    public boolean isUserExistsById(Long id) {
        return participantRepository.existsByUserId(id);
    }

    public ParticipantDTO deleteParticipantByProjectAndUserId(Long projectId, Long userId){
        User user = userService.getById(userId);
        Project project = projectService.findProjectById(projectId);
        Participant participant = participantRepository.findParticipantByProjectIdAndUserId(projectId,userId);
        Integer sharesTaken = participant.getParticipantShares();
        participantRepository.delete(participant);
        user.getProjectsFollowed().remove(project);
        project.setSharesTaken(project.getSharesTaken()-sharesTaken);
        project.setParticipantCount(project.getParticipantCount()-1);
        userService.save(user);
        projectService.save(project);
        return participantMapper.participantToParticipantDTO(participant);
    }

    @Transactional
    public void deleteAllParticipantsOfProject(Long projectId) {
        List<User> userList = userService.getAllUsers();
        participantRepository.deleteAllParticipantsByProjectId(projectId);
        Project project = projectService.findProjectById(projectId);
        for (User user:userList) {
            user.getProjectsParticipated().remove(project);
        }
            project.setSharesTaken(0);
        project.setParticipantCount(0);
        projectService.save(project);
    }
}
