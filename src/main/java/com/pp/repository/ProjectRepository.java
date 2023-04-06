package com.pp.repository;

import com.pp.domain.Project;
import com.pp.domain.User;
import com.pp.domain.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query( "SELECT count(p) from Project p join p.projectImage img where img.id=:id")
    Integer findProjectCountByImageId(@Param("id") String id );

    @EntityGraph(attributePaths = {"projectImage","attachments"})
    Optional<Project> findById(Long id);

    @EntityGraph(attributePaths = {"projectImage","attachments"})
    List<Project> findAll();

    List<Project> getProjectsByAdminAdviceIsTrue();


    Page<Project> findProjectsByProjectStatus(Pageable pageable, ProjectStatus status);
}
