package com.pp.dto;

import com.pp.domain.Project;
import com.pp.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantDTO {

    private Integer participantShares;

    private String userId;

    private String projectId;
}
