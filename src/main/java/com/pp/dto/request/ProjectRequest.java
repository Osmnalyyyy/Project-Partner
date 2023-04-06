package com.pp.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pp.domain.AttachmentFile;
import com.pp.domain.ImageFile;
import com.pp.domain.Participant;
import com.pp.domain.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
    @NotBlank
    private String projectTitle;
    @NotBlank
    private String theme;
    @NotBlank
    @Size(max = 50)
    private String slogan;
    @NotBlank
    private String about;
    @NotBlank
    private String goal;
    @NotBlank
    private String support;
    @NotBlank
    @Size(max = 200)
    private String shortDescription;
    @NotBlank
    @Size(min = 200)
    private String longDescription;
    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy/MM/dd")
    private LocalDate estimatedImplementationTime;
    @NotBlank
    private String projectPlace;
    @NotNull
    private Double projectValue;
    @NotNull
    private Integer totalShares;
    @NotNull
    private Integer maxSharesPerPerson;

    private String imageId;

    private List<String> attachmentId;


}
