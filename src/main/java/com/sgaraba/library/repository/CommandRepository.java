package com.sgaraba.library.repository;

import com.sgaraba.library.domain.Command;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Command entity.
 */
@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {

    @Query(value = "select distinct command from Command command left join fetch command.bands",
        countQuery = "select count(distinct command) from Command command")
    Page<Command> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct command from Command command left join fetch command.bands")
    List<Command> findAllWithEagerRelationships();

    @Query("select command from Command command left join fetch command.bands where command.id =:id")
    Optional<Command> findOneWithEagerRelationships(@Param("id") Long id);
}
