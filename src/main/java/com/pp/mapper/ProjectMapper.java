package com.pp.mapper;

import com.pp.domain.*;
import com.pp.dto.ProjectDTO;
import com.pp.dto.request.ProjectRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "projectImage",target="projectImage",qualifiedByName = "getImageAsString")
    @Mapping(source = "attachments",target="attachments",qualifiedByName = "getAttachmentAsString")
    ProjectDTO projectToProjectDTO(Project project);


    List<ProjectDTO> projectListToProjectDTOList(List<Project> projectList);

    @Mapping(target="projectImage",ignore = true)
    @Mapping(target="attachments",ignore = true)
    @Mapping(target="shareValue",ignore = true)
    @Mapping(target ="createdBy",ignore = true)
    Project projectRequestToProject(ProjectRequest projectRequest);


    @Named("getImageAsString")
    public static Set<String> getImageId(Set<ImageFile> imageFiles){
        Set<String> imgs;
        imgs = imageFiles.stream().map(imgFile->imgFile.getId().toString()).collect(Collectors.toSet());
        return imgs;
    }

    @Named("getAttachmentAsString")
    public static Set<String> getAttachmentId(Set<AttachmentFile> attachmentFiles){
        Set<String> attachments;
        attachments = attachmentFiles.stream().map(attachmentFile->attachmentFile.getId().toString()).collect(Collectors.toSet());
        return attachments;
    }
}
