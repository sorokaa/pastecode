package io.srk.pastecode.code.repository;

import io.srk.pastecode.code.model.dto.CodeMetadataDTO;
import io.srk.pastecode.code.model.entity.Code;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodeRepository extends JpaRepository<Code, UUID> {

    @Query(nativeQuery = true, value = """
                    SELECT *
                    FROM pastecode.code
                    WHERE availability = 'PUBLIC'
                        AND expire_at > now()
                    ORDER BY created DESC
                    LIMIT 10
            """)
    List<Code> findAllLatest();

    @Query("FROM Code WHERE expireAt > CURRENT_TIMESTAMP AND owner.id = :ownerId ORDER BY created DESC")
    Page<Code> findAllByOwnerId(UUID ownerId, Pageable pageable);

    @Query("""
        SELECT new io.srk.pastecode.code.model.dto.CodeMetadataDTO((c.password IS NOT NULL))
        FROM Code c
        WHERE c.id = :id
    """)
    Optional<CodeMetadataDTO> findMetadataById(UUID id);
}
