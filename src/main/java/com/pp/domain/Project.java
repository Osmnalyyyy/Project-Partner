package com.pp.domain;

import com.pp.domain.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "t_project")
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( length=50,nullable = false)
    private String projectTitle;
    @Column( length=50,nullable = false)
    private String theme;
    @Column
    private String slogan;
    @Column
    private String about;
    @Column
    private String goal;
    @Column
    private String support;
    @Column
    private String shortDescription;
    @Column
    private String longDescription;
    @Column
    private LocalDate estimatedImplementationTime;
    @Column( length=50,nullable = false)
    private String projectPlace;

    @Column( length=50,nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;
    @Column( length=50,nullable = false)
    private LocalDateTime createdDate= LocalDateTime.now();
    @Column
    private Integer participantCount=0;
    @Column(nullable = false)
    private Double projectValue;
    @Column
    private Integer sharesTaken=0;
    @Column
    private Integer totalShares;
    @Column
    private Double shareValue;
    @Column
    private Integer maxSharesPerPerson;
    @Column
    private String createdBy;
    @Column
    private boolean adminAdvice=false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "project")
    private Set<Participant> projectParticipantDetails = new HashSet<>();


    @OneToMany(orphanRemoval = true)
    @JoinColumn(name="project_id")
    private Set<ImageFile> projectImage;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name="project_id")
    private Set<AttachmentFile> attachments;
}