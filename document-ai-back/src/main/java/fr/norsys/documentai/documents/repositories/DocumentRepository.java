package fr.norsys.documentai.documents.repositories;

import fr.norsys.documentai.documents.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

    
    Page<Document> findByFileSizeGreaterThan(Long size, Pageable pageable);

    
    @Query("""
        SELECT d FROM Document d
        WHERE d.createdAt >= :start AND d.createdAt < :end
    """)
    Page<Document> findByCreatedAtToday(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        Pageable pageable
    );

    
    @Query("""
        SELECT d FROM Document d
        WHERE d.updatedAt < :before
    """)
    Page<Document> findByUpdatedAtBefore(
        @Param("before") LocalDateTime before,
        Pageable pageable
    );

}
