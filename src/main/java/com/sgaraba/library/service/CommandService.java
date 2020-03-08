package com.sgaraba.library.service;

import com.sgaraba.library.domain.Command;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Command}.
 */
public interface CommandService {

    /**
     * Save a command.
     *
     * @param command the entity to save.
     * @return the persisted entity.
     */
    Command save(Command command);

    /**
     * Get all the commands.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Command> findAll(Pageable pageable);

    /**
     * Get all the commands with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<Command> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" command.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Command> findOne(Long id);

    /**
     * Delete the "id" command.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
