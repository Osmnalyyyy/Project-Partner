package com.pp.repository;

import com.pp.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByProject_IdAndUserId(Long projectId,Long userId);

    Participant findParticipantByUserId(Long id);

    boolean existsByProjectId(Long id);

    boolean existsByUserId(Long id);

    Participant findParticipantByProjectIdAndUserId(Long projectId, Long userId);

    @Modifying
    @Query("DELETE FROM Participant p WHERE p.project.id=:projectId")
    void deleteAllParticipantsByProjectId(@Param("projectId") Long projectId);
}