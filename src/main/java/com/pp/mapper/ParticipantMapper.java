package com.pp.mapper;

import com.pp.domain.Participant;
import com.pp.domain.Project;
import com.pp.domain.User;
import com.pp.dto.ParticipantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    @Mapping(source = "user",target = "userId",qualifiedByName = "getUserId")
    @Mapping(source = "project",target = "projectId",qualifiedByName = "getProjectId")
    ParticipantDTO participantToParticipantDTO(Participant participant);

    @Named("getUserId")
    public static String getUserId(User user){
        return user.getId().toString();
    }

    @Named("getProjectId")
    public static String getProjectId(Project project){
        return project.getId().toString();
    }
}
