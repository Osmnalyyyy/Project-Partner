package com.pp.repository;

import com.pp.domain.AttachmentFile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentFileRepository extends JpaRepository<AttachmentFile,String> {
    @EntityGraph(attributePaths = "id")
    List<AttachmentFile> findAll();

    @EntityGraph(attributePaths = "id")
    Optional<AttachmentFile> findAttachmentById(String id);

}
