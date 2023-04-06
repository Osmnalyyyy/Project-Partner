package com.pp.dto;

import com.pp.domain.AttachmentFile;
import com.pp.domain.ImageFile;
import com.pp.domain.Participant;
import com.pp.domain.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class  ProjectDTO {


    private Long id;

    private String projectTitle;

    private String theme;

    private String slogan;

    private String about;

    private String goal;

    private String support;
    private String createdBy;

    private String shortDescription;

    private String longDescription;

    private LocalDate estimatedImplementationTime;

    private String projectPlace;

    private boolean adminAdvice;

    private ProjectStatus projectStatus;

    private LocalDateTime createdDate;

    private Integer participantCount;

    private Double projectValue;

    private Integer sharesTaken;

    private Integer totalShares;

    private Double shareValue;

    private Integer maxSharesPerPerson;

    private Set<String> projectImage;

    private Set<String> attachments;
}
